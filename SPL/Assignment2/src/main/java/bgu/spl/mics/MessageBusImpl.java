package bgu.spl.mics;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static class BusHolder{
		private static MessageBusImpl INSTANCE= new MessageBusImpl();
	}


	public static MessageBus getInstance(){
		return BusHolder.INSTANCE;
	}

	private HashMap<MicroService,Queue<Message>> microQueueHash; //hashmap of each registered microservice and its msg queue
	private HashMap<Class<? extends Event>,Queue<MicroService>> eventHash; // hashmap of each event type and its subscriber queue
	private HashMap<Class<? extends Broadcast>,List<MicroService>> broadHash;// hashmap of each broadcast type and its subscriber queue
	private HashMap<Event,Future> eventFutureHash; // hashmap of specific and its future
	private ReadWriteLock readWriteLock;//for microQueueHash
	private Lock readLockMQ, writeLockMQ; // specific read/write locks
	private Semaphore eventLock; // eventHash locking semaphore
	private Semaphore broadLock; // BroadHash locking semaphore
	private Semaphore futureLock; // eventFutureHash locking semaphore

// locking order: eventLock->broadLock->readWriteLock->futureLock->specific microservice

	private MessageBusImpl(){
		microQueueHash=new HashMap<>();
		eventHash=new HashMap<>();
		broadHash=new HashMap<>();
		eventFutureHash=new HashMap<>();
		readWriteLock=new ReentrantReadWriteLock(true);
		readLockMQ=readWriteLock.readLock();
		writeLockMQ=readWriteLock.writeLock();
		eventLock=new Semaphore(1,true); // all semaphores are single access and fair
		broadLock=new Semaphore(1,true);
		futureLock=new Semaphore(1,true);
	}
	
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		boolean done = false;
		while (!done) { //while subscription is not done
			try {
				eventLock.acquire(); // lock eventlock then try to lock the microQueueHash in order
				if (readLockMQ.tryLock()) {
					try {
						if (!microQueueHash.containsKey(m)) // if not registered throw
							throw new IllegalArgumentException();
						if (!eventHash.containsKey(type)) { // if first ever subscription create new queue and add
							Queue<MicroService> q = new ArrayDeque<>();
							q.add(m);
							eventHash.put(type, q);
						} else { // if not 1st then just add
							Queue<MicroService> q = eventHash.get(type);
							if (q.contains(m))
								throw new IllegalArgumentException();
							q.add(m);
						}
						done = true;
					}catch (Exception e) {
						throw e;
					} finally {
						readLockMQ.unlock(); // no matter what always unlock
					}
				}
			} catch (InterruptedException e) {
			} catch (Exception e) {
				throw e;
			} finally {
				eventLock.release();// no matter what always unlock
			}
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		boolean done=false;
		while (!done) { //while subscription is not done
			try {
				broadLock.acquire();// lock broadLock then try to lock the microQueueHash in order
				if (readLockMQ.tryLock()) {
					try {
						if (!microQueueHash.containsKey(m))// if not registered throw
							throw new IllegalArgumentException();
						if (!broadHash.containsKey(type)) {// if first ever subscription create new queue and add
							List<MicroService> list = new ArrayList<>();
							list.add(m);
							broadHash.put(type, list);
						} else {// if not 1st then just add
							List<MicroService> list = broadHash.get(type);
							if (list.contains(m))
								throw new IllegalArgumentException();
							list.add(m);
						}
						done=true;
					} catch (Exception e) {
						throw e;
					} finally {
						readLockMQ.unlock();// no matter what always unlock
					}
				}
			} catch (InterruptedException e) {
			} catch (Exception e) {
				throw e;
			} finally {
				broadLock.release();// no matter what always unlock
			}
		}
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		try {
			futureLock.acquire();// lock future
			Future<T> f=eventFutureHash.get(e);
			if (!f.isDone())
				f.resolve(result);
			else // double complete throw
				throw new IllegalArgumentException("can not complete a resolved event");
		}
		catch (InterruptedException ex){}
		catch (Exception ex){throw ex;}
		finally {
			futureLock.release(); // always release
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		boolean done=false;

		while(!done)
		{
			try {
				broadLock.acquire();// lock broadLock then try to lock the microQueueHash in order
				if (writeLockMQ.tryLock()) {
					try {
						List<MicroService> list = broadHash.get(b.getClass());
						if (list != null) { //if anyone is subscribed
							for (MicroService m : list) { // send msg to all subscribers
								Queue<Message> temp = microQueueHash.get(m);
								temp.add(b);
								synchronized (m) { // notify microservice that it has new msg
									m.notifyAll();
								}
							}
						}
						done = true;
					} catch (Exception e) {
						throw e;
					} finally {
						writeLockMQ.unlock();// always release
					}
				}
			}
			catch (InterruptedException e){}
			catch (Exception e){throw e;}
			finally {
				broadLock.release();// always release
			}
		}
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		while (true) {
			try {
				eventLock.acquire();// lock eventLock then try to lock the microQueueHash in order
				if (writeLockMQ.tryLock()) {
					try {
						if (futureLock.tryAcquire()) {
							try {
								Queue<MicroService> q = eventHash.get(e.getClass());
								if (q == null || q.isEmpty())
									return null;//unlocks in finally
								Future<T> f = new Future<>();
								eventFutureHash.put(e, f);
								MicroService m = q.poll();
								q.add(m);
								microQueueHash.get(m).add(e);
								synchronized (m) {// notify microservice that it has new msg
									m.notifyAll();
								}
								return f;
							}
							catch (Exception ex){throw ex;}
							finally {
								futureLock.release();// always release
							}
						}
					} catch (Exception ex) {
						throw ex;
					} finally {
						writeLockMQ.unlock();// always release
					}
				}
			}
			catch (InterruptedException ex) { }
			catch (Exception ex){throw ex;}
			finally {
				eventLock.release();
			}
		}
	}


	@Override
	public void register(MicroService m) {
		writeLockMQ.lock(); // lock microQueueHash
		try {
			if (!microQueueHash.containsKey(m)) {
				microQueueHash.put(m, new ArrayDeque());
			}
		} catch (Exception e) {
			throw new IllegalArgumentException();
		} finally {
			writeLockMQ.unlock(); // always release
		}
	}

	@Override
	public void unregister(MicroService m) {//////////////////////////////////////////////////
		unSub(m); // clean all subscriptions before unreg
		writeLockMQ.lock();
		microQueueHash.remove(m);
		writeLockMQ.unlock();
	}
	private void unSub(MicroService m){// clean all subscriptions before unreg
		try {
			eventLock.acquire();
			for (Queue q:eventHash.values()) {// clean all event subscriptions before unreg
				if (q.contains(m))
					q.remove(m);
			}
		}
		catch (InterruptedException e){}
		catch (Exception e){throw e;}
		finally {
			eventLock.release();
		}
		try {
			broadLock.acquire();
			for (List list:broadHash.values()) {// clean all broadcast subscriptions before unreg
				if (list.contains(m))
					list.remove(m);
			}
		}
		catch (InterruptedException e){}
		catch (Exception e){throw e;}
		finally {
			broadLock.release();
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

		while (true) {
			writeLockMQ.lock(); // lock microQueueHash then sync on specific microservice
			synchronized (m) {
				if (microQueueHash.containsKey(m)) { // if not registered then throw and unlock
					if (!microQueueHash.get(m).isEmpty()) { // if not empty poll msg
						Queue<Message> temp = microQueueHash.get(m);
						writeLockMQ.unlock();
						return temp.poll();
					}
					writeLockMQ.unlock(); // if empty unlock writing on microQueueHash and wait on specific microservice (until queue is not empty)
					m.wait();
				} else {
					writeLockMQ.unlock();
					throw new IllegalStateException();
				}

			}
		}
	}
}

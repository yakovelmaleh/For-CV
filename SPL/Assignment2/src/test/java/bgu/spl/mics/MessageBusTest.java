package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import org.junit.experimental.theories.Theories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusTest {
    private class BroadcastTest implements Broadcast
    {
    }
    private class EventTest implements Event<String>
    {
    }

    MessageBus m;
    MicroService micro;

    @BeforeEach
    void setUp() {
        m=MessageBusImpl.getInstance();
        micro = new MicroService("micro") {
            @Override
            protected void initialize() {

            }
        };
    }

    @Test
    void subscribeEvent() { // subscribeEvent correctness
        AttackEvent e= new AttackEvent(10,new PriorityQueue<>());///////////////////////////////////////////
        try {
            m.subscribeEvent(e.getClass(),micro);
        }
        catch (Exception ex)
        {
            assertTrue(false);
        }
    }

    @Test
    void subscribeEvent3() {// subscribeEvent double subscribe to same event exception

        AttackEvent e= new AttackEvent(10,new PriorityQueue<>());
        MicroService mm=new MicroService("asd") {
            @Override
            protected void initialize() {

            }
        };
        try {
            m.subscribeEvent(e.getClass(),mm);
        }
        catch (Exception ex){
            ex.printStackTrace();
            assertTrue(false);
        }
        try{
            m.subscribeEvent(e.getClass(),mm);
            assertTrue(false);
        }
        catch (Exception ex){
        }
    }
    @Test
    void subscribeBroadcast() {// subscribeBroadcast correctness
        BroadcastTest e= new BroadcastTest();
        try {
            m.subscribeBroadcast(e.getClass(),micro);
        }
        catch (Exception ex)
        {
            assertTrue(false);
        }
    }
    @Test
    void subscribeBroadcast3() {// subscribeBroadcast double subscribe exception
        BroadcastTest e= new BroadcastTest();
        try {
            m.subscribeBroadcast(e.getClass(),micro);
        }
        catch (Exception ex){
            assertTrue(false);
        }
        try{
            m.subscribeBroadcast(e.getClass(),micro);
            assertTrue(false);
        }
        catch (Exception ex){
        }
    }

    @Test
    void complete() { // complete correctness test
        EventTest e= new EventTest();
        m.subscribeEvent(e.getClass(),micro);
        Future<String> f=m.sendEvent(e);
        String s= "test";
        m.complete(e,s);
        assertEquals(s,f.get());
    }
    @Test
    void complete1() { // complete the same event twice exception
        try {
            EventTest e = new EventTest();
            m.subscribeEvent(e.getClass(), micro);
            Future<String> f = m.sendEvent(e);
            String s = "test";
            m.complete(e, s);
            m.complete(e, "test2");
            assertTrue(false);
        }
        catch (Exception e)
        {}

    }

    @Test
    void sendBroadcast() { // sendBroadcast correctness
        try {
            BroadcastTest b = new BroadcastTest();
            m.subscribeBroadcast(b.getClass(), micro);
            m.sendBroadcast(b);
        }
        catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    void sendBroadcast2() { // sendBroadcast test that broadcast is recived by all subscribed
        class BroadTest1 implements Broadcast{}
        try {
            ArrayList<Boolean> arr=new ArrayList<>();
            MicroService micro2= new MicroService("micro2") {
                @Override
                protected void initialize() {

                }
            };
            Thread t1=new Thread(()-> {
                try {
                    m.awaitMessage(micro);
                    arr.add(true);
                } catch (InterruptedException e) {
                }
            });
            Thread t2=new Thread(()-> {
                try {
                    m.awaitMessage(micro2);
                    arr.add(true);
                } catch (InterruptedException e) {
                }
            });
            BroadTest1 b = new BroadTest1();
            m.subscribeBroadcast(b.getClass(), micro);
            m.subscribeBroadcast(b.getClass(),micro2);
            m.sendBroadcast(b);
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            assertEquals(2,arr.size());
        }
        catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    void sendEvent() { // sendEvent correctness
        try {
            EventTest e = new EventTest();
            m.subscribeEvent(e.getClass(), micro);
            Future<String> f=m.sendEvent(e);
            assertNotNull(f);
        }
        catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    void sendEvent1() { // sendEvent null if no subscribed
        class EventTest2 implements Event<String>{}
        try {
            EventTest2 e = new EventTest2();

            Future<String> f=m.sendEvent(e);
            assertNull(f);
        }
        catch (Exception e){
            assertTrue(false);
        }
    }

    @Test
    void sendEvent2() { // sendEvent null with and without register
        class EventTest2 implements Event<String>{}
        try {
            EventTest2 e=new EventTest2();
            Future<String> f=m.sendEvent(e);
            assertNull(f);
            f=m.sendEvent(e);
            assertNull(f);
        }
        catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void sendEvent3() { // sendEvent test that only subscribed events return future
        class EventTest2 implements Event<Boolean> {
        }
        try {
            EventTest e = new EventTest();
            m.subscribeEvent(e.getClass(),micro);
            EventTest2 e2= new EventTest2();
            Future<Boolean> f=m.sendEvent(e2);
            assertNull(f);
            m.subscribeEvent(e2.getClass(),micro);
            f=m.sendEvent(e2);
            assertNotNull(f);
        }
        catch (Exception e){
            assertTrue(false);
        }
    }
    @Test
    void sendEvent4() { // sendEvent given 2 threads subscribed only one will recive each event
        class EventTest1 implements Event<String>{}
        try {
            ArrayList<Boolean> arr=new ArrayList<>();
            MicroService micro2= new MicroService("micro2") {
                @Override
                protected void initialize() {

                }
            };
            Thread t1=new Thread(()-> {
                try {
                    m.awaitMessage(micro);
                    arr.add(true);
                } catch (InterruptedException e) {
                }
            });
            Thread t2=new Thread(()-> {
                try {
                    m.awaitMessage(micro2);
                    arr.add(true);
                } catch (InterruptedException e) {
                }
            });
            EventTest1 e = new EventTest1();
            m.subscribeEvent(e.getClass(), micro);
            m.subscribeEvent(e.getClass(),micro2);
            m.sendEvent(e);
            t1.start();
            t2.start();
            Thread.sleep(1000);
            assertEquals(1,arr.size());
        }
        catch (Exception e){
            assertTrue(false);
        }
    }


    @Test
    void awaitMessage() { // awaitMessage correctness
        class EventTest1 implements Event<String> {}
        class BroadTest1 implements Broadcast{}
        try {
            MicroService mt=new MicroService("am") {
                @Override
                protected void initialize() {

                }
            };
            EventTest1 e = new EventTest1();
            m.subscribeEvent(e.getClass(), mt);
            m.sendEvent(e);
            Message mes=m.awaitMessage(mt);
            assertEquals(e,mes);
            MicroService micro2=new MicroService("micro2") {
                @Override
                protected void initialize() {

                }
            };
            BroadTest1 b=new BroadTest1();
            m.subscribeBroadcast(b.getClass(),micro2);
            m.sendBroadcast(b);
            mes=m.awaitMessage(micro2);
            assertEquals(b,mes);
        }
        catch (Exception ex){
            assertTrue(false);
        }
    }
    @Test
    void awaitMessage2() { // awaitMessage queue ordering test both event and broadcast
        try {
            m.subscribeEvent(EventTest.class, micro);
            EventTest e=new EventTest();
            m.sendEvent(e);
            m.subscribeBroadcast(BroadcastTest.class,micro);
            BroadcastTest b=new BroadcastTest();
            m.sendBroadcast(b);
            for(int i=0; i<10; i++){
                m.sendEvent(new EventTest());
                m.sendBroadcast(new BroadcastTest());
            }
            Message mes=m.awaitMessage(micro);
            assertEquals(e, mes);
            mes=m.awaitMessage(micro);
            assertEquals(b, mes);
        }
        catch (InterruptedException ex){
            assertTrue(false);
        }
        catch (Exception ex){
            assertTrue(false);
        }
    }
    @Test
    void awaitMessage3() { // awaitMessage test if blocking (EVENT)
        class EventTest2 implements Event<String>{}
        class EventTest1 implements Event<String>{}
        try {
            ArrayList<Boolean> b=new ArrayList();
            MicroService mt=new MicroService("mt") {
                @Override
                protected void initialize() {

                }
            };
            Thread t= new Thread(()-> {
                try {
                    m.awaitMessage(mt);
                } catch (InterruptedException e) {
                    b.add(true);
                }
            });
            m.subscribeEvent(EventTest1.class,mt);
            m.sendEvent(new EventTest2());
            t.start();
            Thread.sleep(100);
            assertTrue(Thread.State.WAITING==t.getState() | Thread.State.BLOCKED==t.getState());
            m.sendEvent(new EventTest1());
            t.join();
            assertEquals(0,b.size());
        }
        catch (InterruptedException ex){
            assertTrue(false);
        }
        catch (Exception ex){
            ex.printStackTrace();
            assertTrue(false);
        }
    }
    @Test
    void awaitMessage4() { // awaitMessage test subscribe while blocking (EVENT)
        class EventTest2 implements Event<String>{}
        try {
            ArrayList<Boolean> b=new ArrayList();
            Thread t= new Thread(()-> {
                try {
                    m.awaitMessage(micro);
                } catch (InterruptedException e) {
                    b.add(true);
                }
            });
            m.subscribeEvent(EventTest.class,micro);
            t.start();
            Thread.sleep(100);
            assertTrue(Thread.State.WAITING==t.getState() | Thread.State.BLOCKED==t.getState());
            m.subscribeEvent(EventTest2.class,micro);
            m.sendEvent(new EventTest2());
            t.join();
            assertEquals(0,b.size());
        }
        catch (InterruptedException ex){
            assertTrue(false);
        }
        catch (Exception ex){
            assertTrue(false);
        }
    }
    @Test
    void awaitMessage5() {  // awaitMessage test if blocking (BROADCAST)
        class BroadcastTest2 implements Broadcast{}
        try {
            ArrayList<Boolean> b=new ArrayList();
            Thread t= new Thread(()-> {
                try {
                    m.awaitMessage(micro);
                } catch (InterruptedException e) {
                    b.add(true);
                }
            });
            m.subscribeBroadcast(BroadcastTest.class,micro);
            m.sendBroadcast(new BroadcastTest2());
            t.start();
            Thread.sleep(100);
            assertTrue(Thread.State.WAITING==t.getState() | Thread.State.BLOCKED==t.getState());
            m.sendBroadcast(new BroadcastTest());
            t.join();
            assertEquals(0,b.size());
        }
        catch (InterruptedException ex){
            assertTrue(false);
        }
        catch (Exception ex){
            assertTrue(false);
        }
    }
    @Test
    void awaitMessage6() { // awaitMessage test subscribe while blocking (BROADCAST)
        class BroadcastTest2 implements Broadcast{}
        try {
            ArrayList<Boolean> b=new ArrayList();
            Thread t= new Thread(()-> {
                try {
                    m.awaitMessage(micro);
                } catch (InterruptedException e) {
                    b.add(true);
                }
            });
            t.start();
            Thread.sleep(100);
            assertTrue(Thread.State.WAITING==t.getState() | Thread.State.BLOCKED==t.getState());
            m.subscribeBroadcast(BroadcastTest.class,micro);
            m.sendBroadcast(new BroadcastTest());
            t.join();
            assertEquals(0,b.size());
        }
        catch (InterruptedException ex){
            assertTrue(false);
        }
        catch (Exception ex){
            assertTrue(false);
        }
    }
    @Test
    void awaitMessage7() { // awaitMessage throw interrupted exeption on interrupt
        try {
            ArrayList<Boolean> b=new ArrayList();
            Thread t= new Thread(()-> {
                try {
                    m.awaitMessage(micro);
                } catch (InterruptedException e) {
                    b.add(true);
                }
            });
            t.start();
            Thread.sleep(100);
            assertTrue(Thread.State.WAITING==t.getState() | Thread.State.BLOCKED==t.getState());
            t.interrupt();
            Thread.sleep(100);
            assertEquals(1,b.size());
        }
        catch (InterruptedException ex){
            assertTrue(false);
        }
        catch (Exception ex){
            assertTrue(false);
        }
    }


}
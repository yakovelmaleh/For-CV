package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {

    private Future<String> future;

    @BeforeEach
    public void setUp(){
        future = new Future<>();
    }

    @Test
    public void testGet(){ // get correctness
        String s="test";
        future.resolve(s);
        String output=future.get();
        assertNotNull(output,"result not null");
        assertTrue(s.equals(output), "the result should be "+s + " and the result is " +output);
    }
    @Test
    public void testGet2(){ // get blocking
        boolean get=false;
        try {
            Thread t= new Thread(()-> future.get());
            t.start();
            get=true;
            Thread.sleep(10);
            assertTrue(t.getState()!= Thread.State.BLOCKED);
            future.resolve("end");
        }
        catch (Exception e) {//test for us
            assertTrue(get,"implementation of get is not good");
        }

    }
    @Test
    public void testGet3(){ // get resume upon resolve from outside thread
        boolean get=false;
        try {
            String s="test";
            ArrayList<String> a=new ArrayList<>();
            Thread t= new Thread(()-> a.add(future.get()));
            t.start();
            get=true;
            Thread.sleep(10);
            assertTrue(t.getState()!= Thread.State.BLOCKED);
            assertEquals(a.size(),0);
            future.resolve(s);
            Thread.sleep(10);
            assertEquals(1,a.size());
            assertEquals(a.get(0),s);
        }
        catch (Exception e) {//test for us
            assertTrue(get,"implementation of get is not good");
        }

    }
    @Test
    public void testIsDone(){ // isDone correctness
        assertFalse(future.isDone());
        future.resolve("test");
        assertTrue(future.isDone());
    }

    @Test
    public void testGetTimeOut(){ // getTimeout correctness
        String s="test";
        future.resolve(s);
        String output=future.get(50, TimeUnit.SECONDS);
        assertNotNull(output,"result not null");
        assertTrue(s.equals(output), "the result should be "+s + " and the result is " +output);
    }
    @Test
    public void testGetTimeOut2(){ // getTimeout resume upong relove from outside thread *before* timeout
        ArrayList<String> a=new ArrayList<>();
        Thread t=new Thread(()-> a.add(future.get(50, TimeUnit.SECONDS)));
        t.start();
        try {
            Thread.sleep(2000);
            String s="test";
            future.resolve(s);
            Thread.sleep(5000);
            assertTrue(a.size()==1);
            assertNotNull(a.get(0));
            assertEquals(s, a.get(0), "the result should be " + s + " and the result is " + a.get(0));
        }
        catch (InterruptedException e){
            assertTrue(false, "test exception");
        }
    }
    @Test
    public void testGetTimeOut3(){// getTimeout resume upong relove from outside thread *after* timeout
        ArrayList<String> a=new ArrayList<>();
        Thread t=new Thread(()-> a.add(future.get(5, TimeUnit.SECONDS)));
        t.start();
        try {
            Thread.sleep(10000);
            String s="test";
            future.resolve(s);
            assertTrue(a.size()==1);
            assertNull(a.get(0));
        }
        catch (InterruptedException e){
            assertTrue(false, "test exception");
        }
    }
    @Test
    public void testGetTimeOut4() {// getTimeout test pure timeout
        ArrayList<String> a = new ArrayList<>();

        Thread t = new Thread(() -> a.add(future.get(5, TimeUnit.SECONDS)));
        t.start();
        try {
            assertTrue(a.size() == 0);
            t.join();
            assertTrue(a.size() == 1);
            assertNull(a.get(0));

        }
        catch (InterruptedException e){
            assertTrue(false,"bug");
        }

    }

    @Test
    public void testResolve(){ // given test
        String str = "someResult";
        future.resolve(str);
        assertTrue(future.isDone());
        assertTrue(str.equals(future.get()));
    }
}

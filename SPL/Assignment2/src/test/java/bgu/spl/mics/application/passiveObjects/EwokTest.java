package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {

    Ewok e;

    @BeforeEach
    void setUp() {
        e=new Ewok(1);
    }

    @Test
    void acquire() { // acquire correctness
        assertTrue(e.isAvailable());
        e.acquire();
        assertFalse(e.isAvailable());
    }
    @Test
    void acquire2() { // acquire test wait for release and reaquire
        try {
            Thread t = new Thread(() -> e.acquire());
            e.acquire();
            t.start();
            Thread.sleep(10);
            assertSame(t.getState(), Thread.State.WAITING);
            e.release();
            Thread.sleep(10);
            assertFalse(e.isAvailable());
        }
        catch(Exception e){
            assertTrue(false, "error");
        }
    }
    @Test
    void acquire3() { // acquire deadlock test (shared resource)
        try {
            Thread t1= new Thread(() -> {
                try {
                    e.acquire();
                    Thread.sleep(1000);
                    e.release();
                }
                catch (Exception e){}
            });
            Thread t2= new Thread(() -> {
                try {
                    e.acquire();
                    Thread.sleep(1000);
                    e.release();
                }
                catch (Exception e){}
            });
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        }
        catch(Exception e){
            assertTrue(false, "bug");
        }
    }

    @Test
    void release() { // release test double release exception + correctness
        try {
            if (e.isAvailable())
                e.release();
            assertTrue(false);
        }
        catch (Exception e){
        }
        e.acquire();
        e.release();
        assertTrue(e.isAvailable());
    }
}
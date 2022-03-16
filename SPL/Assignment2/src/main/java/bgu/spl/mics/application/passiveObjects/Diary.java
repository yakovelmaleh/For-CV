package bgu.spl.mics.application.passiveObjects;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private AtomicInteger totalAttacks;
    private long HanSoloFinish=0,C3POFinish=0,R2D2Deactivate=0,LeiaTerminate=0,HanSoloTerminate=0;
    private long C3POTerminate=0,R2D2Terminate=0,LandoTerminate=0;


    private static class DiaryHolder{
        private static Diary INSTANCE= new Diary();
    }
    private Diary(){
        totalAttacks=new AtomicInteger(0);
}

    public void setC3POFinish(long c3POFinish) {
        C3POFinish = c3POFinish;
    }

    public void setHanSoloFinish(long hanSoloFinish) {
        HanSoloFinish = hanSoloFinish;
    }

    public void setR2D2Deactivate(long r2D2Deactivate) {
        R2D2Deactivate = r2D2Deactivate;
    }

    public void setLeiaTerminate(long leiaTerminate) {
        LeiaTerminate = leiaTerminate;
    }

    public void setHanSoloTerminate(long hanSoloTerminate) {
        HanSoloTerminate = hanSoloTerminate;
    }

    public void setR2D2Terminate(long r2D2Terminate) {
        R2D2Terminate = r2D2Terminate;
    }

    public void setLandoTerminate(long landoTerminate) {
        LandoTerminate = landoTerminate;
    }

    public void setC3POTerminate(long c3POTerminate) {
        C3POTerminate = c3POTerminate;
    }

    public void setTotalAttacks() {
        int oldValue,newValue;
        do{
            oldValue=totalAttacks.intValue();
            newValue=oldValue+1;
        }while (!totalAttacks.compareAndSet(oldValue,newValue));
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    public AtomicInteger getTotalAttacks() {
        return totalAttacks;
    }

    public static Diary getInstance(){
        return DiaryHolder.INSTANCE;
    }

}

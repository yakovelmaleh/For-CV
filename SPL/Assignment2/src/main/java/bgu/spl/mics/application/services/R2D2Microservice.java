package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombBroadcast;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.FinishBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private Callback<DeactivationEvent> deactivationEventCallback;
    private long duration;
    private Callback<FinishBroadcast> finishBroadcastCallback;
    private Diary diary;

    public R2D2Microservice(long duration) {
        super("R2D2");
        diary=Diary.getInstance();
        this.duration=duration;
        deactivationEventCallback=(c)->{
            try {
                Thread.currentThread().sleep(duration);
                diary.setR2D2Deactivate(System.currentTimeMillis());
                sendBroadcast(new BombBroadcast());
            }
            catch (InterruptedException e){}
            catch (Exception e){throw e;}
        };
        subscribeEvent(DeactivationEvent.class,deactivationEventCallback);
        finishBroadcastCallback = (c) -> {
            Thread.currentThread().interrupt();
            diary.setR2D2Terminate(System.currentTimeMillis());
        };
        subscribeBroadcast(FinishBroadcast.class, finishBroadcastCallback);
    }

    @Override
    protected void initialize() {
    }
}

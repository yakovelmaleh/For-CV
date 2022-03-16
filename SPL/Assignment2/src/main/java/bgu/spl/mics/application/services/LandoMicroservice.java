package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombBroadcast;
import bgu.spl.mics.application.messages.FinishBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private Callback<BombBroadcast> bombBroadcastCallback;
    private Callback<FinishBroadcast> finishBroadcastCallback;
    private Diary diary;

    public LandoMicroservice(long duration) {
        super("Lando");
        diary=Diary.getInstance();
        bombBroadcastCallback=(c)-> {
            try {
                Thread.currentThread().sleep(duration);
                sendBroadcast(new FinishBroadcast());
            }
            catch (InterruptedException e){}
            catch (Exception e){throw e;}
        };
        subscribeBroadcast(BombBroadcast.class,bombBroadcastCallback);
        finishBroadcastCallback = (c) -> {
            Thread.currentThread().interrupt();
            diary.setLandoTerminate(System.currentTimeMillis());
        };
        subscribeBroadcast(FinishBroadcast.class, finishBroadcastCallback);
    }

    @Override
    protected void initialize() {
    }
}

package bgu.spl.mics.application.services;


import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AllAttackBroadcast;
import bgu.spl.mics.application.messages.AttackDone;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.FinishBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.ArrayList;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

    private Callback<AttackEvent> attackEventCallback;
    private Ewoks ewoks;
    private ArrayList<Ewok> ewok;
    private Callback<FinishBroadcast> finishBroadcastCallback;
    private Callback<AllAttackBroadcast> allAttackBroadcastCallback;
    private Diary diary;

    public HanSoloMicroservice() {
        super("Han");
        diary=Diary.getInstance();
        ewoks=Ewoks.getInstance(0);
        ewok=new ArrayList<>();
        attackEventCallback=(c)->doAttack(c);
        subscribeEvent(AttackEvent.class,attackEventCallback);
        finishBroadcastCallback = (c) -> {
            Thread.currentThread().interrupt();
            diary.setHanSoloTerminate(System.currentTimeMillis());
        };
        allAttackBroadcastCallback=(c)->diary.setHanSoloFinish(System.currentTimeMillis());
        subscribeBroadcast(AllAttackBroadcast.class,allAttackBroadcastCallback);
        subscribeBroadcast(FinishBroadcast.class, finishBroadcastCallback);
    }

    private void doAttack(AttackEvent c){
        try {
            while (!c.getSerial().isEmpty()) {
                int num = c.getSerial().poll();
                ewok.add(ewoks.getEwok(num));// ewoks are stored in priority queue so always aquire from lowest to highest serial
            }
            Thread.currentThread().sleep(c.getAttackTime());
            diary.setTotalAttacks();
            for (Ewok ew: ewok){
                ew.release();
            }
            sendBroadcast(new AttackDone());
        }
        catch (InterruptedException e){}
        catch (Exception e){throw e;}
    }
    @Override
    protected void initialize() {
    }
}

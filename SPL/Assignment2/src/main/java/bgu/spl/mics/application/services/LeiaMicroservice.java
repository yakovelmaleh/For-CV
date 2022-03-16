package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public LeiaMicroservice(String name) {
        super(name);
    }
	private Attack[] attacks;
    private int counter;
    private Callback<AttackDone> attackDoneCallback;
    private Callback<FinishBroadcast> finishBroadcastCallback;
    private Diary diary;


    public LeiaMicroservice(Attack[] attacks) { // leia functions as a msg hub, distributing tasks and listening for completion
        super("Leia");
        diary=Diary.getInstance();
        this.attacks = attacks;
        counter=0;
        attackDoneCallback= c -> {
            if (++counter == attacks.length)
                sendEvent(new DeactivationEvent());
        };
        subscribeBroadcast(AttackDone.class,attackDoneCallback);
        finishBroadcastCallback = (c) -> {
            Thread.currentThread().interrupt();
            diary.setLeiaTerminate(System.currentTimeMillis());
        };
        subscribeBroadcast(FinishBroadcast.class, finishBroadcastCallback);
    }

    @Override
    protected void initialize() {
        for (Attack att: attacks){
            PriorityQueue<Integer> pq=new PriorityQueue();
            for (Integer i: att.getSerials())
            {
                pq.add(i);
            }
            AttackEvent attEvent= new AttackEvent(att.getDuration(),pq);
            sendEvent(attEvent);
        }
        sendBroadcast(new AllAttackBroadcast());
    }

}

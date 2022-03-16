package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class AttackEvent implements Event<Boolean> {
	private int attackTime;
	private PriorityQueue<Integer> serial;
	private Comparator<Integer> comp;
	public AttackEvent(int time, PriorityQueue<Integer> pq){
	    attackTime=time;
	    serial=pq;
    }
    public int getAttackTime(){return attackTime;}
    public PriorityQueue<Integer> getSerial(){return serial;}
}

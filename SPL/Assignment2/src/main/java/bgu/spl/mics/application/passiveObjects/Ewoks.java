package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;

import java.util.ArrayList;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private ArrayList<Ewok> ewok;
    private static int size=-1;
    private Ewoks(){
        ewok=new ArrayList<>();
        for (int i=0; i<=size;i++)
            ewok.add(new Ewok(i));
    }
    public Ewok getEwok(int i){
        ewok.get(i).acquire();
        return ewok.get(i);
    }

    private static class EwoksHolder{
        private static Ewoks INSTANCE= new Ewoks();
    }
    public static Ewoks getInstance(int size){
        if (Ewoks.size<0)
            Ewoks.size=size;
        return EwoksHolder.INSTANCE;
    }

}

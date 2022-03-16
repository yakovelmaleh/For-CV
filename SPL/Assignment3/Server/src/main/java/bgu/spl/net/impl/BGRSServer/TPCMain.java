package bgu.spl.net.impl.BGRSServer;


import bgu.spl.net.impl.BGRS.EncDec;
import bgu.spl.net.impl.BGRS.Protocol;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args) {
       Server test= Server.threadPerClient(Integer.parseInt(args[0]),()->new Protocol(),()->new EncDec());
       test.serve();
    }
}

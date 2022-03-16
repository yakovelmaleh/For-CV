package bgu.spl.net.impl.BGRS;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EncDec implements MessageEncoderDecoder<Message> {
    ByteBuffer bb=ByteBuffer.allocate(2);
    short opcode=0;
    short refcode=0;
    String str1="";
    String str2="";
    int expDel=0;
    int state=0;
    static final int ENDSTATE=4;


    @Override
    public Message decodeNextByte(byte nextByte) {
        try{
            bb.put(nextByte);
            if(bb.position()==bb.limit() & opcode==0){
                bb.order(ByteOrder.BIG_ENDIAN);
                opcode = bb.getShort(0);
                parseOpcode();

                if(state==ENDSTATE){
                    Message ret =new Message(opcode,refcode,str1,str2);
                    reset();
                    return ret;
                }
                return null;
            }
            if(opcode!=0) {
                if(state==1 & bb.position()==bb.limit()){
                    bb.order(ByteOrder.BIG_ENDIAN);
                    refcode = bb.getShort(0);
                    if(expDel==0) {
                        Message ret =new Message(opcode,refcode,str1,str2);
                        reset();
                        return ret;
                    }
                    else {
                        bb=ByteBuffer.allocate(1);
                        state++;
                    }
                }
                else if(state==2){
                    if(nextByte==0){
                        if(expDel==1){
                            Message ret =new Message(opcode,refcode,str1,str2);
                            reset();
                            return ret;
                        }
                        else{
                            state++;
                            expDel--;
                            bb.clear();
                        }
                    }
                    else{
                        str1+=new String(bb.array(), "UTF-8");
                        bb.clear();
                    }
                }
                else if(state==3){
                    if(nextByte==0){
                        Message ret =new Message(opcode,refcode,str1,str2);
                        reset();
                        return ret;
                    }
                    else{
                        str2+=new String(bb.array(), "UTF-8");
                        bb.clear();
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void reset() {
        bb=ByteBuffer.allocate(2);
        opcode=0;
        refcode=0;
        str1="";
        str2="";
        expDel=0;
    }

    private void parseOpcode() {
        if(opcode<=3){
            expDel=2;
            state=2;
            bb=ByteBuffer.allocate(1);
        }
        else if(opcode==8){
            expDel=1;
            state = 2;
            bb=ByteBuffer.allocate(1);
        }
        else if(opcode==12){
            expDel=1;
            state = 1;
            bb=ByteBuffer.allocate(2);
        }
        else if(opcode==4 | opcode==11){
            state=4;
            bb=ByteBuffer.allocate(0);
        }
        else{
            state=1;
            bb=ByteBuffer.allocate(2);
        }
    }


    @Override
    public byte[] encode(Message message) {
        if(message.getOpcode()<=3) return encodeOp2str(message);
        else if(message.getOpcode()==12) return encodeOpRefStr(message);
        else if(message.getOpcode()==4 || message.getOpcode()==11) return encodeOp(message);
        else if(message.getOpcode()==8) return encodeOpStr(message);
        else return encodeOpRef(message);
    }
    private byte[] encodeOp(Message m){
        byte[] out= new byte[2];
        out[1]=(byte)(m.getOpcode() & 0xff);
        out[0]=(byte)((m.getOpcode() >> 8) & 0xff);
        return out;
    }
    private byte[] encodeOpRef(Message m){
        byte[] out= new byte[4];
        out[1]=(byte)(m.getOpcode() & 0xff);
        out[0]=(byte)((m.getOpcode() >> 8) & 0xff);
        out[3]=(byte)(m.getRefcode() & 0xff);
        out[2]=(byte)((m.getRefcode() >> 8) & 0xff);
        return out;
    }
    private byte[] encodeOpStr(Message m){
        byte[] str1buf=m.str1.getBytes();
        byte[] out= new byte[3+str1buf.length];
        out[1]=(byte)(m.getOpcode() & 0xff);
        out[0]=(byte)((m.getOpcode() >> 8) & 0xff);
        for(int i=0;i<str1buf.length;i++){
            out[i+2]=str1buf[i];
        }
        out[str1buf.length+2]='\0';
        return out;
    }
    private byte[] encodeOp2str(Message m){
        byte[] str1buf=m.str1.getBytes();
        byte[] str2buf=m.str2.getBytes();
        byte[] out= new byte[4+str1buf.length+str2buf.length];

        out[1]=(byte)(m.getOpcode() & 0xff);
        out[0]=(byte)((m.getOpcode() >> 8) & 0xff);
        for(int i=0;i<str1buf.length;i++){
            out[i+2]=str1buf[i];
        }
        out[str1buf.length+2]='\0';
        for(int i=0;i<str2buf.length;i++){
            out[i+3+str1buf.length]=str2buf[i];
        }
        out[str1buf.length+str2buf.length+3]='\0';
        return out;
    }
    private byte[] encodeOpRefStr(Message m){
        byte[] str1buf=m.str1.getBytes();
        byte[] out= new byte[5+str1buf.length];
        out[1]=(byte)(m.getOpcode() & 0xff);
        out[0]=(byte)((m.getOpcode() >> 8) & 0xff);
        out[3]=(byte)(m.getRefcode() & 0xff);
        out[2]=(byte)((m.getRefcode() >> 8) & 0xff);
        for(int i=0;i<str1buf.length;i++){
            out[i+4]=str1buf[i];
        }
        out[str1buf.length+4]='\0';
        return out;
    }
}

package bgu.spl.net.impl.BGRS;

public class Message {
    short opcode=0;
    short refcode=0;
    String str1="";
    String str2="";

    public void setOpcode(short opcode) {
        this.opcode = opcode;
    }

    public void setRefcode(short refcode) {
        this.refcode = refcode;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public short getOpcode() {
        return opcode;
    }

    public short getRefcode() {
        return refcode;
    }

    public String getStr1() {
        return str1;
    }

    public Message(short opcode, short refcode) {
        this.opcode = opcode;
        this.refcode = refcode;
    }

    public Message(short opcode, String str1, String str2) {
        this.opcode = opcode;
        this.str1 = str1;
        this.str2 = str2;
    }

    public Message(short opcode, short refcode, String str1) {
        this.opcode = opcode;
        this.refcode = refcode;
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public Message(short opcode, short refcode, String str1, String str2) {
        this.opcode = opcode;
        this.refcode = refcode;
        this.str1 = str1;
        this.str2 = str2;
    }
}

package bgu.spl.net.impl.BGRS;

import bgu.spl.net.api.MessagingProtocol;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;

public class Protocol implements MessagingProtocol<Message> {
    String user="";
    Database db;
    boolean term=false;
    Message msg;
    Message res;
    Lock SR,SW,CR,CW;


    @Override
    public Message process(Message msg) {
        this.msg=msg;
    //    exec.get(msg.getOpcode()).run();
        switch (msg.opcode) {
            case 1:
                ADMINREG();
                break;
            case 2:
                STUDENTREG();
                break;
            case 3:
                LOGIN();
                break;
            case 4:
                LOGOUT();
                break;
            case 5:
                COURSEREG();
                break;
            case 6:
                KDAMCHECK();
                break;
            case 7:
                COURSESTAT();
                break;
            case 8:
                STUDENTSTAT();
                break;
            case 9:
                ISREGISTERED();
                break;
            case 10:
                UNREGISTER();
                break;
            case 11:
                MYCOURSES();
                break;
            default:
                return null;
        }
        return res;
    }

    @Override
    public boolean shouldTerminate() {
        return term;
    }

    public Protocol(){
        db = Database.getInstance();
        SR=db.getSread();
        SW=db.getSwrite();
        CR=db.getCread();
        CW=db.getCwrite();

    }

    private void MYCOURSES() {
        SR.lock();
        if(user.equals("")  || db.getStudentHash().get(user).Type!= UserType.USER) sendErr();
        else{
            sendAck(db.studentHash.get(user).getCourseList());
        }
        SR.unlock();
    }

    private void UNREGISTER() {
        while(true) {
            SW.lock();
            if (CW.tryLock()) {
                if (!user.equals("") && db.getStudentHash().get(user).courses.contains((int)msg.getRefcode())) {
                    db.getStudentHash().get(user).removeCourse((int)msg.getRefcode());
                    db.getCourseHash().get((int)msg.getRefcode()).removeStudent(user);
                    sendAck();
                } else sendErr();

                CW.unlock();
                SW.unlock();
                return;
            }
            SW.unlock();
        }


    }

    private void ISREGISTERED() {
        while (true) {
            SR.lock();
            if (CR.tryLock()) {
                if (user.equals("") || db.getStudentHash().get(user).Type != UserType.USER || !db.getCourseHash().containsKey((int) msg.getRefcode()))
                    sendErr();
                else {
                    if (db.getStudentHash().get(user).courses.contains((int) msg.getRefcode())) sendAck("REGISTERED");
                    else sendAck("NOT REGISTERED");
                }
                CR.unlock();
                SR.unlock();
                return;
            }
            SR.unlock();
        }
    }

    private void STUDENTSTAT() {
        SR.lock();
        if(user.equals("") ||!db.getStudentHash().containsKey(msg.getStr1()) ||db.getStudentHash().get(msg.getStr1()).getType()!=UserType.USER|| db.getStudentHash().get(user).Type!= UserType.ADMIN) sendErr();
        else{
            sendAck(db.getStudentHash().get(msg.getStr1()).toString());
        }
        SR.unlock();
    }

    private void COURSESTAT() {
        while(true) {
            SR.lock();
            if (CR.tryLock()) {
                if(user.equals("") || !db.getCourseHash().containsKey((int)msg.getRefcode())|| db.getStudentHash().get(user).Type!= UserType.ADMIN) sendErr();
                else{
                    sendAck(db.getCourseHash().get((int)msg.getRefcode()).toString());
                }
                CR.unlock();
                SR.unlock();
                return;
            }
            SR.unlock();
        }
    }

    private void KDAMCHECK() {
        while(true) {
            SR.lock();
            if(CR.tryLock()) {
                if (user.equals("") || !db.getCourseHash().containsKey((int) msg.getRefcode()) || db.getStudentHash().get(user).getType() != UserType.USER)
                    sendErr();
                else {
                    sendAck(db.getCourseHash().get((int) msg.getRefcode()).getKdamString());
                }
                CR.unlock();
                SR.unlock();
                return;
            }
            SR.unlock();
        }
    }

    private void LOGOUT() {
        SW.lock();
        if(user.equals("") ||!db.getStudentHash().containsKey(user) || !db.getStudentHash().get(user).isLogged() ) sendErr();
        else{
            db.getStudentHash().get(user).setLogged(false);
            user="";
            sendAck();
        }
        SW.unlock();
    }

    private void COURSEREG() {
        while(true) {
            SW.lock();
            if (CW.tryLock()) {
                if(user.equals("") ||!db.getStudentHash().containsKey(user) || !db.getCourseHash().containsKey((int)msg.getRefcode())|| db.getStudentHash().get(user).Type!= UserType.USER || db.getStudentHash().get(user).getCourses().contains((int)msg.getRefcode())||!db.getCourseHash().get((int)msg.getRefcode()).checkRoom()) sendErr();
                else{
                    Student s= db.studentHash.get(user);
                    Course c=db.courseHash.get((int)msg.getRefcode());
                    for(int k : c.getKdam()){
                        if(!s.getCourses().contains(k)) {
                            sendErr();
                            CW.unlock();
                            SW.unlock();
                            return;
                        }
                    }
                    if(c.getMaxStudents()==c.getStudents().size()) sendErr();
                    else{
                        s.addCourse(c.ID);
                        c.addStudent(user);
                        sendAck();
                    }
                }

                CW.unlock();
                SW.unlock();
                return;
            }
            SW.unlock();
        }
    }

    private void LOGIN() {
        SW.lock();
        if (!user.equals("") || !db.getStudentHash().containsKey(msg.getStr1()) || !db.getStudentHash().get(msg.getStr1()).getPass().equals(msg.getStr2())
                || db.getStudentHash().get(msg.getStr1()).isLogged()) sendErr();
        else {
            db.getStudentHash().get(msg.getStr1()).setLogged(true);
            user = msg.getStr1();
            sendAck();
        }
        SW.unlock();
    }

    private void STUDENTREG() {
        SW.lock();
        if(!user.equals("") ||db.getStudentHash().containsKey(msg.getStr1())) sendErr();
        else{
            db.getStudentHash().put(msg.getStr1(),new Student(msg.getStr1(),msg.getStr2(),UserType.USER));
            sendAck();
        }
        SW.unlock();
    }

    private void ADMINREG(){

        SW.lock();
        if(!user.equals("") ||db.getStudentHash().containsKey(msg.str1)) sendErr();
        else{
            db.getStudentHash().put(msg.str1,new Student(msg.str1,msg.str2,UserType.ADMIN));
            sendAck();
        }
        SW.unlock();
    }

    private void sendErr() {
        res=new Message((short)13,msg.getOpcode());
    }

    private void sendAck() {
        res=new Message((short)12,msg.getOpcode());
    }
    private void sendAck(String s) {
        res=new Message((short)12,msg.getOpcode(),s);
    }

}

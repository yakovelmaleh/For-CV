package bgu.spl.net.impl.BGRS;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */

public class Database {

    static Database singleton=null;
    //to prevent user from creating new Database

    HashMap<String, Student> studentHash;
    HashMap<Integer, Course> courseHash;
    ReadWriteLock SRW;
    ReadWriteLock CRW;
    Lock Sread,Swrite,Cread,Cwrite;



    public HashMap<String, Student> getStudentHash() {
        return studentHash;
    }

    public HashMap<Integer, Course> getCourseHash() {
        return courseHash;
    }

    public Lock getSread() {
        return Sread;
    }

    public Lock getSwrite() {
        return Swrite;
    }

    public Lock getCread() {
        return Cread;
    }

    public Lock getCwrite() {
        return Cwrite;
    }

    private Database() {
        // TODO: implement
        studentHash=new HashMap<>();
        courseHash=new HashMap<>();
        SRW=new ReentrantReadWriteLock(true);
        CRW=new ReentrantReadWriteLock(true);
        Sread=SRW.readLock();
        Swrite=SRW.writeLock();
        Cread=CRW.readLock();
        Cwrite=CRW.writeLock();
        initialize("Courses.txt");
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Database getInstance() {
        if(singleton==null) singleton=new Database();
        return singleton;
    }

    /**
     * loades the courses from the file path specified
     * into the Database, returns true if successful.
     */
    public boolean initialize(String coursesFilePath) {
        // TODO: implement
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    coursesFilePath));
            String line = reader.readLine();
            while (line != null) {
                parseLine(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void parseLine(String line) {
        String[] splt=line.split("\\|");
        String[] kdam=splt[2].substring(1,splt[2].length()-1).split(",");
        courseHash.put(Integer.parseInt(splt[0]),new Course(Integer.parseInt(splt[0]),splt[1],Integer.parseInt(splt[3]),kdam));
    }
    public String toString(){
        String out="";
        for(Course c: courseHash.values()) out+=c+"\n"+c.getKdamString()+"\n";
        return out;
    }


}
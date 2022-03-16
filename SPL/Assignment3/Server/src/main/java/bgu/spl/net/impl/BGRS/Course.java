package bgu.spl.net.impl.BGRS;

import java.util.LinkedList;
import java.util.List;

public class Course {
    int ID;
    String name;
    int maxStudents;
    List<Integer> kdam;
    List<String> students;

    public String getName() {
        return name;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public Course(int ID, String name, int maxStudents, String[] kdam) {
        this.ID = ID;
        this.name = name;
        this.maxStudents = maxStudents;
        this.kdam=new LinkedList<>();
        this.students=new LinkedList<>();
        for (String s:kdam) {
            if(s!=null && !s.equals("")){
                this.kdam.add(Integer.parseInt(s));}
        }
    }

    public int getID() {
        return ID;
    }

    public List<Integer> getKdam() {
        return kdam;
    }

    public List<String> getStudents() {
        return students;
    }

    public void addStudent(String s){
        students.add(s);
    }
    public boolean removeStudent(String s){
        return students.remove(s);
    }

    @Override
    public String toString() {
        String out="Course: ("+ID+") "+name+"\nSeats Available: "+(maxStudents-students.size())+"/"+maxStudents+"\nStudents Registered: [";
        java.util.Collections.sort(students);
        for(String i: students) out+=i+",";
        if(students.size()>0){
            out=out.substring(0,out.length()-1);
        }
        out+="]";
        return out;
    }

    public String getKdamString() {
        String out="[";
        for(Integer i: kdam) out+=i+",";
        if(kdam.size()>0){
            out=out.substring(0,out.length()-1);
        }
        out+="]";
        return out;
    }
    public boolean checkRoom(){
        return maxStudents>students.size();
    }
}

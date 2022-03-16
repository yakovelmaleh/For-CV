package bgu.spl.net.impl.BGRS;


import java.util.LinkedList;
import java.util.List;

public class Student {
    String username;
    String pass;
    UserType Type;
    List<Integer> courses;
    boolean isLogged;

    public void addCourse(int c){
        courses.add(c);
    }
    public boolean removeCourse(int c){
        return courses.remove((Integer) c);
    }
    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public String getUsername() {
        return username;
    }

    public String getPass() {
        return pass;
    }

    public UserType getType() {
        return Type;
    }

    public List<Integer> getCourses() {
        return courses;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public Student(String username, String pass, UserType type) {
        this.username = username;
        this.pass = pass;
        Type = type;
        this.courses = new LinkedList<>();
        this.isLogged = false;
    }

    @Override
    public String toString() {
        String out="Student: "+username+"\nCourses: [";
        for(int i: courses) out+=i+",";
        if(courses.size()>0){
            out=out.substring(0,out.length()-1);
        }
        out+="]";
        return out;
    }

    public String getCourseList() {
        String out="[";
        for(int i: courses) out+=i+",";
        if(courses.size()>0){
            out=out.substring(0,out.length()-1);
        }
        out+="]";
        return out;
    }
}

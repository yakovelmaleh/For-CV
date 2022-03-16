package Business.Employees.EmployeePKG;

import org.apache.log4j.Logger;

public class TermsOfEmployment {
    final static Logger log = Logger.getLogger(TermsOfEmployment.class);
    private int educationFun;
    private int daysOff;
    private int sickDays;

    public TermsOfEmployment(int[] terms){
        this.educationFun = terms[0];
        this.daysOff = terms[1];
        this.sickDays = terms[2];
    }

    /**
     * copy constructor
     * @param other
     */
    public TermsOfEmployment(TermsOfEmployment other) {
        this.educationFun = other.educationFun;
        this.daysOff = other.daysOff;
        this.sickDays = other.sickDays;
    }

    public int getDaysOff() {
        return daysOff;
    }

    public int getEducationFun() {
        return educationFun;
    }

    public int getSickDays() {
        return sickDays;
    }

    public void setDaysOff(int daysOff){
        this.daysOff = daysOff;
    }

    public void setEducationFun(int educationFun){
        this.educationFun = educationFun;
    }

    public void setSickDays(int sickDays){
        this.sickDays = sickDays;
    }


    public int[] toArr() {
        return new int[]{educationFun,daysOff,sickDays};
    }
}

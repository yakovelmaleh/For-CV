package Business.ApplicationFacade.outObjects;

import Business.Employees.EmployeePKG.*;
import Business.Type.RoleType;

import java.util.*;
import java.util.stream.Collectors;

public class Employee {
    public int EID;
    public String name;
    public List<String> role;
    public int[] bankAccount;
    public int[] terms;
    public int salary;


    //Constructor for getEmployeeDetails();
    public Employee(int EID, String name, List<RoleType> role, BankAccount bankAccount,int salary,  TermsOfEmployment terms) {
        this.EID = EID;
        this.name = name;
        this.role = role.stream().map(Enum::name).collect(Collectors.toUnmodifiableList());
        this.bankAccount = new int[]{bankAccount.getAccountNum(), bankAccount.getBankBranch(), bankAccount.getBankID()};
        this.terms = new int[]{terms.getEducationFun(), terms.getDaysOff(), terms.getDaysOff()};
        this.salary = salary;
    }

    public Employee(Business.Employees.EmployeePKG.Employee emp ){
        this.EID = emp.getEID();
        this.name = emp.getName();
        this.role = emp.getRole().stream().map(Enum::name).collect(Collectors.toUnmodifiableList());
        this.bankAccount = emp.getBankAccount().toArr();
        this.terms = emp.getTermsOfEmployment().toArr();
        this.salary = emp.getSalary();
    }
    public String toStringForAllEmployee(){
        return
                "Employee ID: " + EID +
                ", Name: '" + name + '\'' +
                ", Role: " + role ;
    }
    @Override
    public String toString() {
        return
                "Employee ID: " + EID +
                ", Name: '" + name + '\'' +
                ", Role: " + role +
                ", Account Number: "+ bankAccount[0]+ ", Bank Branch: "+ bankAccount[1]+ ", Bank ID: "+ bankAccount[2]+
                ", Education fund: " + terms[0] +", Days-Off: " + terms[1] +", Sick-Days: " + terms[2] +
                ", Salary: " + salary;
    }
}

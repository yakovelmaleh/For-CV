package Business.Employees.EmployeePKG;

import Business.Type.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Employee {
    private int EID;
    private String name;
    private BankAccount bankAccount;
    private int salary;
    private List<RoleType> role;
    private LocalDate startWorkingDate;
    private TermsOfEmployment termsOfEmployment;

    /**
     * Constructor
     */
    public Employee(int EID, String name, int[] bankDetails, int salary, RoleType role, LocalDate startWorkDate, int[] terms) {
        this.name = name;
        this.salary = salary;
        bankAccount = new BankAccount(bankDetails);
        termsOfEmployment = new TermsOfEmployment(terms);
        this.EID = EID;
        this.role = new ArrayList<>();
        this.role.add(role);
        this.startWorkingDate = startWorkDate;

    }
    public Employee(int EID, String name, int[] bankDetails, int salary, LocalDate startWorkDate, int[] terms) {
        this.name = name;
        this.salary = salary;
        bankAccount = new BankAccount(bankDetails);
        termsOfEmployment = new TermsOfEmployment(terms);
        this.EID = EID;
        this.role = new ArrayList<>();
        this.startWorkingDate = startWorkDate;
    }


    //copy constructor
    public Employee(Employee other){
        this.EID = other.getEID();
        this.name = other.getName();
        this.bankAccount = new BankAccount(other.bankAccount);
        this.salary = other.getSalary();
        this.role = new ArrayList<>(other.getRole());
        this.startWorkingDate = other.getStartWorkingDate();
        termsOfEmployment = new TermsOfEmployment(other.termsOfEmployment);
    }


    public boolean isQualified(RoleType role) {
        return this.role.contains(role);
    }


    /**
     * Getters/Setters
     */
    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public LocalDate getStartWorkingDate() {
        return startWorkingDate;
    }

    public int getEID() {
        return EID;
    }

    public int getSalary() {
        return salary;
    }

    public List<RoleType> getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public TermsOfEmployment getTermsOfEmployment() {
        return termsOfEmployment;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setSalary(int salary) {
        this.salary = salary;
    }
    public void addRole(String role){
        if(!this.role.contains(RoleType.valueOf(role)))
            this.role.add(RoleType.valueOf(role));
    }
    public void setRole(List<String> role) {
        this.role = role.stream().map(RoleType::valueOf).collect(Collectors.toList());
    }
}


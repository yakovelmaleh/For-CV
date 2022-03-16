package Business.Employees.EmployeePKG;

import Business.Type.RoleType;
import java.time.LocalDate;


public class Driver extends Employee{
    private int license;
    public Driver(int EID, String name, int[] bankDetails, int salary, RoleType role, LocalDate startWorkDate, int[] terms, int license) {
        super(EID, name, bankDetails, salary, role, startWorkDate, terms);
        this.license = license;
    }

    public Driver(Employee emp, int license) {
        super(emp);
        this.license = license;
    }

    public int getLicense() {
        return license;
    }
}

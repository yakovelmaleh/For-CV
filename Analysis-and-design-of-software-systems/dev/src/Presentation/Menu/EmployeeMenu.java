package Presentation.Menu;

import Business.ApplicationFacade.ResponseData;
import Business.ApplicationFacade.outObjects.Employee;
import Presentation.Controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class EmployeeMenu extends Menu {
    public EmployeeMenu(Controllers r, Scanner input) {
        super(r, input);
    }

    @Override
    public void show() {
        while (true) {
            System.out.println("\n*********** Employee Menu ***********");
            System.out.println("1) Add new employee");
            System.out.println("2) Fire employee");
            System.out.println("3) Update employee name");
            System.out.println("4) Update employee salary");
            System.out.println("5) Update employee bank details");
            System.out.println("6) Update employee terms of employment");
            System.out.println("7) Add a new role to employee");
            System.out.println("8) Print all employees");
            System.out.println("9) previous menu");
            System.out.println("Choose option: ");
            String option = read();
            switch (option) {
                case "1":
                    addEmployee();
                    break;
                case "2":
                    fireEmployee();
                    break;
                case "3":
                    updateEmp("name");
                    break;
                case "4":
                    updateEmp("salary");
                    break;
                case "5":
                    updateEmp("bank details");
                    break;
                case "6":
                    updateEmp("terms of employment");
                    break;
                case "7":
                    addNewRole();
                    break;
                case "8":
                    printAllEmployees("all");
                    break;
                case "9":
                    return;
                default:
                    System.out.println("Invalid input,please choose a number again");
                    if (goBack()) return;
                    break;
            }
        }
    }


    private void updateEmp(String s) {
        int EID;
        while (true) {
            printAllEmployees("");
            System.out.print("Choose a employee ID to update " + s + ": ");
            EID = enterInt(read());
            if (EID <0 || !r.getRc().checkEIDExists(EID)) {
                System.out.println("Invalid id -  chosen user does not exist.");
                if (goBack()) return;
                else continue;
            }
            break;
        }
        while (true){
            switch (s) {
                case "name":
                    System.out.print("New name: ");
                    String name = read();
                    if(!checkName(name)){
                        System.out.println("invalid name - not alphabetical");
                        if(goBack()) return;
                        else continue;
                    }
                    r.getMc().updateEmployeeName(EID, name);
                    return;
                case "salary":
                    System.out.print("New salary: ");
                    int salary = enterInt(read());
                    if(salary < 0){
                        System.out.println("new salary cannot be negative.");
                        if(goBack()) return;
                        else continue;
                    }
                    r.getMc().updateEmployeeSalary(EID, salary);
                    return;
                case "bank details":
                    printBankDetailsOperation(EID);
                    return;
                case "terms of employment":
                    printTermsOfEmpOperation(EID);
                    return;
            }
        }
    }
    private void fireEmployee() {
        while (true) {
            printAllEmployees("");
            System.out.println("Choose a employee ID to fire");
            int EID = enterInt(read());
            if(!r.getRc().checkEIDExists(EID)){
                System.out.println("Invalid id - chosen id to fire does not exist.");
                if(goBack()) return;
                else continue;
            }
            if(r.getRc().checkIfDriver(EID)){
                System.out.println("Invalid id - cannot fire driver.");
                if(goBack()) return;
                else continue;
            }
            r.getMc().fireEmployee(EID);
            break;
        }
    }

    private void printAllEmployees(String all) {
        System.out.println("All employees of this branch: ");
        ResponseData<List<Employee>> employees = r.getMc().getAllEmployees();
        if (!showError(employees)) {
            if (employees.getData().isEmpty()) System.out.println("No employees in this branch");
            else {
                for (Employee e : employees.getData())
                    if (all.equals("all")) {
                        System.out.println(e.toString());
                    } else
                        System.out.println(e.toStringForAllEmployee());
            }
        }
    }

    private void addEmployee() {
        String name, role;
        int ID, AC, BB, BID, salary, fund, DO, SD, licence;
        System.out.println("In order to create a new employee, enter details for this employee\n");
            ID = getValidNewEmpID();
            if (ID == -1) return;
            name = getNameOfNewEmp();
            if (name.equals("1")) return;
            AC = getBankAccountNumber();
            if (AC == -1) return;
            BB = getBankBranchNumber();
            if (BB == -1) return;
            BID = getBankBID();
            if (BID == -1) return;
            salary = getSalary();
            if (salary == -1) return;
            fund = getEducationFund();
            if (fund == -1) return;
            DO = getDaysOff();
            if (DO == -1) return;
            SD = getSickDays();
            if (SD == -1) return;
            role = chooseRole();
            if(role.equals("Driver")) {
                System.out.print("License number: ");
                licence = enterInt(read());
                r.getDc().addNewDriver(ID,name,new int[]{AC, BB, BID}, salary,LocalDate.now(), new int[]{fund, DO, SD},licence);
            }else
                r.getMc().addEmployee(ID, name, new int[]{AC, BB, BID}, salary, role, LocalDate.now(), new int[]{fund, DO, SD});
    }

    private void printBankDetailsOperation(int EID) {
        while (true) {
            System.out.println("1) Update employee account number");
            System.out.println("2) Update employee branch number");
            System.out.println("3) Update employee bank ID\n");
            System.out.println("4) previous menu");
            System.out.println("Choose option: ");
            String option = read();
            int x;
            switch (option) {
                case "1":
                    System.out.print("New account number: ");
                    x = enterInt(read());
                    if (x <= 0) {
                        System.out.println("invalid input - zero or negative account number.");
                        if (goBack()) return;
                        else continue;
                    }
                    r.getMc().updateEmployeeBANum(EID, x);
                    return;
                case "2":
                    System.out.print("New branch number: ");
                    x = enterInt(read());
                    if (x <= 0) {
                        System.out.println("invalid input - zero or negative branch number.");
                        if (goBack()) return;
                        else continue;
                    }
                    r.getMc().updateEmployeeBABranch(EID, x);
                    return;
                case "3":
                    System.out.print("New bank ID: ");
                    x = enterInt(read());
                    if (x <= 0) {
                        System.out.println("invalid input - zero or negative bank ID.");
                        if (goBack()) return;
                        else continue;
                    }
                    r.getMc().updateEmployeeBAID(EID, x);
                    return;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choose,please choose a number again");
                    if (goBack()) return;
                    break;
            }
        }
    }
    private void printTermsOfEmpOperation(int EID) {
        while (true) {
            System.out.println("1) Update employee education fund");
            System.out.println("2) Update employee days-off");
            System.out.println("3) Update employee sick-days\n");
            System.out.println("4) previous menu");
            System.out.println("Choose option: ");
            String option = read();
            int x;
            switch (option) {
                case "1":
                    System.out.print("New education fund: ");
                    x = enterInt(read());
                    if (x <= 0) {
                        System.out.println("invalid input - zero or negative education fund.");
                        if (goBack()) return;
                        else continue;
                    }
                    r.getMc().updateEmployeeEducationFund(EID, x);
                    return;
                case "2":
                    System.out.print("New days-off: ");
                    x = enterInt(read());
                    if (x < 0) {
                        System.out.println("invalid input - negative days-off.");
                        if (goBack()) return;
                        else continue;
                    }
                    r.getMc().updateEmployeeDaysOff(EID, x);
                    return;
                case "3":
                    System.out.print("New sick-days: ");
                    x = enterInt(read());
                    if (x < 0) {
                        System.out.println("invalid input - negative sick-days.");
                        if (goBack()) return;
                        else continue;
                    }
                    r.getMc().updateEmployeeSickDays(EID, x);
                    return;
                case "4": return;
                default:
                    System.out.println("Invalid input,please choose a number again");
                    if (goBack()) return;
                    break;
            }

        }
    }

    private void addNewRole() {
        while (true) {
            printAllEmployees("");
            System.out.print("Choose a employee ID to add a role: ");
            int EID = enterInt(read());
            if(!r.getRc().checkEIDExists(EID)){
                System.out.println("Chosen id does not exist in system.");
                if(goBack()) return;
                else continue;
            }
            r.getMc().addRoleToEmployee(EID, chooseRole3());
            break;
        }
    }

}

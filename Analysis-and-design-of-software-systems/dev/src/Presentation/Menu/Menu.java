package Presentation.Menu;

import Business.ApplicationFacade.Response;
import Business.ApplicationFacade.ResponseData;
import Business.ApplicationFacade.outObjects.Constraint;
import Business.ApplicationFacade.outObjects.Employee;
import Business.ApplicationFacade.outObjects.Shift;
import Business.SupplierBusiness.facade.Tresponse;
import Business.SupplierBusiness.facade.outObjects.Item;
import Business.SupplierBusiness.facade.outObjects.Order;
import Business.SupplierBusiness.facade.outObjects.SupplierAgreement;
import Business.SupplierBusiness.facade.outObjects.SupplierCard;
import Business.SupplierBusiness.facade.response;
import Presentation.Controllers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public abstract class Menu {
    protected final Scanner input;
    protected final Controllers r;

    public Menu(Controllers r, Scanner input) {
        this.input = input;
        this.r = r;
    }


    protected int enterInt(String s) {
        while (true) {
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                System.out.println("input is not a number, please insert a number");
                s = read();
            }
        }
    }

    protected boolean goBack() {
        System.out.println("\n***[If you wish you go back to previous menu enter 1, else 0]***");
        return read().equals("1");
    }

    protected String read() {
        return input.nextLine();
    }


    protected LocalDate chooseDate() {
        System.out.println("Choose date: ");
        while (true) {
            System.out.println("Year: ");
            int year = enterInt(read());
            System.out.println("Month: ");
            int month = enterInt(read());
            System.out.println("Day: ");
            int day = enterInt(read());
            try {
                return LocalDate.of(year, month, day);
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage() + "\nEnter valid inputs");
            }
        }
    }

    protected String getReason() {
        System.out.println("Reason:");
        return read();
    }

    protected String chooseShiftType() {
        System.out.println("Choose a shift type");
        List<String> shiftTypes = r.getRc().getShiftTypes().getData();
        int counter = 1;
        for (String shiftType : shiftTypes) {
            System.out.println(counter++ + ") " + shiftType);
        }
        int s;
        while (true) {
            s = enterInt(read());
            if (s < 1 || s > shiftTypes.size()) {
                System.out.println("Selected option is not in menu, please try again");
            } else break;
        }
        return shiftTypes.get(s - 1);
    }

    protected DayOfWeek chooseDayOfWeek() {
        System.out.println("Choose the day of the shift");
        int counter = 1;
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.println(counter++ + ") " + day.toString());
        }
        int day;
        while (true) {
            day = enterInt(read());
            if (day < 1 || day > 7) {
                System.out.println("Selected option is not in menu, please try again");
            } else break;
        }
        return DayOfWeek.of(day);
    }

    protected String chooseRole() {
        System.out.println("\nChoose a role");
        List<String> roles = r.getRc().getRoleTypes().getData();
        int counter = 1;
        for (String r : roles) {
            System.out.println(counter++ + ") " + r);
        }
        int s;
        while (true) {
            s = enterInt(read());
            if (s < 1 || s > roles.size()) {
                System.out.println("Selected option is not in menu, please try again");
            } else break;
        }
        return roles.get(s - 1);
    }

    protected String chooseRole2() {
        System.out.println("\nChoose a role");
        List<String> roles = r.getRc().getRoleTypes().getData();
        roles.remove("Driver");
        roles.remove("StoreKeeper");
        int counter = 1;
        for (String r : roles) {
            System.out.println(counter++ + ") " + r);
        }
        int s;
        while (true) {
            s = enterInt(read());
            if (s < 1 || s > roles.size()) {
                System.out.println("Selected option is not in menu, please try again");
            } else break;
        }
        return roles.get(s - 1);
    }

    protected String chooseRole3() {
        System.out.println("\nChoose a role");
        List<String> roles = r.getRc().getRoleTypes().getData();
        roles.remove("Driver");
        int counter = 1;
        for (String r : roles) {
            System.out.println(counter++ + ") " + r);
        }
        int s;
        while (true) {
            s = enterInt(read());
            if (s < 1 || s > roles.size()) {
                System.out.println("Selected option is not in menu, please try again");
            } else break;
        }
        return roles.get(s - 1);
    }

    protected void printMyDetails() {
        ResponseData<Employee> res = r.getRc().getEmployeeDetails();
        if (!showError(res)) {
            System.out.println(res.getData().toString());
        }
    }

    protected boolean showError(Response response) {
        if (response.isError()) {
            System.out.println("ERROR: " + response.getError());
            return true;
        }
        return false;
    }

    protected void printMyShifts() {
        System.out.println("Your Shifts: ");
        ResponseData<List<Shift>> shifts = r.getRc().getMyShifts();
        if (!showError(shifts)) {
            if (shifts.getData().isEmpty()) System.out.println("You don't have any shifts");
            else {
                for (Shift s : shifts.getData())
                    System.out.println(s);
            }
        }
    }

    protected boolean printMyConstraints() {
        System.out.println("Your constraints: ");
        ResponseData<List<Constraint>> constraints = r.getRc().getMyConstraints();
        if (!showError(constraints)) {
            if (constraints.getData().isEmpty()) {
                System.out.println("You don't have any constraint");
                return false;
            } else {
                for (Constraint c : constraints.getData())
                    System.out.println(c);
            }
        }
        return true;
    }

    public abstract void show();

    protected int getValidNewEmpID() {
        while (true) {
            System.out.print("ID: ");
            int num = enterInt(read());
            if (num <= 0) {
                System.out.println("invalid id - negative number.");
                if (goBack()) return -1;
                else
                    continue;
            }
            if (r.getRc().checkEIDExists(num)) {
                System.out.println("Chosen id already exists in system.");
                if (goBack()) return -1;
                else continue;
            }
            return num;
        }
    }

    protected String getNameOfNewEmp() {
        while (true) {
            System.out.print("name: ");
            String name = read();
            if (!checkName(name)) {
                System.out.println("name " + name + " is not alphabetical");
                if (goBack()) return "1";
                else
                    continue;
            }
            return name;
        }
    }

    protected boolean checkName(String name) {
        name = name.replaceAll("\\s+", "");
        return !name.equals("") && name.matches("^[a-zA-Z]*$");
    }

    protected int getSickDays() {
        while (true) {
            System.out.print("sick-days: ");
            int num = enterInt(read());
            if (num < 0) {
                System.out.println("invalid sick days input.");
                if (goBack()) return -1;
                else
                    continue;
            }
            return num;
        }
    }

    protected int getDaysOff() {
        while (true) {
            System.out.print("days-off: ");
            int num = enterInt(read());
            if (num < 0) {
                System.out.println("invalid days off input.");
                if (goBack()) return -1;
                else
                    continue;
            }
            return num;
        }
    }

    protected int getEducationFund() {
        while (true) {
            System.out.print("education fund: ");
            int num = enterInt(read());
            if (num < 0) {
                System.out.println("invalid education fund number");
                if (goBack()) return -1;
                else
                    continue;
            }
            return num;
        }
    }

    protected int getSalary() {
        while (true) {
            System.out.print("salary: ");
            int num = enterInt(read());
            if (num < 0) {
                System.out.println("invalid salary number");
                if (goBack()) return -1;
                else
                    continue;
            }
            return num;
        }
    }

    protected int getBankBID() {
        while (true) {
            System.out.print("bank ID: ");
            int num = enterInt(read());
            if (num <= 0) {
                System.out.println("invalid bank id number.");
                if (goBack()) return -1;
                else
                    continue;
            }
            return num;
        }
    }

    protected int getBankBranchNumber() {
        while (true) {
            System.out.print("bank branch number: ");
            int num = enterInt(read());
            if (num <= 0) {
                System.out.println("invalid branch number.");
                if (goBack()) return -1;
                else
                    continue;
            }
            return num;
        }
    }

    protected int getBankAccountNumber() {
        while (true) {
            System.out.print("bank account number: ");
            int num = enterInt(read());
            if (num <= 0) {
                System.out.println("invalid account number.");
                if (goBack()) return -1;
                else
                    continue;
            }
            return num;
        }
    }

    //TODO: ORI
    protected void cancelDelivery() {
        boolean found = false;
        Scanner scanner = new Scanner(System.in);
        Tresponse<List<SupplierCard>> suppliers = r.getSc().showAllSuppliers();
        if (!suppliers.isError()) {
            for (SupplierCard supplierCard : suppliers.getOutObject()) {
                Tresponse<List<Order>> orders = r.getSc().showAllOrdersOfSupplier(supplierCard.getSupplierBN());
                if (!orders.isError())
                    for (Order order : orders.getOutObject()) {
                        if (!order.getIsArrived() && order.getBranchId() == r.getCurrBID()) {
                            found = true;
                            System.out.println(order.toString());
                            Tresponse<List<Item>> items = r.getSc().showAllItemsOfOrder(supplierCard.getSupplierBN(), Integer.parseInt(order.toStringId()));
                            if (!items.isError()) {
                                Tresponse<SupplierAgreement> supplierAgreement = r.getSc().showSupplierAgreement(supplierCard.getSupplierBN());
                                if (!supplierAgreement.isError())
                                    System.out.println("\tship to us: " + supplierAgreement.getOutObject().toStringShipToUs());
                                List<Item> responseItem = items.getOutObject();
                                for (Item item : responseItem) {
                                    System.out.println(item.toString(order.toStringAmount(item.toStringId())));
                                }
                            }
                        }
                        System.out.println("\n");
                    }
            }
        }
        if(found) {
            String answer;
            int toReturnSupplier;
            while (true) {
                System.out.println("please enter the supplierBN of the order you want to delete");
                try {
                    answer = read(scanner);
                    toReturnSupplier = Integer.parseInt(answer);
                    break;
                } catch (Exception e) {
                    System.out.println("supplierBN does not exist or something want wrong");
                }
            }
            int toReturnOrderId;
            while (true) {
                System.out.println("please enter the orderId you want to delete");
                try {
                    answer = read(scanner);
                    toReturnOrderId = Integer.parseInt(answer);
                    break;
                } catch (Exception e) {
                    System.out.println("orderId does not exist or something want wrong");
                }
            }
            response res = r.getSc().removeOrder(toReturnSupplier, toReturnOrderId);
            if (res.isError()) System.out.println("order cannot be removed");
            else System.out.println("order has been removed");
        }
        else System.out.println("there is no orders for this branch , please return");
    }

    private String read(Scanner scanner) {
        return scanner.nextLine().toLowerCase().replaceAll("\\s", "");
    }

}


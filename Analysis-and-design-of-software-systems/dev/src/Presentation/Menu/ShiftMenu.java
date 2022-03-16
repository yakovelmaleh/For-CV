package Presentation.Menu;

import Business.ApplicationFacade.ResponseData;
import Business.ApplicationFacade.outObjects.Shift;
import Presentation.Controllers;

import java.time.LocalDate;
import java.util.*;

public class ShiftMenu extends Menu {
    public ShiftMenu(Controllers r , Scanner input) {
        super(r, input);
    }

    @Override
    public void show() {
        while (true) {
            System.out.println("\n*************** Shift Menu ***************");
            System.out.println("1) Add employee to shift");
            System.out.println("2) Remove employee from shift");
            System.out.println("3) Create Shift");
            System.out.println("4) Create shifts for next week");
            System.out.println("5) make placement for shifts of next week");
            System.out.println("6) Print all shifts");
            System.out.println("7) Update the amount of a specific role in a shift");
            System.out.println("8) Change drivers");
            System.out.println("9) previous menu");
            System.out.println("Choose option: ");
            String option = read();
            switch (option) {
                case "1":
                    addEmployeeToShift();
                    break;
                case "2":
                    removeEmployeeFromShift();
                    break;
                case "3":
                    createShift();
                    break;
                case "4":
                    List<Shift> s = r.getMc().createWeekShifts().getData();
                    if (!s.isEmpty()) {
                        StringBuilder str = new StringBuilder();
                        s.forEach(shift -> {
                            str.append("Shift Date: ").append(shift.date).append("    Type: ").append(shift.shiftType).append("\n");
                        });
                        str.append("*** Do not have Shift Managers ***");
                        System.out.println(str.toString());
                    } else System.out.println("Successfully created shifts for all week\n");
                    break;
                case "5":
                    r.getMc().selfMakeWeekShifts();
                    System.out.println("\nNext week's shifts were successfully placed automatically\n");
                    break;
                case "6":
                    printAllShifts("all", LocalDate.now().plusWeeks(2));
                    break;
                case "7":
                    updateAmountRole();
                    break;
                case "8":
                    int currBID = r.getCurrBID();
                    changeDriver();
                    r.getMc().EnterBranch(currBID);
                case "9":
                    return;
                default:
                    System.out.println("Invalid input,please choose a number again");
                    if (goBack()) return;
                    break;
            }
        }
    }

    private void changeDriver() {
        if (!printAllDrivers(LocalDate.now().plusWeeks(2))) return;
        int SID = getSID();
        int driverID = getDriverEID(SID);
        if(driverID == -1) return;
        Shift s = r.getMc().getShift(SID);
        List<Integer> optionalDrivers = r.getTc().checkAvailableDriverSubs(driverID,s.getShiftType(),s.getDate(),s.getOptionalDrivers());
        if(optionalDrivers.isEmpty()){
            System.out.println("None available drivers to change at this point. -> [chosen sub driver is unavailable]");
            return;
        }
        System.out.print("Optional Drivers: ");
        optionalDrivers.forEach(id -> {
            System.out.print(id+" ");
        });
        System.out.println("\nChoose an optional driver");
        int  newDriver = enterInt(read());
        if(!optionalDrivers.contains(newDriver)){
            System.out.println("invalid chosen id");
            return;
        }
        r.getTc().swapDrivers(newDriver,driverID,s.getShiftType(),s.getDate());
    }

    private int getDriverEID(int SID) {
        while (true) {
            System.out.print("driver ID to change: ");
            int EID = enterInt(read());
            if (EID < 0) {
                System.out.println("Invalid EID: negative number");
                if(goBack()) return -1;
                continue;
            }
            if (!r.getMc().EIDWorkInSID(SID, EID)) {
                System.out.println("Invalid EID: is not work in this shift");
                continue;
            }
            if (!r.getDc().isDriver(EID)) {
                System.out.println("Error: ID is not driver");
                return -1;
            }
            return EID;
        }
    }

    private void updateAmountRole() {
        if (!printAllShifts("roles", LocalDate.now().plusWeeks(2))) return;
        int SID = getSID();
        String role = chooseRole2();
        System.out.print("New amount: ");
        int amount = getAmount(role);
        r.getMc().updateAmountRole(SID, role, amount);
    }

    private void removeEmployeeFromShift() {
        if (!printAllShifts("all", LocalDate.now().plusWeeks(2))) return;
        int SID = getSID();
        if (r.getMc().shiftIsEmpty(SID)) return;
        int EID = getEIDToRemove(SID);
        if(EID == -1) {
            System.out.println("Didn't remove illegal id, going to pres menu.");
            return;
        }
        r.getMc().removeEmpFromShift(SID, EID);
    }

    private void addEmployeeToShift() {
        if (!printAllShifts("all", LocalDate.now().plusWeeks(2))) return;
        int SID = getSID();
        if (r.getMc().optionalIsEmpty(SID)) {
            System.out.println("all position are full or no optionals");
            return;
        }
        int EID = getEIDToAdd(SID);
        String role = chooseRole2();
        if (!r.getMc().canWork(SID, EID, role)) {
            System.out.println("EID: " + EID + " can't be " + role + "in SID: " + SID);
            return;
        }
        r.getMc().addEmpToShift(SID, EID, role);
    }

    private int getSID() {
        while (true) {
            System.out.print("shift ID: ");
            int SID = enterInt(read());
            if (SID < 0) {
                System.out.println("Invalid SID - negative number");
                continue;
            }
            if (!r.getMc().checkIfSIDExist(SID)) {
                System.out.println("Invalid SID - not exist");
                continue;
            }
            return SID;
        }
    }

    private int getEIDToAdd(int SID) {
        while (true) {
            System.out.print("employee ID: ");
            int EID = enterInt(read());
            if (EID < 0) {
                System.out.println("Invalid EID: negative number");
                continue;
            }
            if (!r.getMc().EIDIsOptionForSID(SID, EID)) {
                System.out.println("Invalid EID: is not optional");
                continue;
            }
            return EID;
        }
    }

    private int getEIDToRemove(int SID) {
        while (true) {
            System.out.print("employee ID: ");
            int EID = enterInt(read());
            if (EID < 0) {
                System.out.println("Invalid EID: negative number");
                continue;
            }
            if (!r.getMc().EIDWorkInSID(SID, EID)) {
                System.out.println("Invalid EID: is not work in this shift");
                continue;
            }
            if (!r.getMc().driverOrStoreKeeper(SID, EID)) {
                System.out.println("Error: cannot remove driver or sorter");
                return -1;
            }
            return EID;
        }
    }
    private boolean printAllDrivers(LocalDate until){
        System.out.println("Shifts and Drivers");
        ResponseData<List<Shift>> shifts = r.getMc().getShifts(until);
        if (shifts.getData().isEmpty()) {
            System.out.println("No shifts in this branch");
            return false;
        }else{
            for (Shift s : shifts.getData()){
                System.out.println(s.strDrivers());
            }
        }
        return true;
    }
    private boolean printAllShifts(String all, LocalDate until) {
        System.out.println("All shifts of this branch until " + until + " :");
        ResponseData<List<Shift>> shifts = r.getMc().getShifts(until);
        if (shifts.getData().isEmpty()) {
            System.out.println("No shifts in this branch");
            return false;
        } else {
            ArrayList<Shift> missing = new ArrayList<>();
            ArrayList<Shift> hasSM = new ArrayList<>();
            for (Shift s : shifts.getData()) {
                if (all.equals("all")) {
                    System.out.println(s.toStringAll());
                    if (s.status.equals("*** Missing ***")) missing.add(s);
                    if (!s.hasShiftManager) hasSM.add(s);
                } else
                    System.out.println(s.toStringWithoutOptAndEmp());
            }
            if (!missing.isEmpty())
                printMissing(missing);
            else if (all.equals("all")) System.out.println("\nAll shifts are staffed in all positions");
            if (!hasSM.isEmpty())
                printMissingShiftManager(hasSM);
        }
        return true;
    }

    private void printMissingShiftManager(ArrayList<Shift> hasSM) {
        System.out.print("*** Shift ID's are missing shift manager: ");
        StringBuilder s = new StringBuilder();
        s.append("[");
        hasSM.forEach(shift -> {
            s.append(shift.SID).append(", ");
        });
        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);
        s.append("] ***");
        System.out.println(s.toString());
    }

    private void printMissing(ArrayList<Shift> missing) {
        System.out.print("*** Shift ID's with unmanned positions: ");
        StringBuilder s = new StringBuilder();
        s.append("[");
        missing.forEach(shift -> {
            s.append(shift.SID).append(", ");
        });
        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);
        s.append("] ***");
        System.out.println(s.toString());
    }


    private void createShift() {
        while (true) {
            System.out.println("To create a new shift enter the following details: ");
            LocalDate date = chooseDate();
            if (LocalDate.now().compareTo(date) > 0) {
                System.out.println("Invalid date : date is from the past");
                continue;
            }
            String shiftType = chooseShiftType();
            if(r.getRc().checkIfShiftExist(date,shiftType)){
                System.out.println("shift already exists on this date");
                continue;
            }
            Map<String, Integer> rolesAmount = chooseRolesAmount();
            r.getMc().createShift(rolesAmount, date, shiftType);
            if (!r.getMc().hasShiftManager(date, shiftType))
                System.out.println("Shift Date:" + date + " has been created BUT does not have a ShiftManager");
            break;
        }
    }

    private Map<String, Integer> chooseRolesAmount() {
        System.out.println("Insert the amount of each role");
        Map<String, Integer> rolesAmount = new HashMap<>();
        List<String> roleTypes = r.getRc().getRoleTypes().getData();
        removeRoles(roleTypes);
        for (String role : roleTypes) {
            System.out.print(role + ": ");
            int amount = getAmount(role);
            rolesAmount.put(role, amount);
        }
        return rolesAmount;
    }

    private void removeRoles(List<String> roleTypes) {
        roleTypes.remove("PersonnelManager");
        roleTypes.remove("BranchManager");
        roleTypes.remove("Driver");
        roleTypes.remove("StoreKeeper");
        roleTypes.remove("LogisticManager");
    }


    private int getAmount(String role) {
        int amount;
        while (true) {
            amount = enterInt(read());
            if (amount < 0) {
                System.out.println("Invalid amount - negative");
                continue;
            }
            if (role.equals("ShiftManager") && amount != 1) {
                System.out.println("Invalid amount - shift manager amount need to be 1");
                continue;
            }
            break;
        }
        return amount;
    }

}

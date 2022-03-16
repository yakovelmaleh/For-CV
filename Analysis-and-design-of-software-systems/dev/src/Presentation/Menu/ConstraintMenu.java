package Presentation.Menu;

import Presentation.Controllers;

import java.time.LocalDate;
import java.util.Scanner;

public class ConstraintMenu extends Menu {

    public ConstraintMenu(Controllers r, Scanner input) {
        super(r, input);
    }

    @Override
    public void show() {
        while (true) {
            System.out.println("\n\n************* Constraint Menu *************\n");
            System.out.println("1) Add const constraint");
            System.out.println("2) Add temporal constraint");
            System.out.println("3) Remove constraint");
            System.out.println("4) Update reason");
            System.out.println("5) Update shift type");
            System.out.println("6) previous menu");
            System.out.println("Choose option: ");
            String option = read();
            switch (option) {
                case "1":
                    addConstConstraint();
                    break;
                case "2":
                    addTempConstraint();
                    break;
                case "3":
                    removeConstraint();
                    break;
                case "4":
                    updateReason();
                    break;
                case "5":
                    updateShiftType();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid input,please choose a number again");
                    if (goBack()) return;
                    break;
            }
        }
    }

    private void addConstConstraint() {
        System.out.println("To add a const constraint details are requested");
        r.getRc().addConstConstraint(chooseDayOfWeek(), chooseShiftType(), getReason());
    }

    private void addTempConstraint() {
        System.out.println("To add a temporal constraint details are requested");
        LocalDate date;
        while (true) {
            date = chooseDate();
            if (date.isBefore(LocalDate.now())) {
                System.out.println("Invalid date - date is from the past");
                if (goBack()) {
                    return;
                } else continue;
            }
            break;
        }
        String shiftT;
        while (true) {
            shiftT = chooseShiftType();
            if (!r.getRc().checkIfShiftExist(date, shiftT)) {
                System.out.println("You can't add constraint for shift that not exists");
                if (goBack()) {
                    return;
                } else continue;
            }
            break;
        }
        if(r.getRc().checkIfShiftIsClose(date,shiftT)){
            System.out.println("You can't add constraint for shift that close");
            return;
        }
        r.getRc().addTempConstraint(date, shiftT, getReason());
    }

    private void removeConstraint() {
        while (true) {
            if (!printMyConstraints()) break;
            System.out.println("Choose a constraint ID to remove");
            int CID = enterInt(read());
            if (!r.getRc().checkConstExist(CID)) {
                System.out.println("Chosen CID does not exist.");
                if (goBack()) return;
                else continue;
            }
            if (!r.getRc().checkIfMyConst(CID)) {
                System.out.println("The chosen constraint is not yours to remove.");
                if (goBack()) return;
                else continue;
            }
            r.getRc().removeConstraint(CID);
            break;
        }
    }

    private void updateShiftType() {
        while (true) {
            if (!printMyConstraints()) break;
            System.out.println("Choose a constraint ID to update shift type");
            int CID = enterInt(read());
            if(!r.getRc().checkConstExist(CID)) {
                System.out.println("Chosen cid is not available.");
                if (goBack()) return;
                else continue;
            }
            if(!r.getRc().checkIfMyConst(CID)){
                System.out.println("Chosen cid is not yours to update.");
                if (goBack()) return;
                else continue;
            }
            r.getRc().updateShiftTypeConstraint(CID, chooseShiftType());
            break;
        }
    }

    private void updateReason() {
        while (true) {
            if (!printMyConstraints()) break;
            System.out.println("Choose a constraint ID to update reason");
            int CID = enterInt(read());
            if(!r.getRc().checkConstExist(CID)) {
                System.out.println("Chosen cid is not available.");
                if (goBack()) return;
                else continue;
            }
            if(!r.getRc().checkIfMyConst(CID)){
                System.out.println("Chosen cid is not yours to update.");
                if (goBack()) return;
                else continue;
            }
            r.getRc().updateReasonConstraint(CID, getReason());
            break;
        }
    }

}

package Presentation.Menu;

import Presentation.Controllers;

import java.util.Scanner;

public class LogisticManagerMenu extends Menu{
    public LogisticManagerMenu(Controllers r, Scanner input) {
        super(r, input);
    }

    @Override
    public void show() {
        while (true) {
            System.out.println("\n\n************* Functions Menu *************");
            System.out.println("1) My details");
            System.out.println("2) My constraints operations");
            System.out.println("3) My shifts and constraints");
            System.out.println("4) Logout");
            System.out.println("5) Transportation Menu");
            System.out.println("6) Cancel delivery");
            System.out.println("Choose an option:");
            String option = read();
            switch (option) {
                case "1":
                    printMyDetails();
                    break;
                case "2":
                    Menu consM = new ConstraintMenu(r, input);
                    consM.show();
                    break;
                case "3":
                    printMyShifts();
                    System.out.println();
                    printMyConstraints();
                    System.out.println();
                    break;
                case "4":
                    r.getRc().Logout();
                    return;
                case "5":
                    int currBID = r.getCurrBID();
                    Menu t = new TransportationMenu(r, input);
                    t.show();
                    r.getMc().EnterBranch(currBID);
                    break;
                case "6":
                    cancelDelivery();
                    break;
                default:
                    System.out.println("Invalid input,please choose a number again");
                    if (goBack()) return;
                    break;
            }
        }
    }
}

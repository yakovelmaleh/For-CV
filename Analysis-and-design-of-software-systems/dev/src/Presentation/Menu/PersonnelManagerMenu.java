package Presentation.Menu;

import Presentation.Controllers;

import java.util.List;
import java.util.Scanner;

public class PersonnelManagerMenu extends Menu {
    public PersonnelManagerMenu(Controllers r, Scanner input) {
        super(r, input);
    }

    @Override
    public void show() {
        checkMessages();
        while (true) {
            System.out.println("\n\n************* Functions Menu *************");
            System.out.println("1) My details");
            System.out.println("2) My constraints operations");
            System.out.println("3) My shifts and constraints");
            System.out.println("4) Logout");
            System.out.println("5) Employee operations menu");
            System.out.println("6) Shift operations menu");
            System.out.println("7) Cancel delivery");
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
                    Menu empM = new EmployeeMenu(r, input);
                    empM.show();
                    break;
                case "6":
                    Menu shftM = new ShiftMenu(r, input);
                    shftM.show();
                    break;
                case "7":
                    cancelDelivery();
                    break;
                default:
                    System.out.println("Invalid input,please choose a number again");
                    if (goBack()) return;
                    break;
            }
        }
    }

    private void checkMessages() {
        List<String>  messages = r.getMc().getMessagesOfManager(r.getCurrBID(),r.getRc().getCurrConnectedEID());
        int i = 1;
        if(messages.isEmpty()) return;
        System.out.println("******** Manager Messages ********");
        for (String m : messages){
            System.out.println(i+") "+ m);
            i++;
        }
        System.out.println("******** End of Messages ********\n");
    }
}

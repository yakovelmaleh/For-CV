package Presentation.Menu;

import Presentation.Controllers;

import java.util.Scanner;

public class BranchManagerMenu extends Menu {
    public BranchManagerMenu(Controllers r, Scanner input) {
        super(r, input);
    }

    @Override
    public void show() {
        while (true) {
            System.out.println("\n\n************* Functions Menu *************");
            System.out.println("1) My details");
            System.out.println("2) Reports");
            System.out.println("3) Suppliers card managements");
            System.out.println("4) Logout");
            System.out.println("Choose an option:");
            String option = read();
            switch (option) {
                case "1":
                    printMyDetails();
                    break;
                case "2":
                    new ReportMenu(r,input).show();
                    break;
                case "3":
                    new SuppliersMenu(r,input).show();
                    break;
                case "4":
                    r.getRc().Logout();
                    return;
                default:
                    System.out.println("Invalid input,please choose a number again");
                    if (goBack()) return;
                    break;
            }
        }
    }
}

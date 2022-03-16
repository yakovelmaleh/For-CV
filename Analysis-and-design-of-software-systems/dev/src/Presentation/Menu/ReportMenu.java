package Presentation.Menu;

import Business.StockBusiness.Fcade.StorageService;
import Presentation.Controllers;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReportMenu extends Menu{
    StorageService SS;
    Scanner scan = new Scanner(System.in);
    final static Logger log = Logger.getLogger(ReportMenu.class);

    public ReportMenu(Controllers r, Scanner input) {
        super(r, input);
        SS=r.getSt();
    }
//    public void start() {
//        SS.loadAllStores();
//        String in;
//        String[] menu = {"exit", "return", "add new store", "use a store"};
//        while (true) {
//            System.out.print("\nWelcome to SuperLi storage management.\n" +
//                    "The current stores are: " + (SS.getStores().isError() ? "None" : SS.getStores().getOutObject()) + "\n"
//            );
//            printM(menu);
//            in = read();
//            if (in.equals("1")) System.exit(0);
//            else if (in.equals("2")) return;
//            else if (in.equals("3")) {
//                SS.addStore();
//                System.out.print("new Store added.\n");
//            }
//            else if (in.equals("4")) useStore();
//            else System.out.print("bad input, try again.\n");
//        }
//    }
    private void printM(String[] menu) {
        for (int i = 1; i <= menu.length; i++) {
            System.out.println(i + ") " + menu[i - 1]);
        }
    }

    @Override
    public void show() {
    reports();
    }
//    private void useStore() {
//        String in;
//        System.out.print("\nWelcome to SuperLi storage store selection.\nPlease enter the store you want to use.\n");
//        in = read();
//        try {
//            int i = Integer.parseInt(in);
//            SS.useStore(i);
//            reports();
//        } catch (Exception e) {
//            System.out.print("no such store exists, try again.\n");
//        }
//    }
    private void reports() {
        String in;
        String[] menu = {"exit", "return", "get weekly report", "get waste report", "get missing report"};
        while (true) {
            System.out.print("\nWelcome to report management for store number: " + SS.getCurrID() + "\n");
            printM(menu);
            in = read();
            if (in.equals("1")) System.exit(0);
            if (in.equals("2")) return;
            if (in.equals("3")) weekly();
            else if (in.equals("4")) System.out.print(SS.getWasteReport().getOutObject());
            else if (in.equals("5")) System.out.print(SS.getNeededReport().getOutObject());
            else System.out.print("bad input, try again.\n");
        }

    }
    private void weekly() {
        String in;
        String[] menu = {"exit", "return", "get the weekly report for the entire store",
                "get the weekly report for a specific categories", "list all categories"};
        while (true) {
            System.out.print("\nWelcome to weekly report management for store number: " + SS.getCurrID() + "\n");
            printM(menu);
            in = read();
            if (in.equals("1")) System.exit(0);
            if (in.equals("2")) return;
            if (in.equals("3")) System.out.print(SS.getWeeklyReport().getOutObject());
            else if (in.equals("4")) weeklyByCat();
            else if (in.equals("5")) System.out.print(SS.getCategories().getOutObject());
            else System.out.print("bad input, try again.\n");
        }
    }

    private void weeklyByCat() {
        String in;
        System.out.print("To get the weekly report for some categories enter the category ID numbers separated by commas\n");
        in = read();
        try {
            String[] ls = in.split(",");
            List<Integer> ils = new ArrayList<>();
            for (String s : ls) {
                ils.add(Integer.parseInt(s));
            }
            System.out.print(SS.getWeeklyReport(ils).getOutObject());
        } catch (Exception e) {
            System.out.print("bad input, try again.\n");
        }
    }

}

package Presentation.Menu;

import Business.StockBusiness.Fcade.StorageService;
import Business.StockBusiness.Fcade.outObjects.NeededReport;
import Business.SupplierBusiness.facade.SupplierService;
import Business.SupplierBusiness.facade.Tresponse;
import Business.SupplierBusiness.facade.outObjects.*;
import Business.SupplierBusiness.facade.response;
import Presentation.Controllers;
import org.apache.log4j.Logger;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;

public class OrdersMenu extends Menu{

    private final SupplierService service;
    final static Logger log = Logger.getLogger(SuppliersMenu.class);

    public OrdersMenu(Controllers r , Scanner input) {
        super(r, input);
        service = r.getSc();
    }

    public void loadData() {
        try {
            service.LoadData();
        } catch (Exception e) {
            System.out.println("there is no data here");
        }
    }

    public SupplierService getService() {
        return service;
    }

    public void setStockService(StorageService service) {
        this.service.setStockService(service);
    }

    public void newData() {
        service.newData();
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);
        String[] mainMenuArray = {"showing methods", "adding methods", "removing methods", "back to choice menu", "END PROGRAM"};
        int option = -1;
        while (true) {
            System.out.println("please select an option: ");
            for (int i = 1; i <= mainMenuArray.length; i++) {
                System.out.println(i + ") " + mainMenuArray[i - 1]);
            }
            option = menuCheck(scanner);
            switch (option) {
                case 1 -> {
                    ordersShowingMethods();
                    break;
                }
                case 2 -> {
                    ordersAddingMethods();
                    break;
                }
                case 3 -> {
                    ordersRemovingMethods();
                    break;
                }
                case 4 -> {
                    return;
                }
                case 5 -> {
                    System.exit(0);
                }
                default -> System.out.println("illegal option!!!");
            }
        }
    }

    private void ordersShowingMethods() {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        int option = -1;
        String[] showingMethodArray = {"show Order Of Supplier", "show All Orders Of Supplier", "show Total Amount",
                /*"show Deliver Time"*/ "back to the main menu", "END PROGRAM"};
        System.out.println("please select the showing method: ");
        while (true) {
            for (int i = 1; i <= showingMethodArray.length; i++) {
                System.out.println(i + ") " + showingMethodArray[i - 1]);
            }
            option = menuCheck(scanner);
            int BN;
            switch (option) {
                case 1 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int orderId = intScan(scanner, "please enter orderId", "orderId must be a number");
                    service.showTotalAmount(BN, orderId);
                    Tresponse<Order> response = service.showOrderOfSupplier(BN, orderId);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else {
                        System.out.println(response.getOutObject().toString());
                        Tresponse<SupplierAgreement> supplierAgreement = service.showSupplierAgreement(BN);
                        if (supplierAgreement.isError()) System.out.println(supplierAgreement.getError() + "\n");
                        else {
                            System.out.println("\tship to us: " + supplierAgreement.getOutObject().toStringShipToUs());
                            //System.out.println("\tconstant time: " + supplierAgreement.getOutObject().toStringConstantTime());
                        }
                        Tresponse<List<Item>> items = service.showAllItemsOfOrder(BN, orderId);
                        if (items.isError()) System.out.println(items.getError() + "\n");
                        else {
                            List<Item> responseItem = items.getOutObject();
                            for (Item item : responseItem) {
                                System.out.println(item.toString(response.getOutObject().toStringAmount(item.toStringId())));
                            }
                        }
                    }
                    toContinue(scanner);
                }
                case 2 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    Tresponse<List<Order>> responsesList = service.showAllOrdersOfSupplier(BN);
                    if (responsesList.isError()) System.out.println(responsesList.getError() + "\n");
                    else {
                        List<Order> responses = responsesList.getOutObject();
                        for (Order order : responses) {
                            System.out.println(order.toString());
                            Tresponse<List<Item>> items = service.showAllItemsOfOrder(BN, Integer.parseInt(order.toStringId()));
                            if (items.isError()) System.out.println(items.getError() + "\n");
                            else {
                                Tresponse<SupplierAgreement> supplierAgreement = service.showSupplierAgreement(BN);
                                if (supplierAgreement.isError())
                                    System.out.println(supplierAgreement.getError() + "\n");
                                else {
                                    System.out.println("\tship to us: " + supplierAgreement.getOutObject().toStringShipToUs());
                                    //System.out.println("\tconstant time: " + supplierAgreement.getOutObject().toStringConstantTime());
                                }
                                List<Item> responseItem = items.getOutObject();
                                for (Item item : responseItem) {
                                    System.out.println(item.toString(order.toStringAmount(item.toStringId())));

                                }
                            }
                        }
                    }
                    toContinue(scanner);
                }
                case 3 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int orderId = intScan(scanner, "please enter orderId", "orderId must be a number");
                    Tresponse<Order> response = service.showTotalAmount(BN, orderId);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("total amount is: " + response.getOutObject().toStringTotalAmount());
                    toContinue(scanner);
                }
//                case 4 -> {
//                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
//                    int orderId = intScan(scanner, "please enter orderId", "orderId must be a number");
//                    Tresponse<Order> response = service.showDeliverTime(BN, orderId);
//                    ///if (response.isError()) System.out.println(response.getError() + "\n");
//                    //else System.out.println("deliver time is : " + response.getOutObject().toStringDeliverTime());
//                    toContinue(scanner);
//                }
                case 4 -> {
                    return;
                }
                case 5 -> {
                    System.exit(0);
                }
                default -> {
                    System.out.println("illegal option!!!");
                    toContinue(scanner);
                }
            }
        }
    }

    private void ordersAddingMethods() {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        int option = -1;
        String[] showingMethodArray = {"add Regular Order", "add Item To Order", "back to the main menu", "END PROGRAM"};
        System.out.println("please select the showing method: ");
        while (true) {
            for (int i = 1; i <= showingMethodArray.length; i++) {
                System.out.println(i + ") " + showingMethodArray[i - 1]);
            }
            option = menuCheck(scanner);
            int BN;
            switch (option) {
                case 1 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    //int branchID = intScan(scanner, "please enter branchId for deliver", "branchId must be a number");
                    Hashtable<Integer, Integer> items = hashScan(scanner);
                    Tresponse<Order> response = service.addRegularOrder(BN, r.getCurrBID(), items);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("orderId is: " + response.getOutObject().toStringId() + "\n");
                    toContinue(scanner);
                }
//                case 2 -> {
//                    Tresponse<NeededReport> tresponse = service.getNeededItems();
//                    if (tresponse.isError()) System.out.println(tresponse.getError() + "\n");
//                    else {
//                        Dictionary<Integer , Integer> dictionary = tresponse.getOutObject().get_list();
//                        for (Integer i : Collections.list(dictionary.keys())) {
//                            String ans = stringScan(scanner, "pres yes to if you want to order the item: " + i);
//                            if (ans.equals("yes")) {
//                                response response = service.addNeededOrder(i, dictionary.get(i), tresponse.getOutObject().getStoreID());
//                                if(response.isError()) System.out.println(response.getError() + "\n");
//                                else System.out.println("The operation was completed successfully\n");
//                            }
//                        }
//                    }
//                }
                case 2 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int orderId = intScan(scanner, "please enter orderId", "orderId must be a number");
                    int itemId = intScan(scanner, "please enter itemId", "itemId must be a number");
                    int amount = intScan(scanner, "please enter amount of the item", "amount must be a number");
                    response response = service.addItemToOrder(BN, orderId, itemId, amount);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 3 -> {
                    return;
                }
                case 4 -> {
                    System.out.println("illegal option!!!\n");
                    toContinue(scanner);
                    System.exit(0);
                }
                default -> {
                    System.out.println("illegal option!!!\n");
                    toContinue(scanner);
                }
            }
        }
    }

    private void ordersRemovingMethods() {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        int option = -1;
        String[] removeMethodArray = {"remove item from regular order", "remove amount of items from regular order",
                "back to the main menu", "END PROGRAM"};
        System.out.println("please select the showing method: ");
        while (true) {
            for (int i = 1; i <= removeMethodArray.length; i++) {
                System.out.println(i + ") " + removeMethodArray[i - 1]);
            }
            option = menuCheck(scanner);
            int BN;
            switch (option) {
                case 1 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int orderId = intScan(scanner, "please enter orderId", "orderId must be a number");
                    int itemId = intScan(scanner, "please enter itemId", "itemId must be a number");
                    response response = service.removeItemFromRegularOrder(BN, orderId, itemId);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 2 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int orderId = intScan(scanner, "please enter orderId", "orderId must be a number");
                    int itemId = intScan(scanner, "please enter itemId", "itemId must be a number");
                    int amount = intScan(scanner, "please enter the amount of the item", "amount must be a number");
                    response response = service.removeAmountItemFromRegularOrder(BN, orderId, itemId, amount);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 3 -> {
                    return;
                }
                case 4 -> {
                    System.exit(0);
                }
                default -> {
                    System.out.println("illegal option!!!");
                    toContinue(scanner);
                }
            }
        }
    }


    private String read(Scanner scanner) {
        return scanner.nextLine().toLowerCase().replaceAll("\\s", "");
    }

    private int intScan(Scanner scanner, String before, String after) {
        int toReturn;
        String answer;
        while (true) {
            System.out.println(before);
            try {
                answer = read(scanner);
                toReturn = Integer.parseInt(answer);
                break;
            } catch (Exception e) {
                System.out.println(after);

            }
        }
        return toReturn;
    }

    private String stringScan(Scanner scanner, String before) {
        System.out.println(before);
        return read(scanner);
    }

    private boolean booleanScan(Scanner scanner, String before, String after) {
        boolean toReturn;
        String answer;
        while (true) {
            System.out.println(before);
            try {
                answer = read(scanner);
                if (answer.equals("true")) toReturn = true;
                else if (answer.equals("false")) toReturn = false;
                else throw new IllegalAccessException("not boolean");
                break;
            } catch (Exception e) {
                System.out.println(after);

            }
        }
        return toReturn;
    }


    private double doubleScan(Scanner scanner, String before, String after) {
        double toReturn;
        String answer;
        while (true) {
            System.out.println(before);
            try {
                answer = read(scanner);
                toReturn = Double.parseDouble(answer);
                break;
            } catch (Exception e) {
                System.out.println(after);

            }
        }
        return toReturn;
    }

    private LocalDate dateScan(Scanner scanner, String rest) {
        LocalDate toReturn;
        while (true) {
            try {
                int year = intScan(scanner, "please enter the year of the " + rest, "year must be a number");
                int month = intScan(scanner, "please enter the month of the " + rest, "month must be a number");
                int day = intScan(scanner, "please enter the day of the " + rest, "day must be a number");
                toReturn = LocalDate.of(year, month, day);
                break;
            } catch (Exception e) {
                System.out.println("illegal values of dates");
            }
        }
        return toReturn;
    }

    private Hashtable<Integer, Integer> hashScan(Scanner scanner) {
        Hashtable<Integer, Integer> items = new Hashtable<>();
        while (true) {
            int itemId = intScan(scanner, "please enter itemId", "itemId must be a number");
            int amount = intScan(scanner, "please enter the amount of the item", "amount must be a number");
            items.put(itemId, amount);
            String toStop = stringScan(scanner, "to put more items please type more");
            if (!toStop.equals("more")) break;
        }
        return items;
    }

    private void toContinue(Scanner scanner) {
        while (true) {
            System.out.println("\nto continue please use enter");
            String isEnter = read(scanner);
            if (isEnter.equals("")) break;
        }
    }

    private int menuCheck(Scanner scanner) {
        String choice;
        int toReturn;
        while (true) {
            try {
                choice = read(scanner);
                toReturn = Integer.parseInt(choice);
                break;
            } catch (NumberFormatException e) {
                System.out.println("illegal!\n please enter a number");
                scanner.nextLine();
            }
        }
        return toReturn;
    }
}

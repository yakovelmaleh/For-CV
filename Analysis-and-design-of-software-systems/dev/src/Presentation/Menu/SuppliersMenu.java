package Presentation.Menu;

import Business.StockBusiness.Fcade.StorageService;
import Business.SupplierBusiness.facade.SupplierService;
import Business.SupplierBusiness.facade.Tresponse;
import Business.SupplierBusiness.facade.outObjects.*;
import Business.SupplierBusiness.facade.response;
import Presentation.Controllers;
import org.apache.log4j.Logger;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;

public class SuppliersMenu extends Menu {

    private final SupplierService service;
    final static Logger log = Logger.getLogger(SuppliersMenu.class);

    public SuppliersMenu(Controllers r , Scanner input) {
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
        String[] mainMenuArray = {"showing methods", "adding methods", "removing methods", "updating methods", "back to choice menu", "END PROGRAM"};
        int option = -1;
        while (true) {
            System.out.println("please select an option: ");
            for (int i = 1; i <= mainMenuArray.length; i++) {
                System.out.println(i + ") " + mainMenuArray[i - 1]);
            }
            option = menuCheck(scanner);
            switch (option) {
                case 1 -> {
                    suppliersShowingMethods();
                    break;
                }
                case 2 -> {
                    suppliersAddingMethods();
                    break;
                }
                case 3 -> {
                    suppliersRemovingMethods();
                    break;
                }
                case 4 -> {
                    suppliersUpdatingMethods();
                    break;
                }
                case 5 -> {
                    return;
                }
                case 6 -> {
                    System.exit(0);
                }
                default -> System.out.println("illegal option!!!");
            }
        }
    }

    private void suppliersShowingMethods() {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        int option = -1;
        String[] showingMethodArray = {"show Supplier", "show SupplierBN", "show All Suppliers", "show Item Of Supplier", "show All Items Of Supplier",
                "show All Items", "show Quantity Document", "show Supplier Agreement", "back to the main menu", "END PROGRAM"};
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
                    Tresponse<SupplierCard> response = service.showSupplier(BN);
                    if (response.isError()) System.out.println(response.getError());
                    else {
                        System.out.println(response.getOutObject().toString());
                        Tresponse<List<Item>> items = service.showAllItemsOfSupplier(BN);
                        if (items.isError()) System.out.println(items.getError() + "\n");
                        else {
                            List<Item> responsesItem = items.getOutObject();
                            System.out.println("\tItemsID:");
                            for (Item item : responsesItem) {
                                System.out.println("\t\t" + item.toStringId());
                            }
                            Tresponse<List<Order>> orders = service.showAllOrdersOfSupplier(BN);
                            if (orders.isError()) System.out.println(items.getError() + "\n");
                            else {
                                List<Order> responsesOrders = orders.getOutObject();
                                System.out.println("\tOrdersId:");
                                for (Order order : responsesOrders) {
                                    System.out.println("\t\t" + order.toStringId());
                                }
                            }
                        }
                    }
                    toContinue(scanner);
                }
                case 2 -> {
                    String name = stringScan(scanner, "please enter supplier name");
                    Tresponse<SupplierCard> response = service.showSupplierBN(name);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("supplierBN is: " + response.getOutObject().toStringId());
                    toContinue(scanner);
                }
                case 3 -> {
                    Tresponse<List<SupplierCard>> responsesList = service.showAllSuppliers();
                    if (responsesList.isError()) System.out.println(responsesList.getError() + "\n");
                    else {
                        List<SupplierCard> responses = responsesList.getOutObject();
                        for (SupplierCard supplierCard : responses) {
                            System.out.println(supplierCard.toString());
                        }
                    }
                    toContinue(scanner);
                }
                case 4 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int itemId = intScan(scanner, "please enter itemId", "itemId must be a number");
                    Tresponse<Item> response = service.showItemOfSupplier(BN, itemId);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println(response.getOutObject().toString(false));
                    toContinue(scanner);
                }
                case 5 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    Tresponse<List<Item>> responsesList = service.showAllItemsOfSupplier(BN);
                    if (responsesList.isError()) System.out.println(responsesList.getError() + "\n");
                    else {
                        List<Item> responses = responsesList.getOutObject();
                        for (Item item : responses) {
                            System.out.println(item.toString(false));
                        }
                    }
                    toContinue(scanner);
                }
                case 6 -> {
                    Tresponse<List<Item>> responsesList = service.showAllItems();
                    if (responsesList.isError()) System.out.println(responsesList.getError() + "\n");
                    else {
                        List<Item> responses = responsesList.getOutObject();
                        for (Item item : responses) {
                            System.out.println(item.toString(false));
                        }
                    }
                    toContinue(scanner);
                }
                case 7 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int itemId = intScan(scanner, "please enter itemId", "itemId must be a number");
                    Tresponse<QuantityDocument> response = service.showQuantityDocument(BN, itemId);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println(response.getOutObject().toString());
                    toContinue(scanner);
                }
                case 8 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    Tresponse<SupplierAgreement> response = service.showSupplierAgreement(BN);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println(response.getOutObject().toString());
                    toContinue(scanner);
                }
                case 9 -> {
                    return;
                }
                case 10 -> {
                    System.exit(0);
                }
                default -> {
                    System.out.println("illegal option!!!");
                    toContinue(scanner);
                }
            }
        }
    }


    private void suppliersAddingMethods() {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        int option = -1;
        String[] showingMethodArray = {"add supplier", "add Contact Phone", "add Contact Email", "add Item",
                 "add Quantity Document", "add Supplier Agreement", "back to the main menu", "END PROGRAM"};
        System.out.println("please select the showing method: ");
        while (true) {
            for (int i = 1; i <= showingMethodArray.length; i++) {
                System.out.println(i + ") " + showingMethodArray[i - 1]);
            }
            option = menuCheck(scanner);
            int BN;
            switch (option) {
                case 1 -> {
                    String name = stringScan(scanner, "please enter supplier name");
                    int bankNumber = intScan(scanner, "please enter supplier bank number", "bank number must be a number");
                    int branchNumber = intScan(scanner, "please enter supplier branch number", "branch number must be a number");
                    int bankAccount = intScan(scanner, "please enter supplier bank account", "bank account must be a number");
                    String payWay = stringScan(scanner, "please enter supplier payWay");
                    if(payWay.equals("banktransfer"))payWay="bank transfer";
                    response response = service.addSupplier(name, bankNumber, branchNumber, bankAccount, payWay);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 2 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    String phone = stringScan(scanner, "please enter supplier contact phone");
                    String name = stringScan(scanner, "please enter supplier contact name");
                    response response = service.addContactPhone(BN, phone, name);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 3 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    String email = stringScan(scanner, "please enter supplier contact email");
                    String name = stringScan(scanner, "please enter supplier contact name");
                    response response = service.addContactEmail(BN, email, name);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 4 -> {
                    //int sotreId = intScan(scanner, "please enter the store id", "store id must be a number");
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    String name = stringScan(scanner, "please enter item name");
                    double basePrice = doubleScan(scanner, "please enter item base price", "base price must be a number");
                    double salePrice = doubleScan(scanner, "please enter item sale price", "sale price must be a number");
                    int min = intScan(scanner, "please enter minimal amount of item before report of missing", "minimal amount must must be a number");
                    String preducer = stringScan(scanner, "please enter the name of the item producer");
                    int category = intScan(scanner, "please enter item category", "category must must be a number");
                    LocalDate expirationDate = dateScan(scanner, "expiration date of the item");
                    double weight = doubleScan(scanner, "please enter item weight", "weight must be a number");
                    Tresponse<Item> response = service.addItem(BN, name, basePrice, salePrice, min, preducer, category, expirationDate, weight);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("ItemId is: " + response.getOutObject().toStringId() + "\n");
                    toContinue(scanner);
                }
                case 5 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int itemId = intScan(scanner, "please enter itemId", "itemId must be a number");
                    int minimalAmount = intScan(scanner, "please enter the minimal amount", "minimal amount must be a number");
                    int discount = intScan(scanner, "please enter the discount", "discount must be a number");
                    response response = service.addQuantityDocument(BN, itemId, minimalAmount, discount);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 6 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int minimalAmount = intScan(scanner, "please enter the minimal amount", "minimal amount must be a number");
                    int discount = intScan(scanner, "please enter the discount", "discount must be a number");
                    boolean constantTime = booleanScan(scanner, "please enter true for constant time , and false otherwise", "you must enter true/false");
                    boolean shipToUs = booleanScan(scanner, "please enter true for ship to us , and false otherwise", "you must enter true/false");
                    response response = service.addSupplierAgreement(BN, minimalAmount, discount, constantTime, shipToUs);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 7 -> {
                    return;
                }
                case 8 -> {
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

    private void suppliersRemovingMethods() {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        int option = -1;
        String[] removeMethodArray = {"remove Supplier", "remove Contact Phone", "remove Contact Email", "remove Item",
                "remove Quantity Document", "back to the main menu", "END PROGRAM"};
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
                    response response = service.removeSupplier(BN);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 2 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    String phone = stringScan(scanner, "please enter supplier contact phone");
                    response response = service.removeContactPhone(BN, phone);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 3 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    String email = stringScan(scanner, "please enter supplier contact email");
                    response response = service.removeContactEmail(BN, email);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 4 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int itemId = intScan(scanner, "please enter itemId", "itemId must be a number");
                    response response = service.removeItem(BN, itemId);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 5 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int itemId = intScan(scanner, "please enter itemId", "itemId must be a number");
                    response response = service.removeQuantityDocument(BN, itemId);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 6 -> {
                    return;
                }
                case 7 -> {
                    System.exit(0);
                }
                default -> {
                    System.out.println("illegal option!!!");
                    toContinue(scanner);
                }
            }
        }
    }


    private void suppliersUpdatingMethods() {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        int option = -1;
        int BN;
        String[] updateMethodArray = {"update Supplier PayWay", "update Supplier BankAccount", "update Contact Phone", "update Contact Email",
                 "update Minimal Amount Of Quantity Document", "update Discount Of Quantity Document",
                "update Minimal Amount Of Supplier Agreement", "update Discount Of Supplier Agreement",
                "update Constant Time", "update Ship To Us", "update Price", "back to the main menu", "END PROGRAM"};
        System.out.println("please select the showing method: ");
        while (true) {
            for (int i = 1; i <= updateMethodArray.length; i++) {
                System.out.println(i + ") " + updateMethodArray[i - 1]);
            }
            option = menuCheck(scanner);
            switch (option) {
                case 1 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    String payWay = stringScan(scanner, "please enter supplier payWay");
                    if(payWay.equals("banktransfer"))payWay="bank transfer";
                    response response = service.updateSupplierPayWay(BN, payWay);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 2 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int bankNumber = intScan(scanner, "please enter supplier bank number", "bank number must be a number");
                    int branchNumber = intScan(scanner, "please enter supplier branch number", "branch number must be a number");
                    int bankAccount = intScan(scanner, "please enter supplier bank account", "bank account must be a number");
                    response response = service.updateSupplierBankAccount(BN, bankNumber, branchNumber, bankAccount);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 3 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    String phone = stringScan(scanner, "please enter supplier contact phone");
                    String name = stringScan(scanner, "please enter supplier contact name");
                    response response = service.updateContactPhone(BN, phone, name);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 4 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    String email = stringScan(scanner, "please enter supplier contact email");
                    String name = stringScan(scanner, "please enter supplier contact name");
                    response response = service.updateContactEmail(BN, email, name);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 5 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int itemId = intScan(scanner, "please enter itemId", "itemId must be a number");
                    int minimalAmount = intScan(scanner, "please enter the minimal amount", "minimal amount must be a number");
                    response response = service.updateMinimalAmountOfQD(BN, itemId, minimalAmount);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 6 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int itemId = intScan(scanner, "please enter itemId", "itemId must be a number");
                    int discount = intScan(scanner, "please enter the discount", "discount must be a number");
                    response response = service.updateDiscountOfQD(BN, itemId, discount);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 7 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int minimalAmount = intScan(scanner, "please enter the minimal amount", "minimal amount must be a number");
                    response response = service.updateMinimalAmountOfSA(BN, minimalAmount);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 8 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int discount = intScan(scanner, "please enter the discount", "discount must be a number");
                    response response = service.updateDiscountOfSA(BN, discount);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 9 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    boolean constantTime = booleanScan(scanner, "please enter true for constant time and false otherwise", "you must enter true/false");
                    response response = service.updateConstantTime(BN, constantTime);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 10 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    boolean shipToUs = booleanScan(scanner, "please enter true for ship to us , and false otherwise", "you must enter true/false");
                    response response = service.updateShipToUs(BN, shipToUs);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 11 -> {
                    BN = intScan(scanner, "please enter supplier BN", "BN must be a number");
                    int itemId = intScan(scanner, "please enter itemId", "itemId must be a number");
                    double price = doubleScan(scanner, "please enter item price", "price must be a number");
                    response response = service.updatePrice(BN, itemId, price);
                    if (response.isError()) System.out.println(response.getError() + "\n");
                    else System.out.println("The operation was completed successfully\n");
                }
                case 12 -> {
                    return;
                }
                case 13 -> {
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

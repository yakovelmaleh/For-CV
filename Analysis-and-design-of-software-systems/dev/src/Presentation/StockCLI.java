package Presentation;

import Business.StockBusiness.Fcade.StorageService;
import Business.SupplierBusiness.facade.SupplierService;
import Business.SupplierBusiness.facade.response;
import Presentation.Menu.Menu;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class StockCLI extends Menu {
    StorageService SS;
    Scanner scan = new Scanner(System.in);
    final static Logger log = Logger.getLogger(StockCLI.class);

    public String read() {
        return scan.nextLine().toLowerCase().replaceAll("\\s", "");
    }

    @Override
    public void show() {
        store();
    }

    private void printM(String[] menu) {
        for (int i = 1; i <= menu.length; i++) {
            System.out.println(i + ") " + menu[i - 1]);
        }
    }

//    public StockCLI() {
//        SS = new StorageService();
//    }
//
//    public StockCLI(StorageService ss) {
//        SS = ss;
//    }


    public StockCLI(Controllers r, Scanner input){
        super(r, input);
        SS=r.getSt();
    }

    public StorageService getService() {
        return SS;
    }
    public void setStockService(SupplierService service){
        this.SS.setStockService(service);
    }

//    public void setup() {
//        String in;
//        while (true) {
//            System.out.print("Welcome to SuperLi storage management setup.\n" +
//                    "To start with an empty system enter 'empty'\n to start with a pre-generated example enter 'pre'" +
//                    "\n'exit' to exit.\n");
//
//            in = read();
//            if (in.equals("exit")) return;
//            if (in.equals("empty")) start();
//            if (in.equals("pre")) {
//                try {
//                    StorageService.init(SS);
//                } catch (Exception e) {
//                    System.out.println("problem with initialization. system will start empty instead.");
//                }
//                start();
//            } else System.out.print("bad input, try again.\n");
//        }
//    }

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
//
//    private void useStore() {
//        String in;
//        System.out.print("\nWelcome to SuperLi storage store selection.\nPlease enter the store you want to use.\n");
//        in = read();
//        try {
//            int i = Integer.parseInt(in);
//            SS.useStore(i);
//            store();
//        } catch (Exception e) {
//            System.out.print("no such store exists, try again.\n");
//        }
//    }

    private void store() {
        String in;
        String[] menu = {"exit", "return", "access categories", "access product types", "access specific products", "access discounts"};
        while (true) {
            System.out.print("\nWelcome to store number: " + SS.getCurrID() + "\n");
            printM(menu);
            in = read();
            if (in.equals("1")) System.exit(0);
            if (in.equals("2")) return;
            //if (in.equals("3")) reports();
            if (in.equals("3")) categories();
            else if (in.equals("4")) types();
            else if (in.equals("5")) products();
            else if (in.equals("6")) discounts();
            else System.out.print("bad input, try again.\n");
        }
    }


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

    ///TBD
    private void categories() {
        String in;
        String[] menu = {"exit", "return", "list all categories", "get more information about a specific category",
                "add a new category", "edit an existing category"};
        while (true) {
            System.out.print("\nWelcome to category management for store number: " + SS.getCurrID() + "\n");
            printM(menu);
            in = read();
            if (in.equals("1")) System.exit(0);
            if (in.equals("2")) return;
            else if (in.equals("3")) System.out.print(SS.getCategories().getOutObject());
            else if (in.equals("4")) catInfo();
            else if (in.equals("5")) addCat();
            else if (in.equals("6")) editCat();
            else {
                try {
                    String[] tmp = in.split(",");
                    if (tmp.length == 2) {
                        if (tmp[0].equals("info")) System.out.print(SS.getCategoryInfo(Integer.parseInt(tmp[1])).getOutObject());
                        else System.out.print("bad input, try again.\n");
                    } else System.out.print("bad input, try again.\n");
                } catch (Exception e) {
                    System.out.print("bad input, try again.\n");
                }
            }
        }
    }

    private void catInfo() {
        String in;
        System.out.print("\nPlease enter the requested category ID.\n");
        in = read();
        try {
            System.out.print(SS.getCategoryInfo(Integer.parseInt(in)).getOutObject());
        } catch (Exception e) {
            System.out.print("bad input, try again.\n");
        }
    }

    private void addCat() {
        String in;
        System.out.print("\nTo add a category enter the category name then its parent category ID separated by comma" +
                "\nIf there is no parent category then just enter the category name.\n");
        in = read();
        String[] tmp = in.split(",");
        if (tmp.length == 1) {
            if ((SS.addCategory(tmp[0]).isError())) {
                System.out.print("bad input, try again.\n");
            } else {
                System.out.print("Category added\n");
            }
        } else if (tmp.length == 2) {
            try {
                if ((SS.addCategory(tmp[0], Integer.parseInt(tmp[1])).isError())) {
                    System.out.print("bad input, try again.\n");
                } else {
                    System.out.print("Category added\n");
                }
            } catch (Exception e) {
                System.out.print("bad input, try again.\n");
            }
        } else System.out.print("bad input, try again.\n");
    }

    private void editCat() {
        String in;
        System.out.print("\nTo edit a category enter the category ID then a new name then its new parent category ID separated by comma" +
                "\nIf there is no parent category then just enter the category ID then a new name separated by comma\n");
        in = read();
        String[] tmp = in.split(",");
        if (tmp.length == 2) {
            try {
                if (SS.editCategory(Integer.parseInt(tmp[0]), tmp[1]).isError()) {
                    System.out.print("bad input, try again.\n");
                } else {
                    System.out.print("Category changed\n");
                }
            } catch (Exception e) {
                System.out.print("bad input, try again.\n");
            }
        } else if (tmp.length == 3) {
            try {
                if (SS.editCategory(Integer.parseInt(tmp[0]), tmp[1], Integer.parseInt(tmp[2])).isError()) {
                    System.out.print("bad input, try again.\n");
                } else {
                    System.out.print("Category changed\n");
                }
            } catch (Exception e) {
                System.out.print("bad input, try again.\n");
            }
        } else System.out.print("bad input, try again.\n");
    }

    private void types() {
        String in;
        String[] menu = {"exit", "return", "list all types",
                "get more information about a specific type",
                "get the amount in the store of a specific type", "get the amount in storage of a specific type"};
        while (true) {
            System.out.print("\nWelcome to product type management for store number: " + SS.getCurrID() + "\n");
            printM(menu);
            in = read();
            if (in.equals("1")) System.exit(0);
            if (in.equals("2")) return;
            else if (in.equals("3")) System.out.print(SS.getProductTypes().getOutObject());
            //else if (in.equals("4")) addType();
            //else if (in.equals("5")) editType();
            else if (in.equals("4")) typeInfo(1);
            else if (in.equals("5")) typeInfo(2);
            else if (in.equals("6")) typeInfo(3);
            else System.out.print("bad input, try again.\n");

        }
    }

    private void typeInfo(int i) {
        String in;
        System.out.print("enter the type ID\n");
        in = read();
        try {
            if (i == 1) System.out.print(SS.getProductTypeInfo(Integer.parseInt(in)).getOutObject());
            else if (i == 2) System.out.print(SS.getShelvesAmount(Integer.parseInt(in)).getOutObject());
            else if (i == 3) System.out.print(SS.getStorageAmount(Integer.parseInt(in)).getOutObject());
            else if (i == 4) System.out.print(SS.getProductsByType(Integer.parseInt(in)).getOutObject());
            else if (i == 5) System.out.print(SS.getSaleDiscounts(Integer.parseInt(in)).getOutObject());
            else if (i == 6) System.out.print(SS.getSupplierDiscounts(Integer.parseInt(in)).getOutObject());
            else System.out.print("bad input, try again.\n");
        } catch (Exception e) {
            System.out.print("bad input, try again.\n");
        }
    }

    private void addType() {
        String in;
        System.out.print("\nTo add a type enter the type name then its minimum amount, its base price, sale price, name of producer, ID of supplier" +
                " and the ID of its parent category separated by comma\n");
        in = read();
        String[] tmp = in.split(",");
        if (tmp.length == 7) {
            if ((SS.addProductType(tmp[0], Integer.parseInt(tmp[1]), Float.parseFloat(tmp[2]), Float.parseFloat(tmp[3]), tmp[4], Integer.parseInt(tmp[5]), Integer.parseInt(tmp[6])).isError())) {
                System.out.print("bad input, try again.\n");
            } else {
                System.out.print("Type added\n");
            }
        } else System.out.print("bad input, try again.\n");
    }

    private void editType() {
        String in;
        System.out.print("\nTo edit a type enter the type ID then its type name ,minimum amount, its base price, sale price, name of producer, ID of supplier" +
                " and the ID of its parent category separated by comma" +
                "\nTo return to category management enter 'return'\n");
        in = read();
        String[] tmp = in.split(",");
        if (tmp.length == 8) {
            if ((SS.editProductType(Integer.parseInt(tmp[0]), tmp[1], Integer.parseInt(tmp[2]), Float.parseFloat(tmp[3]), Float.parseFloat(tmp[4]), tmp[5], Integer.parseInt(tmp[6]), Integer.parseInt(tmp[7])).isError())) {
                System.out.print("bad input, try again.\n");
            } else {
                System.out.print("Type changed\n");
            }
        } else System.out.print("bad input, try again.\n");
    }


    private void products() {
        String in;
        String[] menu = {"exit", "return", "list all types", "list products for specific type",
                "get more information about a specific product", "add a new product",
                "relocate a product", "remove a product", "report damage on a products"};
        while (true) {
            System.out.print("\nWelcome to product management for store number: " + SS.getCurrID() + "\n");
            printM(menu);
            in = read();
            if (in.equals("1")) System.exit(0);
            if (in.equals("2")) return;
            else if (in.equals("3")) System.out.println(SS.getProductTypes().getOutObject());
            else if (in.equals("4")) productInfo(1);
            else if (in.equals("5")) productInfo(4);
            else if (in.equals("6")) addProd();
            else if (in.equals("7")) relocateProd();
            else if (in.equals("8")) productInfo(2);
            else if (in.equals("9")) productInfo(3);
            else System.out.print("bad input, try again.\n");
        }
    }

    private void productInfo(int i) {
        String in;
        System.out.print("enter the product ID\n");
        in = read();
        try {
            if (i == 1){
                System.out.print("enter the Type ID\n");
                System.out.print(SS.getProductsByType(Integer.parseInt(in)).getOutObject());}
            else if (i == 2) {
                System.out.print("enter the product ID\n");
                response r= SS.removeProduct(Integer.parseInt(in));
                if (!r.isError())
                    System.out.print("product removed.\n");
                else
                    System.out.println(r.getError());
            }
            else if (i == 3) {
                System.out.print("enter the product ID\n");
                response r= SS.reportDamage(Integer.parseInt(in));
                if (!r.isError())
                    System.out.print("damage reported.\n");
                else
                    System.out.println(r.getError());
            }
            else if (i == 4) {
                System.out.print("enter the Type ID\n");
                System.out.println(SS.getProductInfo(Integer.parseInt(in)));
            } else System.out.print("bad input, try again.\n");
        } catch (Exception e) {
            System.out.print("bad input, try again.\n");
        }
    }

    private void addProd() {
        String in;
        System.out.print("\nTo add a product enter the product ID then its expiration date (in DD-MM-YYYY format) separated by comma\n");
        in = read();
        if (in.equals("exit")) System.exit(0);
        if (in.equals("return")) return;
        String[] tmp = in.split(",");
        if (tmp.length == 2) {
            try {
                if ((SS.addProduct(Integer.parseInt(tmp[0]), new SimpleDateFormat("dd-MM-yyyy").parse(tmp[1])).isError())) {
                    System.out.print("bad input, try again.\n");
                } else {
                    System.out.print("product added\n");
                }
            } catch (Exception e) {
                System.out.print("bad input, try again.\n");
            }
        } else System.out.print("bad input, try again.\n");
    }


    private void relocateProd() {
        String in;
        System.out.print("\nTo relocate a product enter the product ID then its target location (storage or store) then the new shelf number separated by comma\n");
        in = read();
        if (in.equals("exit")) System.exit(0);
        if (in.equals("return")) return;
        String[] tmp = in.split(",");
        if (tmp.length == 3) {
            try {
                boolean b;
                if (tmp[1].equals("storage")) b = true;
                else if (tmp[1].equals("store")) b = false;
                else throw new Exception("bad input\n");
                if ((SS.relocateProduct(Integer.parseInt(tmp[0]), b, Integer.parseInt(tmp[2])).isError())) {
                    System.out.print("bad input, try again.\n");
                } else {
                    System.out.print("product relocated\n");
                }
            } catch (Exception e) {
                System.out.print("bad input, try again.\n");
            }
        } else System.out.print("bad input, try again.\n");
    }

    private void discounts() {
        String in;
        String[] menu = {"exit", "return", "access supplier discounts", "access sale discounts"};
        while (true) {
            System.out.print("\nWelcome to discount management for store number: " + SS.getCurrID() + "\n");
            printM(menu);
            in = read();
            if (in.equals("1")) System.exit(0);
            if (in.equals("2")) return;
            else if (in.equals("3")) supplier();
            else if (in.equals("4")) sale();
            else System.out.print("bad input, try again.\n");
        }

    }

    private void sale() {
        String in;
        String[] menu = {"exit", "return", "get sale discounts for a specific type", "add a new sale discount"};
        while (true) {
            System.out.print("\nWelcome to sale discount management for store number: " + SS.getCurrID() + "\n");
            printM(menu);
            in = read();
            if (in.equals("1")) System.exit(0);
            if (in.equals("2")) return;
            else if (in.equals("3")) typeInfo(5);
            else if (in.equals("4")) addSaleDiscount();
            else System.out.print("bad input, try again.\n");
        }
    }

    private void addSaleDiscount() {
        String in;
        System.out.print("\nTo add a sale discount for a specific type enter 'type' then the type ID then its discount percentage," +
                " then the start and the end date (in DD-MM-YYYY format) separated by comma" +
                "\nTo add a sale discount for a whole category enter 'category' then the category ID then its discount percentage," +
                " then the start and the end date (in DD-MM-YYYY format) separated by comma\n");
        in = read();
        String[] tmp = in.split(",");
        if (tmp.length == 5) {
            try {
                if (tmp[0].equals("type")) {
                    if ((SS.addSaleProductDiscount(Integer.parseInt(tmp[1]), Float.parseFloat(tmp[2]),
                            new SimpleDateFormat("dd-MM-yyyy").parse(tmp[3]), new SimpleDateFormat("dd-MM-yyyy").parse(tmp[4])).isError())) {
                        System.out.print("bad input, try again.\n");
                    } else {
                        System.out.print("sale discount added.\n");
                    }
                }
                else if (tmp[0].equals("category")) {
                    if ((SS.addSaleCategoryDiscount(Integer.parseInt(tmp[1]), Float.parseFloat(tmp[2]),
                            new SimpleDateFormat("dd-MM-yyyy").parse(tmp[3]), new SimpleDateFormat("dd-MM-yyyy").parse(tmp[4])).isError())) {
                        System.out.print("bad input, try again.\n");
                    } else {
                        System.out.print("sale discount added.\n");
                    }
                } else System.out.print("bad input, try again.\n");
            } catch (Exception e) {
                System.out.print("bad input, try again.\n");
            }
        } else System.out.print("bad input, try again.\n");
    }


    private void supplier() {
        String in;
        String[] menu = {"exit", "return", "get supplier discounts for a specific type", "add a new supplier discount"};
        while (true) {
            System.out.print("\nWelcome to supplier discount management for store number: " + SS.getCurrID() + "\n");
            printM(menu);
            in = read();
            if (in.equals("1")) System.exit(0);
            if (in.equals("2")) return;
            else if (in.equals("3")) typeInfo(6);
            else if (in.equals("4")) addSupplierDiscount();
            else System.out.print("bad input, try again.\n");
        }
    }

    private void addSupplierDiscount() {
        String in;
        System.out.print("\nTo add a supplier discount for a specific type enter type ID then its discount percentage," +
                " then the start and the end date (in DD-MM-YYYY format) and then the supplier ID separated by comma\n");
        in = read();
        String[] tmp = in.split(",");
        if (tmp.length == 5) {
            try {
                if ((SS.addSupplierDiscount(Integer.parseInt(tmp[0]), Float.parseFloat(tmp[1]),
                        new SimpleDateFormat("dd-MM-yyyy").parse(tmp[2]), new SimpleDateFormat("dd-MM-yyyy").parse(tmp[3]), Integer.parseInt(tmp[4])).isError())) {
                    System.out.print("bad input, try again.\n");
                } else {
                    System.out.print("product added\n");
                }
            } catch (Exception e) {
                System.out.print("bad input, try again.\n");
            }
        } else System.out.print("bad input, try again.\n");
    }

}

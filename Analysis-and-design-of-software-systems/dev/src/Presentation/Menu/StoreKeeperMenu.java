package Presentation.Menu;


import Business.ApplicationFacade.outObjects.TransportationServiceDTO;
import Business.SupplierBusiness.facade.outObjects.Order;
import Presentation.Controllers;
import Presentation.StockCLI;
import Utility.Tuple;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class StoreKeeperMenu extends Menu{
    public StoreKeeperMenu(Controllers r, Scanner input) {
        super(r, input);
    }
    @Override
    public void show() {
        while (true) {
            System.out.println("\n\n************* Functions Menu *************");
            System.out.println("1) My details");
            System.out.println("2) My constraints operations");
            System.out.println("3) My shifts and constraints");
            System.out.println("4) Stock Menu");
            System.out.println("5) Orders from suppliers");
            System.out.println("6) Suppliers card managements");
            System.out.println("7) Accept incoming orders");
            System.out.println("8) Cancel delivery");
            System.out.println("9) Logout");
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
                    new StockCLI(r,input).show();
                    break;
                case "5":
                    new OrdersMenu(r,input).show();
                    break;
                case "6":
                    new SuppliersMenu(r,input).show();
                    break;
                case "7":
                    List<TransportationServiceDTO> listOfTran=new ArrayList<>();
                    List<TransportationServiceDTO> trans =  r.getTc().getTransportations(r.getCurrBID(), LocalDate.now(), LocalTime.now());
                    for (TransportationServiceDTO t : trans) {
                        boolean checkTran=false;
                        for(Order o: t.getOrders().values().stream().toList())
                        {
                            if (!o.getIsArrived() && o.getBranchId()==r.getCurrBID()) {
                                checkTran = true;
                                break;
                            }
                        }
                        if (checkTran)
                            listOfTran.add(t);
                    }
                    if(listOfTran.isEmpty()) {
                        System.out.println("No transportations available for this shift");
                        break;
                    }
                    for (TransportationServiceDTO t: listOfTran)
                        System.out.println(t.toString());
                    TransportationServiceDTO acceptT = getAcceptID(listOfTran);
                    if(acceptT == null) break; //go back to main menu
                        List<Tuple<Integer,Dictionary<Integer,Integer>>>  failOrders= r.getSt().acceptTrans(acceptT).stream().filter(x->x.item2.size()>0).collect(Collectors.toList());
                        if (failOrders.size()>0){
                            System.out.println(String.format("Transportation %d contains %d fail ordres for #%d branch:",acceptT.getId(),failOrders.size(), r.getCurrBID()));
                            for (Tuple<Integer,Dictionary<Integer,Integer>> fo:failOrders){
                                System.out.println(String.format("order #%d, contains %d fail ProductTypes:",fo.item1,fo.item2.size()));
                                for (Integer i: Collections.list(fo.item2.keys())) {
                                    System.out.println(String.format("ProductType #%d contains %d items that report damage", i, fo.item2.get(i)));
                                    System.out.println("Do you want to make new order of this productType for this Branch? 1 for yes/ 0 for no");
                                    option = read();
                                    if (option.equals("1"))
                                        r.getSc().addNeededOrder(i, fo.item2.get(i), r.getCurrBID());
                                }
                            }
                        }
                        break;
                case "8":
                    cancelDelivery();
                    break;
                case "9":
                    r.getRc().Logout();
                    return;
                default:
                    System.out.println("Invalid input,please choose a number again");
                    if (goBack()) return;
                    break;
            }
        }
    }

    private TransportationServiceDTO getAcceptID(List<TransportationServiceDTO> list) {
        while (true) {
            System.out.print("Enter transportation id to accept: ");
            long num = enterInt(read());
            if (num <= 0) {
                System.out.println("invalid id - negative number.");
                if (goBack()) return null;
                else
                    continue;
            }
            for (TransportationServiceDTO t : list){
                if(t.getId() == num) {
                    return t;
                }
            }
            System.out.println("invalid id - transportation id does no exits in list option");
            if (goBack()) return null;
        }
    }
}

package DataAccess.DalSuppliers;

import DataAccess.DALObject;
import DataAccess.DalController;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class DalSupplierController extends DALObject {
    private int controller;
    private int numOfOrders;
    private int numOfItems;
    private int branchID;
    final static Logger log=Logger.getLogger(DalSupplierController.class);

    public DalSupplierController() {
        super(null);
    }
    public DalSupplierController(Integer controller , Integer numOfOrders , Integer numOfItems, DalController dalController) {
        super(dalController);
        this.controller = controller;
        this.numOfOrders = numOfOrders;
        this.numOfItems = numOfItems;
    }

    @Override
    public String getCreate() {
        return "CREATE TABLE IF NOT EXISTS \"SupplierController\"(\n" +
                "\t\"controller\" INTEGER NOT NULL,\n" +
                "\t\"numOfOrders\" INTEGER NOT NULL,\n" +
                "\t\"numOfItems\" INTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"controller\")\n" +
                ");";
    }

    @Override
    public String getSelect() {
        return "Select * FROM SupplierController;";
    }

    @Override
    public String getDelete() {
        return "DELETE FROM SupplierController\n" +
                "WHERE controller= ?;";
    }

    @Override
    public String getUpdate() {
        return "UPDATE SupplierController\n" +
                "SET (?) = (?)\n"+
                "WHERE controller = ?;";
    }

    @Override
    public String getInsert() {
        return "INSERT OR REPLACE INTO SupplierController\n"+
                "VALUES (?,?,?);";
    }


    public int getNumOfOrders() {
        try {
            String query = "SELECT numOfOrders FROM SupplierController\n" +
                    "WHERE controller = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(controller);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            numOfOrders = (Integer) tuple.item2.get(0);
        }
        catch (Exception e){
            log.warn(e);
        }
        return numOfOrders;
    }

    public int getNumOfItems() {
        try {
            String query = "SELECT numOfItems FROM SupplierController\n" +
                    "WHERE controller = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(controller);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            numOfItems = (Integer) tuple.item2.get(0);
        }
        catch (Exception e){
            log.warn(e);
        }
        return numOfItems;
    }

    public int getBranchID() {
        try {
            String query = "SELECT branchID FROM SupplierController\n" +
                    "WHERE controller = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(controller);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            branchID = (Integer) tuple.item2.get(0);
        }
        catch (Exception e){
            log.warn(e);
        }
        return branchID;
    }

    public void addNumOfOrders() throws Exception {
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE SupplierController\n" +
                "SET numOfOrders = ?\n" +
                "WHERE controller = ?;";
        list.add(new Tuple<>(numOfOrders+1, Integer.class));
        list.add(new Tuple<>(controller, Integer.class));
        DC.noSelect(query, list);
        numOfOrders++;
    }

    public void addNumOfItems() throws Exception {
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE SupplierController\n" +
                "SET numOfItems = ?\n" +
                "WHERE controller = ?;";
        list.add(new Tuple<>(numOfItems+1, Integer.class));
        list.add(new Tuple<>(controller, Integer.class));
        DC.noSelect(query, list);
        numOfItems++;
    }

    public List<Tuple<List<Class>,List<Object>>> loadSuppliers() {
        try {
            String query = "SELECT * FROM Suppliers;";
            LinkedList<Integer> list = new LinkedList<>();
            List<Tuple<List<Class>,List<Object>>> tuple = DC.SelectMany(query, list);
            return tuple;
        }
        catch (Exception e){
            log.warn(e);
        }
        return null;
    }

    public List<Tuple<List<Class>, List<Object>>> getOrderByTransportation(int transportationID) {
        try {
            String query = "SELECT * FROM Orders\n" +
                    "WHERE transportationID = ?";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(transportationID);
            List<Tuple<List<Class>,List<Object>>> tuple = DC.SelectMany(query, list);
            return tuple;
        }
        catch (Exception e){
            log.warn(e);
        }
        return null;
    }

}

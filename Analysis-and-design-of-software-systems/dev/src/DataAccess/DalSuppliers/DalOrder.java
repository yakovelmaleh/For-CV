package DataAccess.DalSuppliers;


import DataAccess.DALObject;
import DataAccess.DalController;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class DalOrder extends DALObject {
    private int orderId;
    private int supplierBN;
    private double totalAmount;
    private int branchId;
    private int orderType;
    private double totalWeight;
    private int transportationID;
    private int isArrived;
    private boolean removed = false;
    final static Logger log=Logger.getLogger(DalOrder.class);

    public DalOrder() {
        super(null);
    }

    public DalOrder(Integer orderId , Integer supplierBN , Double totalAmount , Integer branchId ,
                    Integer orderType , Double totalWeight, Integer transportationID, Integer isArrived, DalController dalController ){
        super(dalController);
        this.orderId = orderId;
        this.supplierBN = supplierBN;
        this.totalAmount = totalAmount;
        this.branchId = branchId;
        this.orderType = orderType;
        this.totalAmount = totalAmount;
        this.transportationID = transportationID;
        this.isArrived = isArrived;
    }

    @Override
    public String getCreate() {
        return "CREATE TABLE IF NOT EXISTS \"Orders\"(\n" +
                "\t\"orderId\" INTEGER NOT NULL,\n" +
                "\t\"supplierBN\" INTEGER NOT NULL,\n" +
                "\t\"totalAmount\" DOUBLE NOT NULL,\n" +
                "\t\"branchId\" INTEGER NOT NULL,\n" +
                "\t\"orderType\" INTEGER NOT NULL,\n" +
                "\t\"totalWeight\" DOUBLE NOT NULL,\n" +
                "\t\"transportationID\" INTEGER NOT NULL,\n" +
                "\t\"isArrived\" INTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(orderId),\n" +
                "\tFOREIGN KEY(\"supplierBN\") REFERENCES \"Suppliers\"(\"supplierBN\") ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");" +
                "CREATE TABLE IF NOT EXISTS \"ItemsInOrders\"(\n" +
                "\t\"orderId\" INTEGER NOT NULL,\n" +
                "\t\"itemId\" INTEGER NOT NULL,\n" +
                "\t\"amount\" INTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"itemId\", \"orderId\"),\n" +
                "\tFOREIGN KEY(\"orderId\") REFERENCES \"Orders\"(\"orderId\")  ON DELETE CASCADE ON UPDATE CASCADE ,\n" +
                "\tFOREIGN KEY(\"itemId\") REFERENCES \"Items\"(\"itemId\")  ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");";
    }

    @Override
    public String getSelect() {
        return "Select * FROM Orders\n" +
                "WHERE orderId = ?;";
    }

    @Override
    public String getDelete() {
        return "DELETE FROM Orders\n" +
                "WHERE orderId = ?;";
    }

    @Override
    public String getUpdate() {
        return "UPDATE Orders\n" +
                "SET (?) = (?)\n"+
                "WHERE orderId = ?;";
    }

    @Override
    public String getInsert() {
        return "INSERT OR REPLACE INTO Orders\n"+
                "VALUES (?,?,?,?,?,?,?,?);";
    }

    public int getOrderID() {
        return orderId;
    }

    public double getTotalAmount() {
        try {
            String query = "SELECT totalAmount FROM Orders\n" +
                    "WHERE orderId = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(orderId);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            totalAmount = (Double) tuple.item2.get(0);
        }
        catch (Exception e){
            log.warn(e);
        }
        return totalAmount;
    }

    public int getSupplierBN() {
        try {
            String query = "SELECT supplierBN FROM Orders\n" +
                    "WHERE orderId = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(orderId);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            supplierBN = (Integer) tuple.item2.get(0);
        }
        catch (Exception e){
            log.warn(e);
        }
        return supplierBN;
    }

    public int getTransportationID() {
        try {
            String query = "SELECT transportationID FROM Orders\n" +
                    "WHERE orderId = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(orderId);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            transportationID = (Integer) tuple.item2.get(0);
        }
        catch (Exception e){
            log.warn(e);
        }
        return transportationID;
    }

    public int getIsArrived() {
        try {
            String query = "SELECT isArrived FROM Orders\n" +
                    "WHERE orderId = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(orderId);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            isArrived = (Integer) tuple.item2.get(0);
        }
        catch (Exception e){
            log.warn(e);
        }
        return isArrived;
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

    public double getTotalWeight() {
        try {
            String query = "SELECT totalWeight FROM Orders\n" +
                    "WHERE orderId = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(orderId);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            totalWeight = (Double) tuple.item2.get(0);
        }
        catch (Exception e){
            log.warn(e);
        }
        return totalWeight;
    }

    public int getBranchID() {
        try {
            String query = "SELECT branchId FROM Orders\n" +
                    "WHERE orderId = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(orderId);
            Tuple<List<Class>, List<Object>> tuple = DC.Select(query, list);
            branchId = (Integer) tuple.item2.get(0);
        }
        catch (Exception e) {
            log.warn(e);
        }
        return branchId;
    }

    public int getOrderType() {
        try {
            String query = "SELECT orderType FROM Orders\n" +
                    "WHERE orderId = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(orderId);
            Tuple<List<Class>, List<Object>> tuple = DC.Select(query, list);
            orderType = (Integer) tuple.item2.get(0);
        }
        catch (Exception e) {
            log.warn(e);
        }
        return orderType;
    }

    public void updateTotalAmount(double totalAmount){
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE Orders\n" +
                "SET totalAmount = ?\n"+
                "WHERE orderId = ?;";
        list.add(new Tuple<>(totalAmount, Double.class));
        list.add(new Tuple<>(orderId, Integer.class));
        try {
            DC.noSelect(query, list);
        } catch (Exception e) {
            log.warn(e);
        }
        this.totalAmount = totalAmount;
    }

    public void updateBranchId(int branchId){
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE Orders\n" +
                "SET branchId = ?\n"+
                "WHERE orderId = ?;";
        list.add(new Tuple<>(branchId, Integer.class));
        list.add(new Tuple<>(orderId, Integer.class));
        try {
            DC.noSelect(query, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.branchId = branchId;
    }

    public void updateTransportation(int transportationID){
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE Orders\n" +
                "SET transportationID = ?\n"+
                "WHERE orderId = ?;";
        list.add(new Tuple<>(transportationID, Integer.class));
        list.add(new Tuple<>(orderId, Integer.class));
        try {
            DC.noSelect(query, list);
        } catch (Exception e) {
            log.warn(e);
        }
        this.transportationID = transportationID;
    }

    public void updateArrived(){
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE Orders\n" +
                "SET isArrived = ?\n"+
                "WHERE orderId = ?;";
        list.add(new Tuple<>(1, Integer.class));
        list.add(new Tuple<>(orderId, Integer.class));
        try {
            DC.noSelect(query, list);
        } catch (Exception e) {
            log.warn(e);
        }
        this.isArrived = 1;
    }

    public void removeOrder() {
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "DELETE FROM Orders\n" +
                "WHERE orderId = ?;";
        list.add(new Tuple<>(orderId, Integer.class));
        try {
            DC.noSelect(query, list);
        } catch (Exception e) {
            log.warn(e);
        }
        removed = true;
    }


    public void removeItemFromOrder(int itemId) {
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "DELETE FROM ItemsInOrders\n" +
                "WHERE itemId = ?;";
        list.add(new Tuple<>(itemId, Integer.class));
        try {
            DC.noSelect(query, list);
        } catch (Exception e) {
            log.warn(e);
        }
    }

    public void addItemToOrder(int itemId, int amount) throws Exception {
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "INSERT INTO ItemsInOrders\n" +
                "VALUES (?,?,?);";
        list.add(new Tuple<>(orderId, Integer.class));
        list.add(new Tuple<>(itemId, Integer.class));
        list.add(new Tuple<>(amount, Integer.class));
        try {
            DC.noSelect(query, list);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<Tuple<List<Class>, List<Object>>> loadItems() {
        try {
            String query = "SELECT * FROM ItemsInOrders\n" +
                    "WHERE orderId = ?";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(orderId);
            List<Tuple<List<Class>,List<Object>>> tuple = DC.SelectMany(query, list);
            return tuple;
        }
        catch (Exception e){
            log.warn(e);
        }
        return null;
    }

    public void updateTotalWeight(double weight) {
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE Orders\n" +
                "SET totalWeight = ?\n"+
                "WHERE orderId = ?;";
        list.add(new Tuple<>(weight, Double.class));
        list.add(new Tuple<>(orderId, Integer.class));
        try {
            DC.noSelect(query, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.totalWeight = weight;
    }

    public void removeOrdersByTransportationId(int tranID) {
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "DELETE FROM Orders\n" +
                "WHERE transportationID = ?;";
        list.add(new Tuple<>(tranID, Integer.class));
        try {
            DC.noSelect(query, list);
        } catch (Exception e) {
            log.warn(e);
        }
    }

    public void updateAmountOfOrder(int itemId, int newAmount) {
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE ItemsInOrders\n" +
                "SET amount = ?\n"+
                "WHERE orderId = ? AND ItemId = ?;";
        list.add(new Tuple<>(newAmount, Integer.class));
        list.add(new Tuple<>(orderId, Integer.class));
        list.add(new Tuple<>(itemId, Integer.class));
        try {
            DC.noSelect(query, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getIsRemoved() {
        return removed;
    }
}

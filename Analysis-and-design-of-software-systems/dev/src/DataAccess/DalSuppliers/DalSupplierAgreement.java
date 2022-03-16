package DataAccess.DalSuppliers;

import DataAccess.DALObject;
import DataAccess.DalController;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class DalSupplierAgreement extends DALObject {
    private int supplierBN;
    private int minimalAmount;
    private int discount;
    private boolean constantTime;
    private boolean shipToUs;
    final static Logger log=Logger.getLogger(DalSupplierAgreement.class);

    public DalSupplierAgreement() {
        super(null);
    }

    public DalSupplierAgreement(Integer supplierBN , Integer minimalAmount , Integer discount , Integer constantTime , Integer shipToUs , DalController dalController){
        super(dalController);
        this.supplierBN = supplierBN;
        this.minimalAmount = minimalAmount;
        this.discount = discount;
        if(constantTime == 0) {
            this.constantTime = false;
        }
        else if (constantTime == 1) {
            this.constantTime = true;
        }
        if(shipToUs == 0) {
            this.shipToUs = false;
        }
        else if (shipToUs == 1) {
            this.shipToUs = true;
        }
    }

    @Override
    public String getCreate() {
        return "CREATE TABLE IF NOT EXISTS \"SupplierAgreements\"(\n" +
                "\t\"supplierBN\" INTEGER NOT NULL,\n" +
                "\t\"minimalAmount\" INTEGER NOT NULL,\n" +
                "\t\"discount\" INTEGER NOT NULL,\n" +
                "\t\"constantTime\" INTEGER NOT NULL,\n" +
                "\t\"shipToUs\" INTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"supplierBN\"),\n" +
                "\tFOREIGN KEY(\"supplierBN\") REFERENCES \"Suppliers\"(\"supplierBN\") ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");";
    }

    @Override
    public String getSelect() {
        return "Select * FROM SupplierAgreements\n" +
                "WHERE supplierBN = ?;";
    }

    @Override
    public String getDelete() {
        return "DELETE FROM SupplierAgreements\n" +
                "WHERE supplierBN = ?;";
    }

    @Override
    public String getUpdate() {
        return "UPDATE SupplierAgreements\n" +
                "SET (?) = (?)\n"+
                "WHERE SupplierBN = ?;";
    }

    @Override
    public String getInsert() {
        return "INSERT OR REPLACE INTO SupplierAgreements\n"+
                "VALUES (?,?,?,?,?);";
    }

    public int getMinimalAmount() {
        try {
            String query = "SELECT minimalAmount FROM SupplierAgreements\n" +
                    "WHERE supplierBN = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            Tuple<List<Class>, List<Object>> tuple = DC.Select(query, list);
            minimalAmount = (Integer) tuple.item2.get(0);
        }
        catch (Exception e) {
            log.warn(e);
        }
        return minimalAmount;
    }

    public int getDiscount() {
        try {
            String query = "SELECT discount FROM SupplierAgreements\n" +
                    "WHERE supplierBN = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            Tuple<List<Class>, List<Object>> tuple = DC.Select(query, list);
            discount = (Integer) tuple.item2.get(0);
        }
        catch (Exception e) {
            log.warn(e);
        }
        return discount;
    }

    public boolean getConstantTime() {
        try {
            String query = "SELECT constantTime FROM SupplierAgreements\n" +
                    "WHERE supplierBN = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            Tuple<List<Class>, List<Object>> tuple = DC.Select(query, list);
            if ((Integer) tuple.item2.get(0) == 0) {
                constantTime = false;
            }
            else {
                constantTime = true;
            }
        }
        catch (Exception e) {
            log.warn(e);
        }
        return constantTime;
    }

    public boolean getShipToUs() {
        try {
            String query = "SELECT shipToUs FROM SupplierAgreements\n" +
                    "WHERE supplierBN = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            Tuple<List<Class>, List<Object>> tuple = DC.Select(query, list);
            if ((Integer) tuple.item2.get(0) == 0) {
                shipToUs = false;
            }
            else {
                shipToUs = true;
            }
        }
        catch (Exception e) {
            log.warn(e);
        }
        return shipToUs;
    }

    public void updateMinimalAmountOfSA(int minimalAmount) throws Exception {
        this.minimalAmount = minimalAmount;
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE SupplierAgreements\n" +
                "SET minimalAmount = ?\n"+
                "WHERE supplierBN = ?;";
        list.add(new Tuple<>(minimalAmount, Integer.class));
        list.add(new Tuple<>(supplierBN, Integer.class));
        DC.noSelect(query, list);
    }

    public void updateDiscountOfSA(int discount) throws Exception {
        this.discount = discount;
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE SupplierAgreements\n" +
                "SET discount = ? \n"+
                "WHERE supplierBN = ?;";
        list.add(new Tuple<>(discount, Integer.class));
        list.add(new Tuple<>(supplierBN, Integer.class));
        DC.noSelect(query, list);
    }

    public void updateConstantTime(boolean constantTime) throws Exception {
        this.constantTime = constantTime;
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE SupplierAgreements\n" +
                "SET constantTime = ? \n"+
                "WHERE supplierBN = ?;";
        if(constantTime) list.add(new Tuple<>(1, Integer.class));
        else list.add(new Tuple<>(0, Integer.class));
        list.add(new Tuple<>(supplierBN, Integer.class));
        DC.noSelect(query, list);
    }

    public void updateShipToUs(boolean shipToUs) throws Exception {
        this.shipToUs = shipToUs;
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE SupplierAgreements\n" +
                "SET shipToUs = ? \n"+
                "WHERE supplierBN = ?;";
        if(shipToUs) list.add(new Tuple<>(1, Integer.class));
        else list.add(new Tuple<>(0, Integer.class));
        list.add(new Tuple<>(supplierBN, Integer.class));
        DC.noSelect(query, list);
    }
}

package DataAccess.DalSuppliers;

import DataAccess.DALObject;
import DataAccess.DalController;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class DalQuantityDocument extends DALObject {
    private int minimalAmount;
    private int discount;
    private int itemId;
    final static Logger log=Logger.getLogger(DalQuantityDocument.class);

    public DalQuantityDocument() {
        super(null);
    }

    public DalQuantityDocument(Integer itemId , Integer minimalAmount , Integer discount , DalController dalController){
        super(dalController);
        this.itemId = itemId;
        this.minimalAmount = minimalAmount;
        this.discount = discount;
    }

    @Override
    public String getCreate() {
        return "CREATE TABLE IF NOT EXISTS \"QuantityDocuments\"(\n"+
                "\"itemId\" INTEGER NOT NULL,\n" +
                "\t\"minimalAmount\" INTEGER NOT NULL,\n" +
                "\t\"discount\" INTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"itemId\"),\n" +
                "\tFOREIGN KEY(\"itemId\") REFERENCES \"Items\"(\"itemId\") ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");";
    }

    @Override
    public String getSelect() {
        return "Select * FROM QuantityDocuments\n" +
                "WHERE itemId = ?;";
    }

    @Override
    public String getDelete() {
       return "DELETE FROM QuantityDocuments\n" +
               "WHERE itemId = ?;";
    }

    @Override
    public String getUpdate() {
        return "UPDATE QuantityDocuments \n" +
                "SET (?) = (?) \n"+
                "WHERE itemId = ?;";
    }

    @Override
    public String getInsert() {
        return "INSERT OR REPLACE INTO QuantityDocuments\n"+
                "VALUES (?,?,?);";
    }

    public int getMinimalAmount(){
        try {
            String query = "SELECT minimalAmount FROM QuantityDocuments\n" +
                    "WHERE itemId = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(itemId);
            Tuple<List<Class>, List<Object>> tuple = DC.Select(query, list);
            minimalAmount = (Integer) tuple.item2.get(0);
        }
        catch (Exception e) {
            log.warn(e);
        }
        return minimalAmount;
    }

    public int getDiscount(){
        try {
            String query = "SELECT discount FROM QuantityDocuments\n" +
                    "WHERE itemId = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(itemId);
            Tuple<List<Class>, List<Object>> tuple = DC.Select(query, list);
            discount = (Integer) tuple.item2.get(0);
        }
        catch (Exception e) {
            log.warn(e);
        }
        return discount;
    }

    public void updateMinimalAmountOfQD(int minimalAmount) throws Exception {
        this.minimalAmount = minimalAmount;
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE QuantityDocuments \n" +
                    "SET minimalAmount = ? \n"+
                    "WHERE itemId = ?;";
        list.add(new Tuple<>(minimalAmount, Integer.class));
        list.add(new Tuple<>(itemId, Integer.class));
        DC.noSelect(query, list);
    }

    public void updateDiscountOfQD(int discount) throws Exception {
        this.discount = discount;
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE QuantityDocuments \n" +
                "SET discount = ? \n"+
                "WHERE itemId = ?;";
        list.add(new Tuple<>(discount, Integer.class));
        list.add(new Tuple<>(itemId, Integer.class));
        DC.noSelect(query, list);
    }

}

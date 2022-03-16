package DataAccess.DalSuppliers;

import DataAccess.DALObject;
import DataAccess.DalController;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

public class DalOrderController extends DALObject {
    private int branchID;
    private int numOfOrders;
    final static Logger log=Logger.getLogger(DalOrderController.class);

    public DalOrderController() {
        super(null);
    }

    public DalOrderController(Integer branchID, Integer numOfOrders, DalController dalController) {
        super(dalController);
        this.branchID = branchID;
        this.numOfOrders = numOfOrders;

    }

    @Override
    public String getCreate() {
        return "CREATE TABLE IF NOT EXISTS \"OrderController\"(\n" +
                "\t\"branchID\" INTEGER NOT NULL,\n" +
                "\t\"numOfOrders\" INTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"branchID\")\n" +
                ");";
    }

    @Override
    public String getSelect() {
        return "Select * FROM OrderController;";
    }

    @Override
    public String getDelete() {
        return "DELETE FROM OrderController\n" +
                "WHERE branchID= ?;";
    }

    @Override
    public String getUpdate() {
        return "UPDATE OrderController\n" +
                "SET (?) = (?)\n" +
                "WHERE branchID = ?;";
    }

    @Override
    public String getInsert() {
        return "INSERT OR REPLACE INTO OrderController\n" +
                "VALUES (?,?);";
    }


    public int getNumOfOrders() {
        try {
            String query = "SELECT numOfOrders FROM OrderController\n" +
                    "WHERE branchID = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(branchID);
            Tuple<List<Class>, List<Object>> tuple = DC.Select(query, list);
            numOfOrders = (Integer) tuple.item2.get(0);
        } catch (Exception e) {
            log.warn(e);
        }
        return numOfOrders;
    }

    public void addNumOfOrders() throws Exception {
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE OrderController\n" +
                "SET numOfOrders = ?\n" +
                "WHERE branchID = ?;";
        list.add(new Tuple<>(numOfOrders+1, Integer.class));
        list.add(new Tuple<>(branchID, Integer.class));
        DC.noSelect(query, list);
        numOfOrders++;
    }
}

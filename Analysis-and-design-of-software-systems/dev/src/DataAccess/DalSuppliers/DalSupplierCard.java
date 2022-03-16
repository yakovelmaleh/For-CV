package DataAccess.DalSuppliers;

import DataAccess.DALObject;
import DataAccess.DalController;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class DalSupplierCard extends DALObject {
    private int supplierBN;
    private String supplierName;
    private int bankNumber;
    private int branchNumber;
    private int accountNumber;
    private String payWay;
    private Dictionary<String , String> contactPhone;
    private Dictionary<String , String> contactEmail;
    final static Logger log=Logger.getLogger(DalSupplierCard.class);

    public DalSupplierCard() {
        super(null);
    }

    public DalSupplierCard(Integer supplierBN , String supplierName , String payWay , DalController dalController) {
        super(dalController);
        this.supplierBN = supplierBN;
        this.supplierName = supplierName;
        this.payWay = payWay;
        contactPhone = new Hashtable<>();
        contactEmail = new Hashtable<>();
    }

    @Override
    public String getCreate() {
        return "CREATE TABLE IF NOT EXISTS \"Suppliers\"(\n" +
                "\t\"supplierBN\" INTEGER NOT NULL,\n" +
                "\t\"supplierName\" VARCHAR NOT NULL,\n" +
                "\t\"payWay\" VARCHAR NOT NULL,\n" +
                "\tPRIMARY KEY(\"supplierBN\")\n" +
                ");\n" +
                "CREATE TABLE IF NOT EXISTS \"BankInfo\"(\n" +
                "\t\"supplierBN\" INTEGER NOT NULL,\n" +
                "\t\"bankNumber\" INTEGER NOT NULL,\n" +
                "\t\"branchNumber\" INTEGER NOT NULL,\n" +
                "\t\"bankAccount\" INTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"supplierBN\"),\n" +
                "\tFOREIGN KEY(\"supplierBN\") REFERENCES \"Suppliers\"(\"supplierBN\") ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS \"SupplierPhones\"(\n" +
                "\t\"supplierBN\" INTEGER NOT NULL,\n" +
                "\t\"phone\" TEXT NOT NULL,\n" +
                "\t\"name\" TEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"phone\"),\n" +
                "\tFOREIGN KEY(\"supplierBN\") REFERENCES \"Suppliers\"(\"supplierBN\") ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE IF NOT EXISTS \"SupplierEmails\"(\n" +
                "\t\"supplierBN\" INTEGER NOT NULL,\n" +
                "\t\"email\" TEXT NOT NULL,\n" +
                "\t\"name\" TEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"email\"),\n" +
                "\tFOREIGN KEY(\"supplierBN\") REFERENCES \"Suppliers\"(\"supplierBN\")  ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");";
    }

    @Override
    public String getSelect() {
        return "Select * FROM Suppliers\n" +
                "WHERE supplierBN = ?;";
    }

    @Override
    public String getDelete() {
        return "DELETE FROM Suppliers\n" +
                "WHERE supplierBN = ?;";
    }

    @Override
    public String getUpdate() {
        return "UPDATE (?)\n" +
                "SET (?) = (?)\n"+
                "WHERE SupplierBN = ?";
    }

    @Override
    public String getInsert() {
        return "INSERT OR REPLACE INTO Suppliers\n"+
                "VALUES (?,?,?);";
    }


    public int getSupplierBN() {
        try {
            String query = "SELECT supplierBN FROM Suppliers\n" +
                    "WHERE supplierBN = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            supplierBN = (Integer)tuple.item2.get(0);
        }
        catch (Exception e){
            log.warn(e);
        }
        return supplierBN;
    }

    public int getSupplierBankNumber() {
        try {
            String query = "SELECT bankNumber FROM BankInfo\n" +
                    "WHERE supplierBN = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            bankNumber = (Integer)tuple.item2.get(0);
        }
        catch (Exception e){
            log.warn(e);
        }
        return bankNumber;
    }

    public int getSupplierBranchNumber() {
        try {
            String query = "SELECT branchNumber FROM BankInfo\n" +
                    "WHERE supplierBN = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            branchNumber = (Integer)tuple.item2.get(0);
        }
        catch (Exception e){
            log.warn(e);
        }
        return branchNumber;
    }

    public int getSupplierAccountNumber() {
        try {
            String query = "SELECT bankAccount FROM BankInfo\n" +
                    "WHERE supplierBN = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            accountNumber = (Integer)tuple.item2.get(0);
        }
        catch (Exception e){
            log.warn(e);
        }
        return accountNumber;
    }

    public String getSupplierPayWay() {
        String paywayTemp = "";
        try {
            String query = "SELECT payWay FROM Suppliers\n" +
                    "WHERE supplierBN = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            paywayTemp = tuple.item2.get(0).toString();
        }
        catch (Exception e){
            log.warn(e);
        }
        payWay = paywayTemp;
        return payWay;
    }

    public Dictionary<String , String> getContactPhone() {
        try {
            String query = "SELECT * FROM SupplierPhones\n" +
                    "WHERE supplierBN = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            List<Tuple<List<Class>,List<Object>>> list1 = DC.SelectMany(query, list);
            if (list1 != null && list1.size() > 0) {
                for (int i = 1 ; i < list1.get(0).item2.size() ; i= i+4) {
                    contactPhone.put((String) list1.get(0).item2.get(i), (String) list1.get(0).item2.get(i + 1));
                }
            }
        }
        catch (Exception e){
            log.warn(e);
        }
        return contactPhone;
    }

    public Dictionary<String , String> getContactEmail() {
        try {
            String query = "SELECT * FROM SupplierEmails\n" +
                    "WHERE supplierBN = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            List<Tuple<List<Class>,List<Object>>> list1 = DC.SelectMany(query, list);
            if (list1 != null && list1.size() > 0) {
                for (int i = 1 ; i < list1.get(0).item2.size() ; i= i+4) {
                    contactEmail.put((String) list1.get(0).item2.get(i), (String) list1.get(0).item2.get(i+1));
                }
            }
        }
        catch (Exception e){
            log.warn(e);
        }
        return contactEmail;
    }

    public String getSupplierName() {
        String supplierNameTemp = "";
        try {
            String query = "SELECT supplierName FROM Suppliers\n" +
                    "WHERE supplierBN = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            Tuple<List<Class>,List<Object>> tuple = DC.Select(query, list);
            supplierNameTemp = tuple.item2.get(0).toString();
        }
        catch (Exception e){
            log.warn(e);
        }
        supplierName = supplierNameTemp;
        return supplierName;
    }

    public void updateSupplierPayWay(String payWay) throws Exception {
        this.payWay = payWay;
        String p = payWay;
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        String query = "UPDATE Suppliers\n" +
                "SET payWay = ?\n"+
                "WHERE supplierBN = ?";
        list.add(new Tuple<>(p, String.class));
        list.add(new Tuple<>(supplierBN, Integer.class));
        DC.noSelect(query, list);
    }

    public void addContactPhone(String phone, String name) throws Exception {
        contactPhone.put(phone, name);
        String query = "INSERT OR REPLACE INTO SupplierPhones (supplierBN, phone, name)\n" +
                "VALUES (?,?,?)";
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        list.add(new Tuple<>(supplierBN, Integer.class));
        list.add(new Tuple<>(phone, String.class));
        list.add(new Tuple<>(name, String.class));
        DC.noSelect(query, list);
    }

    public void updateSupplierBankAccount(int bankNumber , int branchNumber , int bankAccount) {
        String query = "INSERT OR REPLACE INTO BankInfo (supplierBN, bankNumber, branchNumber, bankAccount)\n" +
                "VALUES (?,?,?,?)";
        try {
            LinkedList<Tuple<Object, Class>> list = new LinkedList<>();
            list.add(new Tuple<>(supplierBN, Integer.class));
            list.add(new Tuple<>(bankNumber, Integer.class));
            list.add(new Tuple<>(branchNumber, Integer.class));
            list.add(new Tuple<>(bankAccount, Integer.class));
            DC.noSelect(query, list);
            this.bankNumber = bankNumber;
            this.branchNumber = branchNumber;
            this.accountNumber = bankAccount;
        }
        catch (Exception e ) {
            log.warn(e);
        }
    }

    public void addContactEmail(String email, String name) throws Exception {
        contactEmail.put(email, name);
        String query = "INSERT OR REPLACE INTO SupplierEmails (supplierBN, email, name)\n" +
                "VALUES (?,?,?)";
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        list.add(new Tuple<>(supplierBN, Integer.class));
        list.add(new Tuple<>(email, String.class));
        list.add(new Tuple<>(name, String.class));
        DC.noSelect(query, list);
    }

    public void removeContactPhone(String phone) throws Exception {
        contactPhone.remove(phone);
        String query = "DELETE From SupplierPhones\n" +
                "WHERE phone = ?";
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        list.add(new Tuple<>(phone, String.class));;
        DC.noSelect(query, list);
    }

    public void removeContactEmail(String email) throws Exception {
        contactEmail.remove(email);
        String query = "DELETE From SupplierEmails\n" +
                "WHERE email = ?";
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        list.add(new Tuple<>(email, String.class));;
        DC.noSelect(query, list);
    }

    public void removeSupplier() throws Exception {
        String query = "DELETE From Suppliers\n" +
                "WHERE supplierBN = ?";
        LinkedList<Tuple<Object,Class>> list = new LinkedList<>();
        list.add(new Tuple<>(supplierBN, Integer.class));
        DC.noSelect(query, list);
    }

    public void load(Integer key) {
        try {
            String query = "SELECT * FROM Suppliers;" +
                    "WHERE supplierBN = ?";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(key);
            DC.Select(query, list);
        }
        catch (Exception e){
            log.warn(e);
        }
    }

    public List<Tuple<List<Class>, List<Object>>> loadOrders() {
        try {
            String query = "SELECT * FROM Orders\n"+
                    "WHERE supplierBN = ?";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            List<Tuple<List<Class>,List<Object>>> tuple = DC.SelectMany(query, list);
            return tuple;
        }
        catch (Exception e){
            log.warn(e);
        }
        return null;
    }

    public List<Tuple<List<Class>, List<Object>>> loadOrdersByTransportation(int transportationID) {
        try {
            String query = "SELECT * FROM Orders\n"+
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

    public List<Tuple<List<Class>, List<Object>>> loadItems() {
        try {
            String query = "SELECT * FROM Items\n" +
                    "WHERE supplierBN = ?";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            List<Tuple<List<Class>,List<Object>>> tuple = DC.SelectMany(query, list);
            return tuple;
        }
        catch (Exception e){
            log.warn(e);
        }
        return null;
    }

    public Tuple<List<Class>, List<Object>> loadSupplierAgreement() {
        try {
            String query = "SELECT * FROM SupplierAgreements\n" +
                    "WHERE supplierBN = ?;";
            LinkedList<Integer> list = new LinkedList<>();
            list.add(supplierBN);
            Tuple<List<Class>, List<Object>> tuple = DC.Select(query, list);
            return tuple;
        }
        catch (Exception e) {
            log.warn(e);
        }
        return new Tuple<>(new LinkedList<>(), new LinkedList<>());
    }


}

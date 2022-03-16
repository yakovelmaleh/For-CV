package Business.SupplierBusiness;

import DataAccess.DALObject;
import DataAccess.DalSuppliers.DalOrder;
import DataAccess.DalSuppliers.DalSupplierCard;
import DataAccess.DalSuppliers.DalSupplierController;
import DataAccess.SMapper;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.rmi.server.ExportException;
import java.time.LocalDate;
import java.util.*;

public class SupplierController{
    private Dictionary<Integer , SupplierCard> suppliers;
    private DalSupplierController dalSupplierController;
    final static Logger log=Logger.getLogger(SupplierController.class);


    public SupplierController(){
        List<Tuple<Object,Class>> list=new ArrayList<>();
        list.add(new Tuple<>(0,Integer.class));
        list.add(new Tuple<>(1,Integer.class));
        list.add(new Tuple<>(1,Integer.class));
        SMapper map= SMapper.getMap();
        map.setItem(DalSupplierController.class,list);
        List<Integer> keyList=new ArrayList<>();
        DALObject check =map.getItem(DalSupplierController.class ,keyList);
        if (check==null ||(check.getClass()!=DalSupplierController.class)){
            String s="the instance that return from Mapper is null";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        else{
            log.info("create new Object");
            dalSupplierController = (DalSupplierController) check;
        }
        suppliers = new Hashtable<>();
    }

    public SupplierController(boolean load) {
        SMapper map= SMapper.getMap();
        List<Integer> keyList=new ArrayList<>();
        DALObject check =map.getItem(DalSupplierController.class ,keyList);
        if (check==null ||(check.getClass()!=DalSupplierController.class)){
            String s="the instance that return from Mapper is null";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        else{
            log.info("create new Object");
            dalSupplierController = (DalSupplierController) check;
            dalSupplierController.getNumOfOrders();
        }
        loadSuppliers();
    }

    private void loadSuppliers() {
        suppliers = new Hashtable<>();
        List<Tuple<List<Class>,List<Object>>> list1 = dalSupplierController.loadSuppliers();
        for (int i=0; i<list1.get(0).item2.size() ; i=i+4) {
            int key = (int)list1.get(0).item2.get(i);
            SMapper map= SMapper.getMap();
            List<Integer> keyList=new ArrayList<>();
            keyList.add(key);
            DALObject check =map.getItem(DalSupplierCard.class ,keyList);
            if (check==null ||(check.getClass()!=DalSupplierCard.class)){
                String s="the instance that return from Mapper is null";
                log.warn(s);
                throw new IllegalArgumentException(s);
            }
            else {
                log.info("loaded new Object");
                suppliers.put(key, new SupplierCard((DalSupplierCard) check));
            }
        }
    }

    public SupplierCard showSupplier(int supplierBN) throws Exception {
        SupplierCard supplierCard  = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        return supplierCard;
    }

    public void addSupplier(String supplierName, int bankNumber , int branchNumber, int bankAccount, String payWay) throws Exception {
        if(bankNumber < 0) throw new Exception("bank number must be a positive number");
        if(branchNumber < 0) throw new Exception("branch must be a positive number");
        if(bankAccount < 0) throw new Exception("bank account must be a positive number");
        Enumeration<Integer> enumeration = suppliers.keys();
        while(enumeration.hasMoreElements()){
            SupplierCard supplierCard = suppliers.get(enumeration.nextElement());
            if(supplierCard.getSupplierName().equals(supplierName)) throw new Exception("supplier name all ready exist");
            if(supplierCard.getSupplierBankNumber() == bankNumber && supplierCard.getSupplierBranchNumber() == branchNumber && supplierCard.getSupplierAccountNumber() == bankAccount)
                throw new Exception("the combination of bank number , branch number and bank account must be unique");
        }
        if(!(payWay.equals("check") || payWay.equals("bank transfer") || payWay.equals("cash")))
            throw new Exception("pay way must be check/bank transfer/cash.");
        Business.SupplierBusiness.SupplierCard supplierCard = new Business.SupplierBusiness.SupplierCard(suppliers.size() ,supplierName, bankNumber,branchNumber,bankAccount,payWay);
        suppliers.put(suppliers.size() , supplierCard);
    }

    public void removeSupplier(int removeSupplier) throws Exception {
        SupplierCard supplierCard = suppliers.remove(removeSupplier);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        else supplierCard.removeSupplier();
        /*List<Item> toRemoveItems = supplierCard.showAllItemsOfSupplier();
        for(Item item : toRemoveItems){
        for(Item item : toRemoveItems){
            removeItemFromSupplier(removeSupplier , item.getItemId());
        }
        List<Order> toRemoveOrders = supplierCard.showAllOrdersOfSupplier();
        for(Order order : toRemoveOrders){
            supplierCard.removeOrder(order.getOrderId());
        }*/
    }

    public SupplierCard showSupplierBN(String supplierName) throws Exception {
        Enumeration<Integer> enumeration = suppliers.keys();
        while(enumeration.hasMoreElements()) {
            int current = enumeration.nextElement();
            if (suppliers.get(current).getSupplierName().equals(supplierName)) {
                return suppliers.get(current);
            }
        }
        throw new Exception("supplier name is not exist");
    }

    public void updateSupplierPayWay(int supplierBN, String payWay) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        suppliers.get(supplierBN);
        try {
            suppliers.get(supplierBN).updateSupplierPayWay(payWay);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void updateSupplierBankAccount(int supplierBN, int bankNumber , int branchNumber ,int bankAccount) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).updateSupplierBankAccount(bankNumber , branchNumber , bankAccount);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void addContactPhone(int supplierBN, String phone, String name) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).addContactPhone(phone, name);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void addContactEmail(int supplierBN, String email, String name) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).addContactEmail(email, name);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void removeContactPhone(int supplierBN, String phone) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).removeContactPhone(phone);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void removeContactEmail(int supplierBN, String email) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).removeContactEmail(email);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void updateContactPhone(int supplierBN, String phone , String name) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).updateContactPhone(phone , name);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void updateContactEmail(int supplierBN, String email , String name) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).updateContactEmail(email , name);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<SupplierCard> showAllSuppliers() {
        List<SupplierCard> list = new LinkedList<>();
        Enumeration<SupplierCard> enumeration = suppliers.elements();
        while (enumeration.hasMoreElements()) {
            list.add(0 , enumeration.nextElement());
        }
        return list;
    }

    public int findSupplierByItemId(int itemId) throws Exception {
        try {
            List<SupplierCard> suppliers = showAllSuppliers();
            for(SupplierCard supplierCard : suppliers){
                List<Item> items = supplierCard.getSupplierItems();
                for(Item item : items){
                    if(item.getItemId() == itemId)
                        return supplierCard.getSupplierBN();
                }
            }
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        throw new Exception("there is no supplier that has this itemId");
    }

    public List<Item> showAllItemsOfSupplier(int supplierBN) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        return suppliers.get(supplierBN).showAllItemsOfSupplier();
    }

    public Hashtable<Item, Integer> showAllItemsOfOrder(int supplierBN , int orderId) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        return suppliers.get(supplierBN).showAllItemsOfOrder(orderId);
    }

    public List<Item> showAllItems() {
        List<Item> list = new LinkedList<>();
        Enumeration<SupplierCard> enumeration = suppliers.elements();
        while (enumeration.hasMoreElements()) {
            List<Item> toAdd = enumeration.nextElement().showAllItemsOfSupplier();
            list.addAll(0 , toAdd);
        }
        return list;
    }

    public Item showItemOfSupplier(int supplierBN , int itemId) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        Item item;
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            item = supplierCard.showItemOfSupplier(itemId);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return item;
    }



    public Item addItem(int supplierBN,String name , double price,LocalDate expirationDate, double weight) throws Exception {
        Item item;
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            item = suppliers.get(supplierBN).addItem(supplierBN, dalSupplierController.getNumOfItems() , name, price , expirationDate, weight);
            dalSupplierController.addNumOfItems();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return item;
    }

    public void removeItem(int supplierBN , int itemId) throws Exception {
        if(itemId < 0) throw new Exception("itemId does not exist.");
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            supplierCard.removeItem(itemId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void removeItemFromRegularOrder(int supplierBN, int orderId, int itemId) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            supplierCard.removeItemFromRegularOrder(orderId , itemId);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void removeAmountItemFromRegularOrder(int supplierBN, int orderId, int itemId, int amount) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            supplierCard.removeAmountItemFromRegularOrder(orderId , itemId , amount);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public Tuple<Order , Boolean> addRegularOrder(int supplierBN , int branchId, Hashtable<Integer, Integer> items) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        Tuple<Order , Boolean> tuple;
        try {
            tuple = suppliers.get(supplierBN).addRegularOrder(dalSupplierController.getNumOfOrders(), branchId, items);
            dalSupplierController.addNumOfOrders();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return tuple;
    }

    public Item getItem(int supplierBN , int itemId) throws Exception {
        try {
            SupplierCard supplierCard = suppliers.get(supplierBN);
            if(supplierCard == null) throw new Exception("supplier BN does not exist.");
            return supplierCard.getItem(itemId);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }


    public Tuple<Order , Boolean> addNeededOrder(int itemId, int neededAmount, int branchID) throws Exception {
        Order order;
        Tuple<Order , Boolean> tuple;
        Item item = null;
        double bestPrice = Integer.MAX_VALUE;
        int bestSupplier = 0;
        try {
            Enumeration<SupplierCard> enumeration = suppliers.elements();
            while(enumeration.hasMoreElements()) {
                SupplierCard temp = enumeration.nextElement();
                List<Item> items = temp.getSupplierItems();
                for (Item i : items) {
                    if (i.getItemId() == itemId) {
                        double currentPrice = i.getPrice()*neededAmount;
                        if (neededAmount >= i.getQuantityDocument().getMinimalAmount()) {
                            currentPrice = currentPrice * (100-i.getQuantityDocument().getDiscount()) /100;
                        }
                        if (currentPrice < bestPrice) {
                            bestSupplier = temp.getSupplierBN();
                        }
                        item = i;
                    }
                }
            }
            tuple = suppliers.get(bestSupplier).addNeededOrder(dalSupplierController.getNumOfOrders(), branchID, item, neededAmount);
            dalSupplierController.addNumOfOrders();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        return tuple;
    }

    public Tuple<Order , Boolean> addItemToOrder(int supplierBN, int orderId, int itemId , int amount) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try{
            Order order = supplierCard.showOrderOfSupplier(orderId);
            if(order.getOrderType() == 1) throw new Exception("you cannot add new items to needed order");
            return suppliers.get(supplierBN).addItemToOrder(orderId, itemId , amount);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void removeOrder(int supplierBN , int orderId) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try{
            suppliers.get(supplierBN).removeOrder(orderId);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public Order showOrderOfSupplier(int supplierBN, int orderId) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try{
            return suppliers.get(supplierBN).showOrderOfSupplier(orderId);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<Order> showAllOrdersOfSupplier(int supplierBN) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        return suppliers.get(supplierBN).showAllOrdersOfSupplier();
    }

    public Order showTotalAmount(int supplierBN, int orderId) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        return suppliers.get(supplierBN).showTotalAmount(orderId);
    }

    public Order showDeliverTime(int supplierBN, int orderId) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            return suppliers.get(supplierBN).showDeliverTime(orderId);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

//    public void updateDeliverTime(int supplierBN, int orderId, LocalDate deliverTime) throws Exception {
//        SupplierCard supplierCard = suppliers.get(supplierBN);
//        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
//        try {
//            suppliers.get(supplierBN).updateDeliverTime(orderId, deliverTime);
//        } catch (Exception e){
//            throw new Exception(e.getMessage());
//        }
//    }

    public void addQuantityDocument(int supplierBN, int itemId, int minimalAmount, int discount) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).addQuantityDocument(itemId, minimalAmount, discount);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void removeQuantityDocument(int supplierBN, int itemId) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).removeQuantityDocument(itemId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public QuantityDocument showQuantityDocument(int supplierBN, int itemId) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            return suppliers.get(supplierBN).showQuantityDocument(itemId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void updateMinimalAmountOfQD(int supplierBN, int itemId, int minimalAmount) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).updateMinimalAmountOfQD(itemId, minimalAmount);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void updateDiscountOfQD(int supplierBN, int itemId, int discount) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).updateDiscountOfQD(itemId,discount);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void addSupplierAgreement(int supplierBN, int minimalAmount, int discount, boolean constantTime, boolean shipToUs) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).addSupplierAgreement(supplierBN, minimalAmount, discount, constantTime, shipToUs);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public SupplierAgreement showSupplierAgreement(int supplierBN) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        return suppliers.get(supplierBN).showSupplierAgreement();
    }

    public void updateMinimalAmountOfSA(int supplierBN, int minimalAmount) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).updateMinimalAmountOfSA(minimalAmount);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void updateDiscountOfSA(int supplierBN, int discount) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).updateDiscountOfSA(discount);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void updateConstantTime(int supplierBN, boolean constantTime) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        suppliers.get(supplierBN).updateConstantTime(constantTime);
    }

    public void updateShipToUs(int supplierBN, boolean shipToUs) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        suppliers.get(supplierBN).updateShipToUs(shipToUs);
    }

    public void updatePrice(int supplierBN, int itemId, double price) throws Exception {
        SupplierCard supplierCard = suppliers.get(supplierBN);
        if(supplierCard == null) throw new Exception("supplier BN does not exist.");
        try {
            suppliers.get(supplierBN).updatePrice(itemId,price);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public List<Order> getOrdersByTransportation(int transportationID) {
        List <Order> retList = new LinkedList<>();
        List<Tuple<List<Class>, List<Object>>> orders = dalSupplierController.getOrderByTransportation(transportationID);
        if (orders.size() > 0) {
            for (int i = 0; i < orders.get(0).item2.size(); i = i + 9) {
                int key = (int) orders.get(0).item2.get(i);
                SMapper map = SMapper.getMap();
                List<Integer> keyList = new ArrayList<>();
                keyList.add(key);
                DALObject check = map.getItem(DalOrder.class, keyList);
                if (check == null || (check.getClass() != DalOrder.class)) {
                    String s = "the instance that return from Mapper is null";
                    log.warn(s);
                    throw new IllegalArgumentException(s);
                } else {
                    log.info("loaded new Object");
                    if (((DalOrder) check).getOrderType() == 0) {
                        retList.add(new regularOrder((DalOrder) check));
                    }
                    else {
                        retList.add(new neededOrder((DalOrder) check));
                    }
                }
            }
        }
        return retList;
    }


    public void removeOrdersByTransport(int transportationID) throws Exception {
        try {
            Enumeration<SupplierCard> suppliersEnum = this.suppliers.elements();
            while (suppliersEnum.hasMoreElements()) {
                suppliersEnum.nextElement().removeOrdersByTransportation(transportationID);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void updateArrived(int supplierBN , int orderId) throws Exception {
        try{
            SupplierCard supplierCard = suppliers.get(supplierBN);
            if(supplierCard == null) throw new Exception("supplier BN does not exist.");
            supplierCard.updateArrived(orderId);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public LocalDate getExpirationDate(int supplierBN , int itemId) throws ExportException {
        try{
            SupplierCard supplierCard = suppliers.get(supplierBN);
            if(supplierCard == null) throw new Exception("supplier BN does not exist.");
            return supplierCard.getExpirationDate(itemId);
        }catch (Exception e){
            throw new ExportException(e.getMessage());
        }
    }
}
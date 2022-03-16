package Business.SupplierBusiness;

import DataAccess.DALObject;
import DataAccess.DalSuppliers.*;
import DataAccess.SMapper;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.rmi.server.ExportException;
import java.time.LocalDate;
import java.util.*;


public class SupplierCard {
    private List<Order> orders;
    private List<Item> items;
    private SupplierAgreement supplierAgreement;
    private DalSupplierCard dalSupplierCard;
    final static Logger log=Logger.getLogger(SupplierCard.class);

    public SupplierCard(int supplierBN , String supplierName ,int bankNumber , int branchNumber, int accountNumber , String payWay){
        List<Tuple<Object,Class>> list=new ArrayList<>();
        list.add(new Tuple<>(supplierBN,Integer.class));
        list.add(new Tuple<>(supplierName,String.class));
        list.add(new Tuple<>(payWay,String.class));
        SMapper map= SMapper.getMap();
        map.setItem(DalSupplierCard.class,list);
        List<Integer> keyList=new ArrayList<>();
        keyList.add(supplierBN);
        DALObject check =map.getItem(DalSupplierCard.class ,keyList);
        if (check==null ||(check.getClass()!=DalSupplierCard.class)){
            String s="the instance that return from Mapper is null";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        else{
            log.info("create new Object");
            dalSupplierCard = (DalSupplierCard) check;
        }
        items = new LinkedList<>();
        orders = new LinkedList<>();
        dalSupplierCard.updateSupplierBankAccount(bankNumber, branchNumber, accountNumber);
    }

    public SupplierCard(DalSupplierCard dalSupplierCard) {
        this.dalSupplierCard = dalSupplierCard;
        loadItems();
        loadOrders();
        loadSupplierAgreement();
    }

    private void loadSupplierAgreement() {
        Tuple<List<Class>,List<Object>> tuple = dalSupplierCard.loadSupplierAgreement();
        if (tuple != null &&  tuple.item2 != null && tuple.item2.size() > 0) {
            int key = (int) tuple.item2.get(0);
            SMapper map = SMapper.getMap();
            List<Integer> keyList = new ArrayList<>();
            keyList.add(key);
            DALObject check = map.getItem(DalSupplierAgreement.class, keyList);
            if (DalSupplierAgreement.class == null || check == null || (check.getClass() != DalSupplierAgreement.class)) {
                String s = "the instance that return from Mapper is null";
                log.warn(s);
                throw new IllegalArgumentException(s);
            } else {
                log.info("loaded new Object");
                supplierAgreement = new SupplierAgreement((DalSupplierAgreement) check);
            }
        }
        else {
            supplierAgreement = null;
        }
    }

    private void loadOrders() {
        orders = new LinkedList<>();
        List<Tuple<List<Class>,List<Object>>> list1 = dalSupplierCard.loadOrders();
        if (list1.size() > 0) {
            for (int i = 0; i < list1.get(0).item2.size(); i = i + 9) {
                int key = (int) list1.get(0).item2.get(i);
                SMapper map = SMapper.getMap();
                List<Integer> keyList = new ArrayList<>();
                keyList.add(key);
                DALObject check = map.getItem(DalOrder.class, keyList);
                if (DalOrder.class == null || check == null || (check.getClass() != DalOrder.class)) {
                    String s = "the instance that return from Mapper is null";
                    log.warn(s);
                    throw new IllegalArgumentException(s);
                } else {
                    log.info("loaded new Object");
                    if (((DalOrder) check).getOrderType() == 0) {
                        orders.add(new regularOrder((DalOrder) check));
                    }
                    else {
                        orders.add(new neededOrder((DalOrder) check));
                    }
                }
            }
        }
    }

    private void loadItems() {
        items = new LinkedList<>();
        List<Tuple<List<Class>,List<Object>>> list1 = dalSupplierCard.loadItems();
        if (list1.size() > 0) {
            for (int i = 0; i < list1.get(0).item2.size(); i = i + 7) {
                int key = (int) list1.get(0).item2.get(i);
                SMapper map = SMapper.getMap();
                List<Integer> keyList = new ArrayList<>();
                keyList.add(key);
                DALObject check = map.getItem(DalItem.class, keyList);
                if (DalItem.class == null || check == null || (check.getClass() != DalItem.class)) {
                    String s = "the instance that return from Mapper is null";
                    log.warn(s);
                    throw new IllegalArgumentException(s);
                } else {
                    log.info("loaded new Object");
                    items.add(new Item((DalItem) check));
                }
            }
        }
    }


    public void removeSupplier() throws Exception {
        dalSupplierCard.removeSupplier();
        orders = new LinkedList<>();
        items = new LinkedList<>();
    }

    public int getSupplierBN() {
        return dalSupplierCard.getSupplierBN();
    }

    public int getSupplierBankNumber() {
        return dalSupplierCard.getSupplierBankNumber();
    }

    public int getSupplierBranchNumber() {
        return dalSupplierCard.getSupplierBranchNumber();
    }

    public int getSupplierAccountNumber() {
        return dalSupplierCard.getSupplierAccountNumber();
    }

    public String getSupplierPayWay() {
        return dalSupplierCard.getSupplierPayWay();
    }

    public Dictionary<String , String> getContactPhone() {
        return dalSupplierCard.getContactPhone();
    }

    public Dictionary<String , String> getContactEmail() {
        return dalSupplierCard.getContactEmail();
    }

    public String getSupplierName() {
        return dalSupplierCard.getSupplierName();
    }

    public void updateSupplierPayWay(String payWay) throws Exception {
        if(!(payWay.equals("check") || payWay.equals("bank transfer") || payWay.equals("cash")))
            throw new Exception("pay way must be check/bank transfer/cash.");
        dalSupplierCard.updateSupplierPayWay(payWay);
    }

    public void updateSupplierBankAccount(int bankNumber , int branchNumber , int bankAccount) throws Exception {
        if(bankAccount < 0) throw new Exception("bank account must be a positive number");
        dalSupplierCard.updateSupplierBankAccount(bankNumber, branchNumber, bankAccount);
    }

    public void addContactPhone(String phone, String name) throws Exception {
        try {
            if (dalSupplierCard.getContactPhone().get(phone) != null)
                throw new Exception("contact phone all ready exist, you may want to use: update contact phone");
        }
        catch (Exception e) {
        }
        dalSupplierCard.addContactPhone(phone, name);
    }

    public void addContactEmail(String email, String name) throws Exception {
        try {
            if (dalSupplierCard.getContactEmail().get(email) != null)
                throw new Exception("contact email all ready exist, you may want to use: update contact email");
        } catch (Exception e) {
        }
        dalSupplierCard.addContactEmail(email, name);
    }

    public void removeContactPhone(String phone) throws Exception {
        if(dalSupplierCard.getContactPhone().get(phone) == null)
            throw new Exception("contact phone does not exist");
        dalSupplierCard.removeContactPhone(phone);
    }

    public void removeContactEmail(String email) throws Exception {
        if(dalSupplierCard.getContactEmail().get(email) == null)
            throw new Exception("contact email does not exist");
        dalSupplierCard.removeContactEmail(email);
    }



    public void updateContactPhone(String phone , String name) throws Exception {
        Enumeration<String> e1 = dalSupplierCard.getContactPhone().elements();
        while (e1.hasMoreElements()) {
            String element = e1.nextElement();
            if(name.equals(element)){
                Enumeration<String> e2 = dalSupplierCard.getContactPhone().keys();
                while (e2.hasMoreElements()) {
                    String oldPhone = e2.nextElement();
                    if (dalSupplierCard.getContactPhone().get(oldPhone).equals(name)) {
                        removeContactPhone(oldPhone);
                        break;
                    }
                }
            }
        }
        addContactPhone(phone, name);
    }

    public void updateContactEmail(String email , String name) throws Exception {
        Enumeration<String> e1 = dalSupplierCard.getContactEmail().elements();
        while (e1.hasMoreElements()) {
            String element = e1.nextElement();
            if(name.equals(element)){
                Enumeration<String> e2 = dalSupplierCard.getContactEmail().keys();
                while (e2.hasMoreElements()) {
                    String oldEmail = e2.nextElement();
                    if (dalSupplierCard.getContactEmail().get(oldEmail).equals(name)) {
                        removeContactEmail(oldEmail);
                        break;
                    }
                }
            }
        }
        addContactEmail(email, name);
    }


    public List<Item> showAllItemsOfSupplier() {
        return items;
    }

    public Item showItemOfSupplier(int itemId) throws Exception {
        for(Item item : items){
            if(item.getItemId() == itemId) return item;
        }
        throw new Exception("itemId does net exist for this supplier");
    }

    public Item addItem(int supplierBN, int ItemId , String name , double price , LocalDate expirationDate, double weight) throws Exception {
        if(price < 0) throw new Exception("price must be a positive number!");
        if(expirationDate.isBefore(LocalDate.now())) throw new IllegalAccessException("expiration date must be in the future");
        Item newItem = new Business.SupplierBusiness.Item(supplierBN, ItemId , name , price , expirationDate, weight);
        items.add(newItem);
        return newItem;
    }

    public Hashtable<Item, Integer> showAllItemsOfOrder(int orderId) throws Exception {
        for(Order order : orders){
            if(order.getOrderId() == orderId) return order.showAllItemsOfOrder();
        }
        throw new Exception("orderId does not exist.");
    }

    public void removeItem(int itemId) throws Exception { /////////I think it need to be modified
        List<Item> copyItem = items;
        boolean found = false;
        for(Item item : copyItem){
            if(item.getItemId() == itemId){
                items.remove(item);
                item.removeItem();
                found = true;
                break;
            }
        }
        if(!found) throw new Exception("itemId does not exist for this supplier");
    }

    public void removeItemFromRegularOrder(int orderId, int itemId) throws Exception {
        for(Order order : orders){
            if(order.getOrderId() == orderId){
                try {
                    if(order.getOrderType() == 1) throw new Exception("you can remove items only from regular order");
                    regularOrder regularOrder = (regularOrder) order;
                    regularOrder.removeItemFromRegularOrder(itemId);
                    break;
                }catch (Exception e){
                    throw new Exception(e.getMessage());
                }
            }
        }
    }

    public void removeAmountItemFromRegularOrder(int orderId, int itemId, int amount) throws Exception {
        for(Order order : orders){
            if(order.getOrderId() == orderId){
                try {
                    if(order.getOrderType() == 1) throw new Exception("you can remove items only from regular order");
                    regularOrder regularOrder = (Business.SupplierBusiness.regularOrder) order;
                    regularOrder.removeAmountItemFromRegularOrder(itemId , amount);
                    break;
                }catch (Exception e){
                    throw new Exception(e.getMessage());
                }
            }
        }
    }

    public Tuple<Order , Boolean> addRegularOrder(int orderId , int branchId, Hashtable<Integer, Integer> items) throws Exception {
        regularOrder order;
        order = new regularOrder(dalSupplierCard.getSupplierBN(), orderId , branchId);
        orders.add(order);
        try {
            for (Integer itemID : items.keySet()) {
                addItemToOrder(orderId, itemID, items.get(itemID));
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return new Tuple<>(order , supplierAgreement.getShipToUs());
    }

    public Item getItem(int itemId) throws Exception {
        for(Item item : items){
            if(item.getItemId() == itemId)
                return item;
        }
        throw new Exception("there is no such itemId for this Supplier");
    }

    public Tuple<Order , Boolean> addNeededOrder(int orderID, int branchID, Item item, int amount) throws Exception {
        if (item == null || isItemExist(item.getItemId()) == null) return null;
        neededOrder order;
        double totalAmount = calculateTotalAmount(item , amount);
        try {
             order = new neededOrder(dalSupplierCard.getSupplierBN(), orderID ,LocalDate.now().plusDays(1), branchID, item, amount , totalAmount);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
        orders.add(order);
        return new Tuple<>(order , supplierAgreement.getShipToUs());
    }

    public double calculateTotalAmount(Item item , int amount){
        double totalAmount = 0.0;
        QuantityDocument qd = item.getQuantityDocument();
        if (qd != null) {
            totalAmount = totalAmount + item.getPrice() * amount;
            if (qd.getMinimalAmount() <= amount) {
                double discount = qd.getDiscount() / 100.0;
                totalAmount = totalAmount - item.getPrice() * discount * amount;
            }
        }
        return totalAmount;
    }

    private Item isItemExist(int itemId){
        for (Item i : items) {
            if (i.getItemId() == itemId) {
                return i;
            }
        }
        return null;
    }

    public Tuple<Order , Boolean> addItemToOrder(int orderId, int itemId , int amount) throws Exception {
        Item toAdd = isItemExist(itemId);
        if(toAdd == null) throw new Exception("the supplier does not have this item");
        for (Order o : orders) {
            if (o.getOrderId() == orderId) {
                if(o.getOrderType() == 1) throw new Exception("you can add more items to an existing order only for regular order");
                regularOrder temp = (regularOrder) o;
                temp.addItemToOrder(toAdd , amount);
                return new Tuple<>(o, supplierAgreement.getShipToUs());
            }
        }
        throw new Exception("orderId does not exist");
    }


    public void removeOrder(int orderId) throws Exception {
        List<Order> copyOrders = orders;
        boolean found = false;
        for(Order order : copyOrders){
            if(order.getOrderId() == orderId){
                if (order.getIsArrived()==1) {
                    throw new Exception("order already arrived to store - can't remove!");
                }
                order.removeOrder();
                orders.remove(order);
                found = true;
                break;
            }
        }
        if(!found) throw new Exception("orderId does not exist");
    }

    public Order showOrderOfSupplier(int orderId) throws Exception {
        for (Order o : orders) {
            if (o.getOrderId() == orderId)
                if (!o.isRemoved())
                    return o;
        }
        throw new Exception("orderId does not exist.");
    }

    public List<Order> showAllOrdersOfSupplier() {
        orders.removeIf(Order::isRemoved);
        return orders;
    }

    public Order showTotalAmount(int orderId) throws Exception {
        double totalAmount;
        boolean found = false;
        Order order = null;
        for(Order o : orders) {
            if (o.getOrderId() == orderId) {
                found = true;
                try {
                    totalAmount = o.getTotalAmount();
                } catch (Exception e) {
                    throw new Exception(e);
                }
                order = o;
                if(supplierAgreement.getMinimalAmount() <= order.getTotalAmount()){
                    double discount = 1 - supplierAgreement.getDiscount()/100.0;
                    totalAmount = totalAmount*discount;
                    regularOrder temp = (regularOrder) order;
                    temp.updateTotalAmount(totalAmount);
                }
            }
        }
        if(found) return order;
        throw new Exception("orderId does not exist.");
    }

    public Order showDeliverTime(int orderId) throws Exception {
        for (Order o : orders) {
            if (o.getOrderId() == orderId)
                try {
                    return o.showDeliverTime();
                } catch (Exception e){
                    throw new Exception(e);
                }
        }
        throw new Exception("orderId does not exist.");
    }

//    public void updateDeliverTime(int orderId, LocalDate deliverTime) throws Exception {
//        boolean hasFound = false;
//        for (Order o : orders) {
//            if (o.getOrderId() == orderId) {
//                if(o.getOrderType() == 1) throw new Exception("you can update deliver time only for regular order");
//                regularOrder temp = (regularOrder) o;
//                temp.updateDeliverTime(deliverTime);
//                hasFound = true;
//            }
//            if(hasFound) break;
//        }
//        if(!hasFound) throw new Exception("orderId does not exist.");
//    }

    public void addQuantityDocument(int itemId, int minimalAmount, int discount) throws Exception {
        boolean hasFound = false;
        for (Item i : items) {
            if (i.getItemId() == itemId) {
                try {
                    i.addQuantityDocument(minimalAmount, discount);
                    hasFound = true;
                } catch (Exception e){
                    throw new Exception(e);
                }
           }
            if(hasFound) break;
        }
        if(!hasFound) throw new Exception("itemId does not exist.");
    }

    public void removeQuantityDocument(int itemId) throws Exception {
        boolean hasFound = false;
        for (Item i : items) {
            if (i.getItemId() == itemId) {
                try {
                    i.removeQuantityDocument();
                    hasFound = true;
                }catch (Exception e){
                    throw new Exception(e);
                }
            }
            if(hasFound) break;
        }
        if(!hasFound) throw new Exception("itemId does not exist for this supplier");
    }

    public QuantityDocument showQuantityDocument(int itemId) throws Exception {
        for (Item i : items) {
            if (i.getItemId() == itemId) {
                try {
                    return i.showQuantityDocument();
                } catch (Exception e){
                    throw new Exception(e);
                }
            }
        }
        throw new Exception("itemId does not exist.");
    }

    public void updateMinimalAmountOfQD(int itemId, int minimalAmount) throws Exception {
        boolean hasFound = false;
        for (Item i : items) {
            if (i.getItemId() == itemId) {
                try {
                    i.updateMinimalAmountOfQD(minimalAmount);
                    hasFound = true;
                }catch (Exception e){
                    throw new Exception(e);
                }
            }
            if(hasFound) break;
        }
        if(!hasFound) throw new Exception("itemId does not found");
    }

    public void updateDiscountOfQD(int itemId, int discount) throws Exception {
        boolean hasFound = false;
        for (Item item : items) {
            if (item.getItemId() == itemId) {
                try{
                    item.updateDiscountOfQD(discount);
                    hasFound = true;
                } catch (Exception e){
                    throw new Exception(e);
                }
            }
            if(hasFound) break;
        }
        if(!hasFound) throw new Exception("itemId does not exist");
    }

    public void addSupplierAgreement(int supplierBN, int minimalAmount, int discount, boolean constantTime, boolean shipToUs) throws Exception {
        if(minimalAmount < 0 ) throw new Exception("minimal amount must be a positive number");
        if(discount < 0 || discount > 100) throw new Exception("minimal amount must be a positive number between 0 to 100");
        supplierAgreement = new SupplierAgreement(supplierBN, minimalAmount, discount, constantTime, shipToUs);
    }

    public SupplierAgreement showSupplierAgreement() {
        return supplierAgreement;
    }

    public void updateMinimalAmountOfSA(int minimalAmount) throws Exception {
        try {
            supplierAgreement.updateMinimalAmountOfSA(minimalAmount);
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    public void updateDiscountOfSA(int discount) throws Exception {
        try {
            supplierAgreement.updateDiscountOfSA(discount);
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    public void updateConstantTime(boolean constantTime) throws Exception { supplierAgreement.updateConstantTime(constantTime); }

    public void updateShipToUs(boolean shipToUs) throws Exception {
        supplierAgreement.updateShipToUs(shipToUs);
    }

    public void updatePrice(int itemId , double price) throws Exception {
        boolean hasFound = false;
        for(Item item : items){
            if(item.getItemId() == itemId){
                try {
                    item.updatePrice(price);
                    hasFound = true;
                }catch (Exception e){
                    throw new Exception(e);
                }
            }
            if(hasFound) break;
        }
        if(!hasFound) throw new Exception("itemId does not found");
    }

    public void removeOrdersByTransportation(int transportationID) throws Exception {
        for (Order o : orders) {
            if (o.getTransportationID() == transportationID) {
                removeOrder(o.getOrderId());
            }
        }
    }
    public List<Item> getSupplierItems() {
        return items;
    }

    private void loadOrdersByTransportationID(int transportationID) {
        List<Order> ordersOfTransportation = new LinkedList<>();
        List<Tuple<List<Class>,List<Object>>> list1 = dalSupplierCard.loadOrdersByTransportation(transportationID);
        if (list1.size() > 0) {
            for (int i = 0; i < list1.get(0).item2.size(); i = i + 8) {
                int key = (int) list1.get(0).item2.get(i);
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
                        ordersOfTransportation.add(new regularOrder((DalOrder) check));
                    }
                    else {
                        ordersOfTransportation.add(new neededOrder((DalOrder) check));
                    }
                }
            }
        }
    }

    public void updateArrived(int orderId) throws Exception {
        boolean found = false;
        try{
            for (Order o : orders) {
                if (o.getOrderId() == orderId) {
                    o.updateArrived();
                    found = true;
                }
            }
            if(!found) throw new Exception("orderId does not exist.");
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public LocalDate getExpirationDate(int itemId) throws ExportException {
        try {
            for (Item item : items) {
                if (item.getItemId() == itemId)
                    return item.getExpirationDate();
            }
        } catch (Exception e) {
            throw new ExportException(e.getMessage());
        }
        throw new ExportException("there is no such itemId to this supplier");
    }
}

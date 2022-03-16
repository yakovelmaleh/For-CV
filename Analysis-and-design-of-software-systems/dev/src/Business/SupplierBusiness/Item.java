package Business.SupplierBusiness;

import DataAccess.DALObject;
import DataAccess.DalSuppliers.DalItem;
import DataAccess.DalSuppliers.DalQuantityDocument;
import DataAccess.SMapper;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Item{
    private QuantityDocument quantityDocument;
    private DalItem dalItem;
    final static Logger log=Logger.getLogger(Item.class);

    public Item(int supplierBN, int itemId , String name , double price, LocalDate expirationDate, double weight){
        List<Tuple<Object,Class>> list=new ArrayList<>();
        list.add(new Tuple<>(itemId,Integer.class));
        list.add(new Tuple<>(supplierBN,Integer.class));
        list.add(new Tuple<>(name,String.class));
        list.add(new Tuple<>(price,Double.class));
        list.add(new Tuple<>(expirationDate.toString(),String.class));
        list.add(new Tuple<>(weight,Double.class));
        SMapper map= SMapper.getMap();
        map.setItem(DalItem.class,list);
        List<Integer> keyList=new ArrayList<>();
        keyList.add(itemId);
        DALObject check =map.getItem(DalItem.class ,keyList);
        if (check==null ||(check.getClass()!=DalItem.class)){
            String s="the instance that return from Mapper is null";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        else{
            log.info("create new Object");
            dalItem = (DalItem) check;
        }
        quantityDocument = null;
    }

    public Item(DalItem dalItem) {
        this.dalItem = dalItem;
        loadQuantityDocument();
    }

    private void loadQuantityDocument() {
        Tuple<List<Class>,List<Object>> tuple = dalItem.loadQuantityDocument();
        if (tuple != null &&  tuple.item2 != null && tuple.item2.size()>0) {
            int key = (int) tuple.item2.get(0);
            SMapper map = SMapper.getMap();
            List<Integer> keyList = new ArrayList<>();
            keyList.add(key);
            DALObject check = map.getItem(DalQuantityDocument.class, keyList);
            if (DalQuantityDocument.class == null || check == null || (check.getClass() != DalQuantityDocument.class)) {
                String s = "the instance that return from Mapper is null";
                log.warn(s);
                throw new IllegalArgumentException(s);
            } else {
                log.info("loaded new Object");
                quantityDocument = new QuantityDocument((DalQuantityDocument) check);
            }
        }
        else {
            quantityDocument = null;
        }
    }

    public void addQuantityDocument(int minimalAmount, int discount) throws Exception {
        if(minimalAmount < 0) throw new Exception("minimal amount must be a positive number");
        if(discount < 0 || discount > 100) throw new Exception("discount must be a number between 0 to 100");
        quantityDocument = new QuantityDocument(dalItem.getItemId(), minimalAmount, discount);
    }

    public void removeQuantityDocument() throws Exception {
        ////NEED TO ADD REMOVE FROM DATABASE
        if(quantityDocument == null) throw new Exception("quantity document all ready removed");
        quantityDocument = null;
    }

    public QuantityDocument showQuantityDocument() throws Exception {
        if(quantityDocument == null) throw new Exception("quantity document does not exist");
        return quantityDocument;
    }

    public void updateMinimalAmountOfQD(int minimalAmount) throws Exception {
        try {
            quantityDocument.updateMinimalAmountOfQD(minimalAmount);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void updateDiscountOfQD(int discount) throws Exception {
        try {
            quantityDocument.updateDiscountOfQD(discount);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public int getItemId() {
        return dalItem.getItemId();
    }

    public QuantityDocument getQuantityDocument(){
        return quantityDocument;
    }

    public double getPrice() {
        return dalItem.getPrice();
    }

    public double getWeight() {
        return dalItem.getWeight();
    }

    public void updatePrice(double price) throws Exception {
        if(price < 0) throw new Exception("price must be a positive number");
        dalItem.updatePrice(price);
    }

    public String getName() { return dalItem.getName(); }

    public LocalDate getExpirationDate() { return LocalDate.parse(dalItem.getExpirationDate()); }

    public void removeItem() {
        dalItem.removeItem();
    }
}

package Business.StockBusiness.Type;

import Business.StockBusiness.instance.Location;
import Business.StockBusiness.instance.Product;
import DataAccess.DALObject;
import DataAccess.DalStock.DALProductType;
import DataAccess.SMapper;
import Utility.Tuple;
import Utility.Util;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductType {
    DALProductType dal;

    private List<SaleDiscount> _saleDiscounts=new ArrayList<>();
    private List<SupplierDiscount> _supplierDiscounts=new ArrayList<>();
    final static Logger log=Logger.getLogger(ProductType.class);
    public ProductType(){//for testing
      //  _typeID=1000;
    }



    public ProductType(int storeID,int _typeID, int _categoryID, String _name, int _minAmount, double _basePrice, double _salePrice, String _producer, List<Integer> _suppliers) {
        checkValues(_typeID,  _categoryID,  _name, _minAmount, _basePrice, _salePrice, _producer, _suppliers);
        //dal=Util.initDal(DALProductType.class,storeID,_typeID, _name, _categoryID, _minAmount,0,0, _basePrice, _salePrice, _producer);
        List<Tuple<Object,Class>> list=new ArrayList<>();
        list.add(new Tuple<>(storeID,Integer.class));
        list.add(new Tuple<>(_typeID,Integer.class));
        list.add(new Tuple<>(_name,String.class));
        list.add(new Tuple<>(_categoryID,Integer.class));
        list.add(new Tuple<>(_minAmount,Integer.class));
        list.add(new Tuple<>(0,Integer.class));
        list.add(new Tuple<>(0,Integer.class));
        list.add(new Tuple<>(_basePrice,Integer.class));
        list.add(new Tuple<>(_salePrice,Integer.class));
        list.add(new Tuple<>(_producer,Integer.class));
        SMapper map= SMapper.getMap();
        map.setItem(DALProductType.class,list);
        List<Integer> keyList=new ArrayList<>();
        keyList.add(storeID);
        keyList.add(_typeID);
        DALObject check =map.getItem(DALProductType.class,keyList);
        if (check==null ||(check.getClass()!=DALProductType.class)){
            String s="the instance that return from Mapper is null for: "+DALProductType.class;
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        else{
            log.info("create new Object");
            dal = (DALProductType) check;
        }
        dal.setSuppliers(_suppliers);
    }

    public ProductType(int id, Integer i) {
        List<Integer> list=new ArrayList<>();
        list.add(id);
        list.add(i);
        dal= (DALProductType) SMapper.getMap().getItem(DALProductType.class,list);
        log.warn("loaded product types");
        loadSaleDiscount();
        log.warn("loaded sale discounts");
        loadSupplierDiscount();
        log.warn("loaded supplier discounts");
    }
    public void loadSaleDiscount(){
        List<Integer> list=dal.getSaleDiscounts();
        log.warn("got sale discount");
        for (Integer i:list)
            _saleDiscounts.add(new SaleDiscount(dal.getStoreId(),i));
    }
    public void initSaleDiscount(List<SaleDiscount> list){
        _saleDiscounts.addAll(list);
    }

    public void loadSupplierDiscount(){
        List<Integer> list=dal.getSupplierDiscounts();
        log.warn(list);
        for (Integer i:list)
            _supplierDiscounts.add(new SupplierDiscount(dal.getStoreId(),i));
    }

    private void checkValues(Object... o){
        String s;
        for (int i = 0; i < o.length; i++) {
            if (o[i] instanceof String)
            {
                if (o[i]==null || o[i].equals(""))
                {
                    s="the value is illegal(String)";
                    log.warn(s);
                    throw new IllegalArgumentException(s);
                }
            }
            else if (o[i] instanceof Integer)
            {
                if ((Integer)o[i]<0)
                {
                    s="the value is illegal(String)";
                    log.warn(s);
                    throw new IllegalArgumentException(s);
                }
            }
            else if (o[i] instanceof Double)
            {
                if ((Double)o[i]<0)
                {
                    s="the value is illegal(String)";
                    log.warn(s);
                    throw new IllegalArgumentException(s);
                }
            }
            else
                log.warn("the checkValue get unanimous value");
        }


    }


    public ProductType(int storeID,int i, String name, int minAmount, double basePrice,double salePrice, String producer, int supID, int category) {
        checkValues(i,name, minAmount, basePrice,salePrice, producer,supID,category);
        dal=Util.initDal(DALProductType.class,storeID,i,name,category, minAmount,0,0, basePrice, salePrice, producer);
        dal.insertSupplier(storeID,i,supID);
    }

    public int get_typeID() {
        return dal.get_typeID();
    }


    public int get_categoryID() {
        return dal.get_categoryID();
    }

    public void set_categoryID(int _categoryID) {
        dal.setCategory(_categoryID);
    }

    public List<Integer> get_products() {
        return dal.get_products();
    }

    public void initProducts(List<Integer> _products) {
        dal.initProducts(_products);
    }

    public String get_name() {
        return dal.get_name();
    }

    public void set_name(String _name) {
        dal.set_name(_name);
    }

    public int get_minAmount() {
        return dal.get_minAmount();
    }

    public void set_minAmount(int _minAmount) {
        dal.set_minAmount(_minAmount);
    }

    public int get_shelfCurr() {
        return dal.get_shelfCurr();
    }


    public int get_storageCurr() {
        return dal.get_storageCurr();
    }

    public double get_basePrice() {
        return dal.get_basePrice();
    }

    public void set_basePrice(double _basePrice) {
        dal.set_basePrice(_basePrice);
    }

    public double get_salePrice() {
        double min=dal.get_basePrice();
        for (SaleDiscount s: _saleDiscounts){
            if (s.relevant() && min>get_basePrice()*(1-s.get_percent())){
               min= get_basePrice()*(1-s.get_percent());
            }
        }
        return min;
    }


    public String get_producer() {
        return dal.get_producer();
    }

    public void set_producer(String _producer) {
        dal.set_producer(_producer);
    }

    public List<Integer> get_suppliers() { return dal.get_suppliers();    }

    public void set_suppliers(List<Integer> _suppliers) {dal.setSuppliers(_suppliers); }


    public void addSaleProductDiscount(int discountID,double percent, Date start, Date end) {
        log.debug(String.format("addSaleProductDiscount(float percent, Date start, Date end) Values:?,?,?",percent,start,end));
        SaleDiscount s=new SaleDiscount(dal.getStoreId(),discountID,get_typeID(),0,percent,start,end);
        try{
            dal.addSaleProductDiscount(s.get_discountID());
        }
        catch (Exception e){
            String info="can not add Sale Discount";
            log.warn(info);
            throw new IllegalArgumentException(info);
        }
        _saleDiscounts.add(s);
    }


    public void addDiscount(SaleDiscount discount){
        log.debug(String.format("addDiscount(SaleDiscount discount) typeID: %d DiscountID: %d",dal.get_typeID(),discount.get_discountID()));
        if (!_saleDiscounts.contains(discount)) {
            try{
                dal.addSaleProductDiscount(discount.get_discountID());
            }
            catch (Exception e){
                String info="can not add Sale Discount";
                log.warn(info);
                throw new IllegalArgumentException(info);
            }
            _saleDiscounts.add(discount);
        }
    }
    public void addDiscount(SupplierDiscount discount){
        log.debug(String.format("addDiscount(SupplierDiscount discount) typeID: %d",dal.get_typeID()));
        _supplierDiscounts.add(discount);
    }

    public void removeDiscount(SaleDiscount s){
        try{
            dal.removeSaleDiscount(s.get_discountID());
        }
        catch (Exception e){
            String info="can not remove Sale Discount";
            log.warn(info);
            throw new IllegalArgumentException(info);
        }
        _saleDiscounts.remove(s);
    }

    public void removeDiscountFromList(Discount s){
        String str="the function get illegal arg";
        log.error(str);
        throw new IllegalArgumentException(str);
    }

    public List<SupplierDiscount> getSupplierDiscounts() {
        log.warn(_supplierDiscounts);
        return _supplierDiscounts;
    }

    public List<SaleDiscount> getSalePrice() {
        return _saleDiscounts;
    }

    public void edit(String name, int minAmount, double basePrice, String producer, int supID, int category
            , List<SaleDiscount> saleDiscountsToDelete, List<SaleDiscount> saleDiscountsToAdd) {
        log.debug(String.format("edit(String name, int minAmount, float basePrice, String producer, int supID," +
                " int category) Value:?,?,?,?,?,?" ,name,minAmount,basePrice,producer,supID,category));
        checkValues(name,minAmount,basePrice,producer,supID,category);
        set_basePrice(basePrice);
        set_name(name);
        set_categoryID(category);
        set_minAmount(minAmount);
        set_producer(producer);
        dal.addSupplier(supID);
        for (SaleDiscount toDelete: saleDiscountsToDelete){
            try{
                dal.removeSaleDiscount(toDelete.get_discountID());
            }
            catch (Exception e){
                String info="can not remove Sale Discount";
                log.warn(info);
                throw new IllegalArgumentException(info);
            }
            _saleDiscounts.remove(toDelete);}
        for (SaleDiscount toAdd: saleDiscountsToAdd){
            if (!_saleDiscounts.contains(toAdd)) {
                try{
                    dal.addSaleProductDiscount(toAdd.get_discountID());
                }
                catch (Exception e){
                    String info="can not add Sale Discount";
                    log.warn(info);
                    throw new IllegalArgumentException(info);
                }
                _saleDiscounts.add(toAdd);
            }
        }
    }

    public void removeProduct(int productID, Tuple<Integer, Location> location) {
        log.debug(String.format("removeProduct(int productID, Tuple<Integer, Location> location) Values: "+productID+", "+location));
        if (!dal.productsContaions(productID)){
            String s=String.format("the product #? , is not exist in the ProductType List.",productID);
            log.error(s);
            throw new IllegalArgumentException(s);
        }
        log.debug("TypeID: "+ get_typeID()+" shelfcurr: "+dal.get_shelfCurr()+" storagecurr: "+dal.get_storageCurr());
        if ((location.item2.equals(Location.Shelves)&& dal.get_shelfCurr()<=0)|(location.item2.equals(Location.Storage)&&dal.get_storageCurr()<=0)){
            String s=String.format("the product #? , is not accept to the value in ProductType.",productID);
            log.error(s);
            throw new IllegalArgumentException(s);
        }
        dal.removeProduct(productID);
        log.debug("DONE REMOVE: "+productID);
        try {
            if ((location.item2.equals(Location.Shelves))) {
                dal.set_shelfCurr(dal.get_shelfCurr() - 1);
            } else {
                dal.set_storageCurr(dal.get_storageCurr() - 1);
            }
        }
        catch (Exception e){
            String info="can not refresh the amount of product in shelves";
            log.warn(info);
            throw new IllegalArgumentException(info);
        }
    }

    public void addProduct(int productID, Location l) {
        log.debug(String.format("addProduct(int productID, Location l) Values: "+productID+", "+l));
        if (dal.productsContaions(productID)){
            String s=String.format("the product "+productID+" , exists in the ProductType List already.");
            log.error(s);
            throw new IllegalArgumentException(s);
        }
        try{
            log.warn("adding product to product type.");
            dal.addProduct(productID);
            if ((l.equals(Location.Shelves))) {
                log.warn("adding shelves to product type.");
                dal.set_shelfCurr(dal.get_shelfCurr() + 1);
            } else {
                log.warn("adding storage to product type.");
                dal.set_storageCurr(dal.get_storageCurr() + 1);
            }
            log.warn("done adding shelves/storage to product type.");
        }
        catch (Exception e){
            String info="can not add product";
            log.warn(info);
            throw new IllegalArgumentException(info);
        }

    }

    public void relocateProduct(boolean toStorage,Location loc) {
        log.debug("relocateProduct()");
        log.warn("relocate in type with: toStorage="+toStorage+" shelf_curr="+dal.get_shelfCurr()+" storage_curr="+dal.get_storageCurr());
        if ((loc==Location.Shelves && dal.get_shelfCurr()<=0) ||(loc==Location.Storage && dal.get_storageCurr()<=0))
        {
            String s="no have product in this place.";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        try {
            if (toStorage && loc==Location.Shelves) {
                dal.set_storageCurr(dal.get_storageCurr() + 1);
                dal.set_shelfCurr(dal.get_shelfCurr() - 1);
            } else if(!toStorage && loc==Location.Storage) {
                dal.set_storageCurr(dal.get_storageCurr() - 1);
                dal.set_shelfCurr(dal.get_shelfCurr() + 1);
            }
        }
        catch (Exception e){
            String info="can not relocate product";
            log.warn(info);
            throw new IllegalArgumentException(info);
        }
    }

    public void reportDamage(Product i) {
        log.debug(String.format("reportDamage(int i) Value:?",i));
        if (!dal.productsContaions(i.get_id())){
            String s=String.format("the ID of product is not exist in system");
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        if ((i.get_location().item2.equals(Location.Storage)&& dal.get_storageCurr()<=0)||
                (i.get_location().item2.equals(Location.Shelves)&& dal.get_shelfCurr()<=0)){
            String s="no have product in this place.";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        try {
            if (i.get_location().item2.equals(Location.Storage)) {
                dal.set_storageCurr(dal.get_storageCurr() - 1);
            } else
                dal.set_shelfCurr(dal.get_shelfCurr() - 1);
        }
        catch (Exception e){
            String info="can not report damage";
            log.warn(info);
            throw new IllegalArgumentException(info);
        }
    }

    public Integer getNeededReport() {
        log.debug("getNeededReport()");
        return (dal.get_minAmount()<=dal.get_storageCurr()+dal.get_shelfCurr())?
                0: dal.get_minAmount()-dal.get_storageCurr()-dal.get_shelfCurr();
    }

    public List<Integer> getDiscount() {
        List<Integer> output=new ArrayList<>();
        for (SaleDiscount s: _saleDiscounts){
            output.add(s.get_discountID());
        }
        return output;
    }

    public void addSupplierDiscount(int discountID, double percent, Date start, Date end, int supId) {
        log.debug(String.format("addSupplierDiscount(float percent, Date start, Date end,supID)"));
        SupplierDiscount s=new SupplierDiscount(dal.getStoreId(),discountID,get_typeID(),percent,start,end,supId);
        log.warn("here");
        try{
            dal.addSupplerDiscount(s.get_discountID());
        }
        catch (Exception e){
            String info="can not add Supplier Discount";
            log.warn(info);
            throw new IllegalArgumentException(info);
        }
        _supplierDiscounts.add(s);
    }

    public void removeSupplier(int supplierId) {
        dal.removeSupplier(supplierId);
    }
}

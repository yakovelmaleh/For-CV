package Business.StockBusiness.instance;


import Business.StockBusiness.StoreController;
import DataAccess.DalStock.DALInstanceController;
import Utility.Tuple;
import Utility.Util;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class InstanceController {
    private static int _MAX_PRODUCTS_ON_PROTUCTTYPE= StoreController.getMaxProdOnType();
    private DALInstanceController dal;
    private Dictionary<Integer,Product> _products=new Hashtable<>();

    final static Logger log=Logger.getLogger(InstanceController.class);
    public InstanceController(){//for testing
       // _typeID=1000;
    }

    public InstanceController(int storeID,int typeID) {
        dal=Util.initDal(DALInstanceController.class,storeID,typeID,0);
        List<Integer> list=dal.getProducts();
        for (Integer i:list) {
            _products.put(i, new Product(storeID, i));
        }
    }
    public InstanceController(int storeID, int typeID,int max){
        _MAX_PRODUCTS_ON_PROTUCTTYPE=max;
        dal=Util.initDal(DALInstanceController.class,storeID,typeID,0);
    }

    public Product removeProduct(int i) {
        log.debug(String.format("removeProduct(int i) Value: "+i));
        Product p=checkProduct(i);
        try{
            dal.removeProduct(i);
        }
        catch (Exception e){
            String s=String.format("can not remove the product");
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        _products.remove(i);
        log.info(String.format("the IC remove Product "+i));
        return p;
    }
    private Product checkProduct(int i){
        String s;
        if (i<=0)
        {
            s=String.format("the value of i is illegal :"+i);
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        Product p=_products.get(i);
        if (p==null)
        {
            s=String.format("does not have a product with #%d ID",i);
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        return p;
    }

    public void addProduct(Product p) {
        log.debug("addProduct(Product p)");
        String s;
        if (p==null)
        {
            s=String.format("the product is null");
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        if (Collections.list(_products.elements()).contains(p))
        {
            s="the product in already exist in the system";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }

        try{
            dal.addProduct(p.get_id());
        }
        catch (Exception e){
            String info=String.format("can not add the product");
            log.warn(info);
            throw new IllegalArgumentException(info);
        }

        _products.put(p.get_id(),p);
        log.info(String.format("new item add to IC #?",dal.get_typeID()));
    }

    public int addProduct(Date expiration, Location l,int shelf) {
        log.debug("addProduct(Date expiration, Location l) Values: "+expiration+", "+l+", "+shelf);
        int id=dal.get_typeID()*_MAX_PRODUCTS_ON_PROTUCTTYPE+dal.get_counter();
        try {
            dal.set_counter(dal.get_counter() + 1);
        }
        catch (Exception e){
            String s="can not set the counter";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        log.warn("adding product to dal from instanceController.");
        Product p=new Product(dal.getStoreID(),dal.get_typeID(),id, expiration, new Tuple<>(shelf,l));
        log.warn("done adding product to dal in instanceController.");
        addProduct(p);
        log.warn("done adding product to biz in instanceController.");
        _products.put(id ,p);
        log.warn("done adding product to hash in instanceController.");
        return id;
    }
    public List<Integer> getProducts(){
        return Collections.list(_products.keys()).stream().filter(x->!_products.get(x).is_isDamage()).collect(Collectors.toList());
    }

    public Product reportDamage(int i) {
        log.debug(String.format("reportDamage(int i) Value:?",i));
        Product p=checkProduct(i);
        if (p.is_isDamage())
            throw new IllegalArgumentException("this product already reported");
        p.set_isDamage();
        return p;
    }

    public Product getProduct(int i) {
        log.debug(String.format("getProduct(int i)",i));
        return checkProduct(i);
    }

    public Dictionary<Integer, Tuple<Integer,Boolean>> getWeeklyReport() {
        log.debug("getWeeklyReport()");
        Dictionary<Integer,Tuple<Integer,Boolean>> output=new Hashtable<>();
        List<Integer> ids=Collections.list(_products.keys());
        for(int i:ids){
            output.put(i,new Tuple(_products.get(i).getShelf(),_products.get(i).get_location().equals(Location.Storage)));
        }
        return output;
    }

    public void getWasteReport(List<Integer> list) {
        log.debug("getWasteReport(List<Integer> list)");
        for(Product p: Collections.list(_products.elements())){
            if(p.is_isDamage()) {
                list.add(p.get_id());
                dal.removeProduct(p.get_id());
                _products.remove(p.get_id());
                //p.remove();
            }
        }
    }
}

package Business.StockBusiness.instance;

import DataAccess.DALObject;
import DataAccess.DalStock.DALProduct;
import DataAccess.SMapper;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product {
    DALProduct dal;
   // private Location _location;
    final static Logger log=Logger.getLogger(Product.class);

    public Product(int storeId,int typeID,int id, Date expiration, Tuple<Integer,Location> shelf) {
        checkLocation(shelf);
        //dal= Util.initDal(DALProduct.class,storeId,id,typeID,expiration.toString(),0,shelf.item1,(shelf.item2==Location.Shelves)?0:1);
        //_location=shelf.item2;

        try {

            String pattern="dd-MM-yyyy";
            DateFormat df=new SimpleDateFormat(pattern);
            String tosend=df.format(expiration);
            Class c = DALProduct.class;
            List<Tuple<Object, Class>> list = new ArrayList<>();
            list.add(new Tuple<>(storeId, Integer.class));
            list.add(new Tuple<>(typeID, Integer.class));
            list.add(new Tuple<>(id, Integer.class));
            list.add(new Tuple<>(tosend, String.class));
            list.add(new Tuple<>(0, Integer.class));
            list.add(new Tuple<>(shelf.item1, Integer.class));
            list.add(new Tuple<>((shelf.item2 == Location.Shelves) ? 0 : 1, Integer.class));
            SMapper map= SMapper.getMap();
            map.setItem(c,list);
            List<Integer> keyList = new ArrayList<>();
            keyList.add(storeId);
            keyList.add(id);
            log.warn("starting 1st check DALProduct");
            DALObject check = map.getItem(c, keyList);
            log.warn("did 1st check DALProduct");
            if (check != null) {
                log.warn("entry is already in DB, will not attempt insert.");
                dal = (DALProduct) check;
                return;
            }
            map.setItem(c, list);
            log.warn("did insert DALProduct");
            check = map.getItem(c, keyList);
            if (c == null || check == null || (check.getClass() != c)) {
                String s = "the instance that return from Mapper is null for: " + c;
                log.warn(s);
                throw new IllegalArgumentException(s);

            } else {
                log.info("create new Object");
            }
            dal = (DALProduct) check;
        }catch (Exception e){log.warn(e);}
    }

    public int getType(){
        return dal.getTypeID();
    }

    public Product(int storeID, Integer i) {
        List<Integer> list=new ArrayList<>();
        list.add(storeID);
        list.add(i);
        dal=(DALProduct) SMapper.getMap().getItem(DALProduct.class,list);
    }

    public int get_id() {
        log.debug("get_id()");
        return dal.get_id();
    }

    public Date get_expiration()
    {
        log.debug("get_expiration()");
        return dal.get_expiration();
    }

    public boolean is_isDamage() {
        log.debug("is_isDamage()");
        return dal.is_isDamage();
    }

    public void set_isDamage() {
        log.debug(String.format("set_isDamage()"));
        if (dal.is_isDamage())
        {
            String s=String.format("the product #%d , is damage already.",dal.get_id());
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        try {
            dal.set_isDamage(true);
        }
        catch (Exception e){
            String s="can not change the value of damage";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
    }

    public Tuple<Integer, Location> get_location() {
        return new Tuple<>(dal.getShelfNum(),(dal.get_location().item2=="Shelves")?Location.Shelves:Location.Storage);
    }

    public void set_location(Tuple<Integer, Location> location)
    {
        log.debug(String.format("set_location(Tuple<Integer, Location> _location)"));
        checkLocation(location);
        try {
            dal.setLocation(location.item1,location.item2.toString());
        }
        catch (Exception e){
            String s="can not change the value of location";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        //_location = location.item2;
    }
    private void checkLocation(Tuple<Integer, Location> location){
        if (location.item1<1){
            String s=String.format("the value of shelf (#%d) is illegal",location.item1);
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
    }

    public int getShelf() {
        return dal.getShelfNum();
    }

    @Override
    public String toString() {
        return "Product{" +
                "_id=" + dal.get_id() +
                "_type="+ dal.getTypeID()+
                ", _expiration=" + dal.get_expiration() +
                ", _isDamage=" + dal.is_isDamage() +
                ", _location=" + dal.get_location().item1 +","+dal.get_location().item2+
                '}';
    }

    public void remove() {
        dal.removeProduct();
    }
}

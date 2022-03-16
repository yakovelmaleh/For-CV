package DataAccess.DalStock;

import DataAccess.DALObject;
import DataAccess.DalController;
import DataAccess.SMapper;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DALInstanceController extends DALObject {
    final static Logger log=Logger.getLogger(DALInstanceController.class);
    private int _storeID;
    private int _typeID;
    private int _counter=0;
    private List<Integer> _products=new ArrayList<>();
    String tableName="InstanceController";


    public DALInstanceController(){
        super(null);
    }

    public DALInstanceController(Integer storeID, Integer typeID, Integer counter, DalController dc){
        super(dc);
        _storeID=storeID;
        _typeID=typeID;
        _counter=counter;
        loadProduct();
    } // get products from controller

    @Override
    public String getCreate() {
        return "CREATE TABLE IF NOT EXISTS InstanceController  (\n" +
                "\tstoreID INTEGER NOT NULL,\n" +
                "\ttypeID INTEGER NOT NULL,\n" +
                "\tcounter INTEGER NOT NULL,\n" +
                "\tPRIMARY KEY (storeID, typeID),\n" +
                "\tUNIQUE (storeID, typeID),\n" +
//                "\tFOREIGN KEY (storeID) REFERENCES StoreController(storeID)\n" +
//                "\tON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "\tFOREIGN KEY (storeID, typeID) REFERENCES ProductType(storeID, typeID)\n" +
                "\tON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");";
    }
    private void loadProduct(){
        String query= """
                SELECT productID \s
                FROM Product \s
                WHERE StoreID=? AND typeID=?;""";
        List<Integer> list=new ArrayList<>();
        list.add(_storeID);
        list.add(_typeID);
        try {
            List<Tuple<List<Class>,List<Object>>> tmp= DC.SelectMany(query,list);

            for (Tuple<List<Class>,List<Object>> t: tmp){
                _products= tmp.stream().map(x->(int)x.item2.get(0)).collect(Collectors.toList());
            }
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
    }

    @Override
    public String getSelect() {
        return """
                SELECT * \s
                FROM InstanceController
                WHERE StoreID=? AND typeID=?;""";
    }

    @Override
    public String getDelete() {
        return """
                DELETE FROM InstanceController \s
                WHERE storeID=? AND typeID=?;""";
    }

    @Override
    public String getUpdate() {
        return null;
    }

    @Override
    public String getInsert() {
        return """
                INSERT INTO InstanceController \s
                VALUES(?,?,?);""";
    }
    public void removeProduct(int i){
        List<Integer> key=new ArrayList<>();
        key.add(_storeID);
        key.add(i);
        DALProduct pt=(DALProduct) SMapper.getMap().getItem(DALProduct.class,key);
        pt.removeProduct();
        log.warn("done remove from DB, list is: "+_products+" i is:"+i);
        log.warn(i);
        log.warn(_products);
        _products.remove(_products.indexOf(i));
        log.warn(_products);
    }
    public void addProduct(int i){
        List<Integer> key=new ArrayList<>();
        key.add(_storeID);
        key.add(i);
        DALProduct pt= (DALProduct) SMapper.getMap().getItem(DALProduct.class,key);
        pt.addProduct(_typeID);
        _products.add(i);
    }
    public int get_typeID(){return _typeID;}
    public int get_counter(){return _counter;}
    public void set_counter(int i){
        String query="UPDATE InstanceController \n" +
                "SET counter=?\n" +
                "WHERE \n" +
                "storeID=?\n" +
                "AND typeID=?;";
        List<Tuple<Object,Class>> list=new ArrayList<>();
        list.add(new Tuple<>(i,Integer.class));
        list.add(new Tuple<>(_storeID,Integer.class));
        list.add(new Tuple<>(_typeID,Integer.class));
        try {
            DC.noSelect(query, list);
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
        _counter=i;
    }
    public List<Integer> getProducts(){
        String query= """
                SELECT productID \s
                FROM Product \s
                WHERE storeID=? AND typeID=?;""";
        List<Integer> list=new ArrayList<>();
        list.add(_storeID);
        list.add(_typeID);
        try{
            List<Tuple<List<Class>, List<Object>>> get= DC.SelectMany(query,list);
            List<Integer> ret=new ArrayList<>();
            if(get.size()==0) return ret;
            for(int i =0;i<get.get(0).item2.size();i=i+2){
                ret.add((Integer) get.get(0).item2.get(i));
            }
            return ret;
        }
        catch (Exception e){
            throw new IllegalArgumentException("failed to get products in instanceController");
        }
    }
    public int getStoreID(){
        return _storeID;
    }
}

package DataAccess.DalStock;

import DataAccess.DALObject;
import DataAccess.DalController;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DALStoreController extends DALObject {

    private int _storeID=0;
    private int _typeCounter=0;
    private int _categoryCounter=0;
    private int _discountCounter=0;
    private int _numberOfShelves=10;
    private int _storeShelves;
    private String tableName="StoreController";

    final static Logger log=Logger.getLogger(DALStoreController.class);

    public DALStoreController(){
        super(null);
    }

    public DALStoreController(Integer storeID, Integer storeShelves, Integer shelves, Integer discountCounter,
                              Integer typeCounter, Integer categoryCounter, DalController dc){
        super(dc);
        this._storeID=storeID;
        this._storeShelves=storeShelves;
        this._numberOfShelves=shelves;
        this._discountCounter=discountCounter;
        this._typeCounter=typeCounter;
        this._categoryCounter=categoryCounter;
    }
    //get categories controllers discounts and product types from controller

    @Override
    public String getCreate() {
        return "CREATE TABLE IF NOT EXISTS StoreController (\n" +
                "\tstoreID INTEGER NOT NULL UNIQUE,\n" +
                "\tstoreShelves INTEGER NOT NULL,\n" +
                "\tnumberOfShelves INTEGER NOT NULL,\n" +
                "\tdiscountCounter INTEGER NOT NULL,\n" +
                "\ttypeCounter INTEGER NOT NULL,\n" +
                "\tcategoryCounter INTEGER NOT NULL,\n"+
                "\tFOREIGN KEY(\"storeID\") REFERENCES \"Branches\"(\"BID\"),\n" +
                "\tPRIMARY KEY(\"storeID\")\n" +
                ");";
    }

    @Override
    public String getSelect() {
        return "SELECT * FROM StoreController WHERE storeID=?";
    }

    @Override
    public String getDelete() {
        return null;
    }

    @Override
    public String getUpdate() {
        return null;
    }

    @Override
    public String getInsert() {
        return "INSERT OR REPLACE INTO StoreController VALUES (?,?,?,?,?,?);";
    }

    public int getStoreID(){
        return _storeID;
    }
    public int categoryCounter(){
        return _categoryCounter;
    }
    public void setCategoryCounter(int i){
        updateCategoryCounter(prepareList(i,_storeID));
        _categoryCounter=i;
    }
    private void updateTypeCounter(List<Tuple<Object,Class>> list){
        String updateName= """
                UPDATE StoreController\s
                SET typeCounter = ?\s
                WHERE storeID = ?;""";
        try {
            DC.noSelect(updateName, list);
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
    }
    private void updateDiscountCounter(List<Tuple<Object,Class>> list){
        String updateName= """
                UPDATE StoreController\s
                SET discountCounter = ?\s
                WHERE storeID = ?;""";
        try {
            DC.noSelect(updateName, list);
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
    }
    private void updateCategoryCounter(List<Tuple<Object,Class>> list){
        String updateName= """
                UPDATE StoreController\s
                SET categoryCounter = ?\s
                WHERE storeID = ?;""";
        try {
            DC.noSelect(updateName, list);
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
    }
//    private void update(List<Tuple<Object,Class>> list){
//        String updateName= """
//                UPDATE StoreController\s
//                SET ?=?
//                WHERE\s
//                storeID=?;""";
//        try {
//            DC.noSelect(updateName, list);
//        }
//        catch (Exception e){
//            throw new IllegalArgumentException("fail");
//        }
//    }
    public int getTypeCounter(){
        return _typeCounter;
    }
    public void setTypeCounter(int i){
        updateTypeCounter(prepareList(i,_storeID));
        _typeCounter=i;
    }
    public int get_discountCounter(){
        return _discountCounter;
    }
    public void set_discountCounter(int i){
        updateDiscountCounter(prepareList(i,_storeID));
        _discountCounter=i;
    }


    public int get_storeShelves(){
        return _storeShelves;
    }
    public int get_numberOfShelves(){
        return _numberOfShelves;
    }
    public List<Integer> getListShelves(){
        return selectMany("shelfID","Shelf");
    }
    public List<Integer> getTypes(){
        return selectMany("typeID","ProductType");
    }
    public List<Integer> selectMany(String column,String table){
        String query="SELECT "+column+" \n"+
                "FROM "+table+"\n" +
                "WHERE storeID=?;";
        List<Integer> list= new ArrayList<>();
        list.add(_storeID);
        try {
            log.warn("query: "+query+" params: "+list);
            List<Tuple<List<Class>, List<Object>>> get= DC.SelectMany(query,list);
            List<Integer> ret=new ArrayList<>();
            if(get.size()==0) return ret;
            for(int i =0;i<get.get(0).item2.size();i=i+2){
                ret.add((Integer) get.get(0).item2.get(i));
            }
             //ret=get.stream().map(x->(Integer)x.item2.get(0)).collect(Collectors.toList());
            log.warn(ret);
            return ret;
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
    }
    public List<Integer> getSaleCategoryDiscount(){
        String query= """
                SELECT discountID \s
                FROM Discount \s
                WHERE storeID=? AND categoryID>0 ;""";
        List<Integer> list= new ArrayList<>();
        list.add(_storeID);
        try {
            return DC.SelectMany(query,list).stream().map(x->(Integer)x.item2.get(0)).collect(Collectors.toList());
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
    }
    public List<Integer> getCategories(){
        return selectMany("categoryID","Category");
    }
    private List<Tuple<Object,Class>> prepareList(Object... o){
        List<Tuple<Object,Class>> params=new ArrayList<>();
        for (Object o1:o){
            params.add(new Tuple<>(o1,o1.getClass()));
        }
        return params;
    }
}

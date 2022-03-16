package DataAccess.DalStock;

import DataAccess.DALObject;
import DataAccess.DalController;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DALProduct extends DALObject {
    final static Logger log=Logger.getLogger(DALProduct.class);
    private int _id;
    private String _expiration;
    private int _isDamage;
    private int storeID;
    private Tuple<Integer, String> _location=new Tuple<>(0,"");
    private String tableName="Product";
    private int typeID;

    public int getTypeID() {
        return typeID;
    }


    public Tuple<Integer, String> get_location() {
        return _location;
    }

    public DALProduct(){
        super(null);
    }

    public DALProduct(Integer storeID, Integer typeID, Integer id, String date, Integer isDamaged,Integer shelf, Integer Location, DalController dc){
        super(dc);
        this.storeID=storeID;
        this.typeID=typeID;
        _id=id;
        _isDamage=isDamaged;
        _expiration=date;
        _location=new Tuple<>(shelf,Location==0?"Shelves":"Storage");
    } //get location from controller



    @Override
    public String getCreate() {
        return "CREATE TABLE IF NOT EXISTS Product (\n" +
                "\tstoreID INTEGER NOT NULL ,\n" +
                "\ttypeID INTEGER NOT NULL,\n" +
                "\tproductID INTEGER NOT NULL ,\n" +
                "\texpiration VARCHAR NOT NULL,\n" +
                "\tisDamaged INTEGER NOT NULL,\n" +
                "\tshelfNum INTEGER NOT NULL,\n" +
                "\tlocation INTEGER NOT NULL,\n" +
                "\tPRIMARY KEY (storeID, productID),\n" +
                "\tUNIQUE (storeID,productID),\n" +
                //"\tFOREIGN KEY (storeID) REFERENCES StoreController(storeID)\n" +
                "\tFOREIGN KEY (storeID,shelfNum, location) REFERENCES Shelf(storeID,shelfID, location) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");";
//        return "CREATE TABLE IF NOT EXISTS Product (\n" +
//                "\tstoreID INTEGER NOT NULL,\n" +
//                "\ttypeID INTEGER NOT NULL,\n" +
//                "\tproductID INTEGER NOT NULL,\n" +
//                "\texpiration VARCHAR NOT NULL,\n" +
//                "\tisDamaged INTEGER NOT NULL,\n" +
//                "\tshelfNum INTEGER NOT NULL,\n" +
//                "\tlocation VARCHAR NOT NULL,\n" +
//                "\tPRIMARY KEY (storeID, productID),\n" +
//                "\tFOREIGN KEY (storeID) REFERENCES StoreController(storeID)\n" +
//                "\tON DELETE CASCADE ON UPDATE CASCADE,\n" +
//                "\tFOREIGN KEY (typeID) REFERENCES InstanceController(typeID)\n" +
//                "\tON DELETE CASCADE ON UPDATE CASCADE\n" +
//                ");";
//                "CREATE TABLE IF NOT EXISTS ShelfProduct (\n" +
//                "\tshelfID INTEGER NOT NULL,\n" +
//                "\tlocation INTEGER NOT NULL,\n" +
//                "\tproductID INTEGER NOT NULL,\n" +
//                "\ttypeID INTEGER NOT NULL,\n" +
//                "\tcurr INTEGER NOT NULL,\n" +
//                "\tPRIMARY KEY (shelfID, location, productID),\n" +
//                "\tFOREIGN KEY (productID) REFERENCES Product(productID)\n" +
//                "\tON DELETE CASCADE ON UPDATE CASCADE\n" +
//                "\tFOREIGN KEY (typeID) REFERENCES Product(typeID)\n" +
//                "\tON DELETE CASCADE ON UPDATE CASCADE\n" +
//                ");";
    }

    @Override
    public String getSelect() {
        return """
                SELECT * \s
                FROM Product \s
                WHERE storeID=? AND productID=?; \s
                """;
    }

    @Override
    public String getDelete() {
        return """
                DELETE FROM Product \s
                WHERE storeID=? AND productID=?; \s
                """;
    }

    @Override
    public String getUpdate() {
        return null;
    }

    @Override
    public String getInsert() {
        return "INSERT OR REPLACE INTO Product\n"+
                "VALUES (?,?,?,?,?,?,?);";
    }
    public void removeProduct(){
        String query= """
                DELETE FROM Product\s
                WHERE storeID=? AND productID=?;""";
        List<Tuple<Object,Class>> params=prepareList(storeID,_id);
        try {
            DC.noSelect(query,params);
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
    }
    public void addProduct(int i){
        String query= """
                UPDATE Product\s
                SET typeID=? WHERE storeID=? AND productID=?;""";
        List<Tuple<Object,Class>> params=prepareList(i,storeID,_id);
        try {
            DC.noSelect(query,params);
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }

    }
    public int get_id(){return _id;}
    public Date get_expiration(){
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(_expiration);
        }
        catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    public boolean is_isDamage(){return (_isDamage==1);}
    public void set_isDamage(boolean b){
        String query= """
                UPDATE Product\s
                SET isDamaged=?
                WHERE\s
                storeID=?
                AND productID=?;""";
        List<Tuple<Object,Class>> params=prepareList((b)? 1:0,storeID,_id);
        try {
            DC.noSelect(query,params);
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
        if (b)
            _isDamage=1;
        else
            _isDamage=0;
    }
    public int getShelfNum(){return  _location.item1;}
    public void setLocation(int num,String s){
        String query= """
                UPDATE Product\s
                SET shelfNum=?, location=?
                WHERE\s
                storeID=?
                AND productID=?;""";
        List<Tuple<Object,Class>> params=prepareList(num,s.equals("Storage")?1:0,storeID,_id);
        log.warn("done change product shelf num");
//        if (!_location.item2.equals(s))
//        {
//            query+="""
//                UPDATE Product\s
//                SET location=?
//                WHERE\s
//                storeID=?
//                AND productID=?;""";
//            params.addAll(prepareList(s,storeID,_id));
//        }
        try {
            DC.noSelect(query,params);
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
        _location.item1=num;
        _location.item2=s;
    }

    private List<Tuple<Object,Class>> prepareList(Object... o){
        List<Tuple<Object,Class>> params=new ArrayList<>();
        for (Object o1:o){
            params.add(new Tuple<>(o1,o1.getClass()));
        }
        return params;
    }
}

package DataAccess.DalStock;

import Business.StockBusiness.instance.Location;
import DataAccess.DALObject;
import DataAccess.DalController;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DALShelf extends DALObject {
    final static Logger log=Logger.getLogger(DALShelf.class);
    private int _shelfID;
    private int _cur;
    private int _typeID=0;
    private int _maxAmount;
    private int isStorage;
    int storeId;
    String tableName="Shelf";

    public DALShelf(){
        super(null);
    }

    public DALShelf(Integer storeID,Integer id, Integer isStorage, Integer typeID, Integer curr, Integer max, DalController dc){
        super(dc);
        this.storeId=storeID;
        _typeID=typeID;
        this.isStorage=isStorage;
        _cur=curr;
        _maxAmount=max;
        _shelfID=id;
    }

    public String getCreate() {
        return "CREATE TABLE IF NOT EXISTS Shelf (\n" +
                "\tstoreID INTEGER NOT NULL,\n" +
                "\tshelfID INTEGER NOT NULL,\n" +
                "\tlocation INTEGER NOT NULL,\n" +
                "\ttypeID INTEGER,\n" +
                "\tcurr INTEGER NOT NULL,\n" +
                "\tmaximum INTEGER NOT NULL,\n" +
                "\tPRIMARY KEY (storeID,shelfID, location),\n" +
                "\tUNIQUE (storeID,shelfID, location),\n" +
                "\tFOREIGN KEY (storeID, typeID) REFERENCES ProductType(storeID, typeID)\n" +
                "\tON DELETE CASCADE ON UPDATE CASCADE\n" +
//                "\tFOREIGN KEY (storeID) REFERENCES StoreController(storeID)\n" +
//                "\tON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");";
    }

    public String getSelect() {
        return """
                SELECT * \s
                FROM Shelf \s
                WHERE storeID=? AND shelfID=?;""";
    }

    public String getDelete() {
        return """
                DELETE FROM Shelf \s
                WHERE storeID=? AND shelfID=?;""";
    }

    public String getUpdate() {
        return null;
    }

    public String getInsert() {
        return """
                INSERT INTO Shelf \s
                VALUES (?,?,?,?,?,?);""";
    }
    public int getID(){return _shelfID;}
    public int getCur(){return _cur;}
    public int getMax(){
        return _maxAmount;
    }
    public void setType(int typeID){
        String query= """
                UPDATE Shelf \s
                SET typeID=? WHERE storeID=? AND shelfID=?;""";
        //List<Tuple<Object,Class>> params=prepareList(typeID==0?null:typeID,storeId,_shelfID);
        List<Tuple<Object,Class>> params=new ArrayList<>();
        params.add(new Tuple<>(typeID==0?null:typeID,Integer.class));
        params.add(new Tuple<>(storeId,Integer.class));
        params.add(new Tuple<>(_shelfID,Integer.class));
        log.warn("query: "+query+" params: "+params);
        try {
            DC.noSelect(query,params);
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
        this._typeID=typeID;
    }
    public void setCur(int cur){
        String query= """
                UPDATE Shelf \s
                SET curr=? WHERE storeID=? AND shelfID=?;""";
        List<Tuple<Object,Class>> params=prepareList(cur,storeId,_shelfID);
        try {
            DC.noSelect(query,params);
            _cur=cur;
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }

    }
    public int get_typeID(){return _typeID;}

    protected List<Tuple<Object,Class>> prepareList(Object... o){
        List<Tuple<Object,Class>> params=new ArrayList<>();
        for (Object o1:o){
            params.add(new Tuple<>(o1,o1.getClass()));
        }
        return params;
    }
    public boolean isStorage(){
        return isStorage==1;
    }
    public Location getLocation(){
        if (isStorage())
            return Location.Storage;
        else
            return Location.Shelves;
    }

}

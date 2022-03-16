package DataAccess.DalStock;

import DataAccess.DalController;
import Utility.Tuple;

import java.util.List;

public class DALSaleDiscount extends DALDiscount{
    private int categoryID;

    public DALSaleDiscount(){
        super(null);
    }

    public int getCategoryID(){
        return categoryID;
    }

    public DALSaleDiscount(Integer storeID, Integer id, Integer typeID, Integer categoryID,Integer supplier,
                           Double percent, String startDate, String endDate, DalController dc){
        super(dc,storeID,id,typeID,percent,startDate,endDate);
        this.categoryID=categoryID;
    }// get supplier id from controller

    @Override
    public String getCreate() {
        return super.getCreate();
    }

    public void removeCategory(int i) {
        String query= """
                UPDATE Discount \s
                SET categoryID=0
                WHERE storeID=? AND discountID=? AND categoryID=?;""";
        List<Tuple<Object,Class>> params=prepareList(storeID,_discountID,categoryID);
        try {
            DC.noSelect(query,params);
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
        categoryID=0;
    }

    public void addCategory(int i) {
        String query= """
                UPDATE Discount \s
                SET categoryID=?
                WHERE storeID=? AND discountID=?;""";
        List<Tuple<Object,Class>> params=prepareList(i,storeID,_discountID);
        System.out.println("query: \n"+query+"\nparams:\n"+params);
        try {
            DC.noSelect(query,params);
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
        categoryID=i;
    }
}

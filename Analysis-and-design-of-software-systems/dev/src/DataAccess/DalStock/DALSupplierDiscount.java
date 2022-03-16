package DataAccess.DalStock;

import DataAccess.DalController;
import Utility.Tuple;

import java.util.List;

public class DALSupplierDiscount extends DALDiscount{
    private int _supplierID;

    public DALSupplierDiscount(){
        super(null);
    }


    public DALSupplierDiscount(Integer storeID, Integer id, Integer typeID, Integer category, Integer supplierID,
                               Double percent, String startDate, String endDate, DalController dc){
        super(dc,storeID,id,typeID,percent,startDate,endDate);
        _supplierID=supplierID;
    } // get supplier id from controller

    @Override
    public String getCreate() {
    return super.getCreate();
    }


    public int getSupplier(){
        return _supplierID;
    }
    public void setSupplier(int sup){
        String query= """
                UPDATE Discount \s
                SET supplierID=?
                WHERE storeID=? AND discountID=?;""";
        List<Tuple<Object,Class>> params=prepareList(sup,storeID,_discountID);
        try {
            DC.noSelect(query,params);
        }
        catch (Exception e){
            throw new IllegalArgumentException("fail");
        }
        _supplierID=sup;
    }


}

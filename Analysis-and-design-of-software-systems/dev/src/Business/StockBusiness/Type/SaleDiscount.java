package Business.StockBusiness.Type;

import DataAccess.DalStock.DALSaleDiscount;
import DataAccess.SMapper;
import Utility.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaleDiscount extends Discount{
    DALSaleDiscount dal;

//    public SaleDiscount(int storeID,int _discountID, double _percent, Date _start, Date _end) {
//        super(storeID,_discountID,0,0,0, _percent, _start, _end);
//    }
    public SaleDiscount(int storeID,int _discountID,Integer typeID,Integer category, double _percent, Date _start, Date _end) {
        super(storeID,_discountID,typeID,category,0, _percent, _start, _end);
    }
    public int getCategoryID(){
        return dal.getCategoryID();
    }

    public SaleDiscount(int id, Integer i) {
        List<Integer> list=new ArrayList<>();
        list.add(id);
        list.add(i);
       // list.add(-1);
        dal=(DALSaleDiscount) SMapper.getMap().getItem(DALSaleDiscount.class,list);
    }

    @Override
    protected void init(int storeID, int id,Integer typeID ,Integer categoryID,Integer sup, double percent, String start, String end) {
        dal=Util.initDal(DALSaleDiscount.class,storeID,id,typeID,categoryID,sup,percent,start,end);
    }

    @Override
    public int get_discountID() {
        return dal.getID();
    }

    @Override
    public double get_percent() {
        return dal.getPercent();
    }

    @Override
    public Date get_start() {
        return dal.getStartDate();
    }

    @Override
    public Date get_end() {
        return dal.getEndDate();
    }

    @Override
    public void addTo(ProductType productType) {
        log.info("addTo(ProductType productType)");
        productType.addDiscount(this);
    }

    @Override
    public void removeFrom(ProductType productType) {
        productType.removeDiscount(this);
    }



}

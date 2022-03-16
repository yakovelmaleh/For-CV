package Business.SupplierBusiness;

import DataAccess.DALObject;
import DataAccess.DalSuppliers.DalQuantityDocument;
import DataAccess.SMapper;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class QuantityDocument {
    DalQuantityDocument dalQuantityDocument;
    final static Logger log=Logger.getLogger(QuantityDocument.class);

    public QuantityDocument(int itemId, int minimalAmount , int discount){
        List<Tuple<Object,Class>> list=new ArrayList<>();
        list.add(new Tuple<>(itemId,Integer.class));
        list.add(new Tuple<>(minimalAmount,Integer.class));
        list.add(new Tuple<>(discount,Integer.class));
        SMapper map= SMapper.getMap();
        map.setItem(DalQuantityDocument.class,list);
        List<Integer> keyList=new ArrayList<>();
        keyList.add(itemId);
        DALObject check =map.getItem(DalQuantityDocument.class ,keyList);
        if (check==null ||(check.getClass()!=DalQuantityDocument.class)){
            String s="the instance that return from Mapper is null";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        else{
            log.info("create new Object");
            dalQuantityDocument = (DalQuantityDocument) check;
        }
    }

    public QuantityDocument(DalQuantityDocument dalQuantityDocument) {
        this.dalQuantityDocument = dalQuantityDocument;
    }

    public void updateMinimalAmountOfQD(int minimalAmount) throws Exception {
        if(minimalAmount < 0) throw new Exception("minimal amount must be a positive number");
        dalQuantityDocument.updateMinimalAmountOfQD(minimalAmount);
    }

    public void updateDiscountOfQD(int discount) throws Exception {
        if(discount < 0 || discount > 100) throw new Exception("discount amount must be a number between 0 to 100 ");
        dalQuantityDocument.updateDiscountOfQD(discount);
    }

    public int getMinimalAmount(){
        return dalQuantityDocument.getMinimalAmount();
    }

    public int getDiscount(){
        return dalQuantityDocument.getDiscount();
    }
}

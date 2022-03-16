package Business.SupplierBusiness;

import DataAccess.DALObject;
import DataAccess.DalSuppliers.DalSupplierAgreement;
import DataAccess.SMapper;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class SupplierAgreement {

    private DalSupplierAgreement dalSupplierAgreement;
    final static Logger log=Logger.getLogger(SupplierAgreement.class);

    public SupplierAgreement(int supplierBN, int minimalAmount , int discount , boolean constantTime , boolean shipToUs){
        List<Tuple<Object,Class>> list=new ArrayList<>();
        list.add(new Tuple<>(supplierBN,Integer.class));
        list.add(new Tuple<>(minimalAmount,Integer.class));
        list.add(new Tuple<>(discount,Integer.class));
        if (constantTime) {
            list.add(new Tuple<>(1,Integer.class));
        }
        else {
            list.add(new Tuple<>(0,Integer.class));
        }
        if (shipToUs) {
            list.add(new Tuple<>(1, Integer.class));
        }
        else {
            list.add(new Tuple<>(0, Integer.class));
        }
        SMapper map= SMapper.getMap();
        map.setItem(DalSupplierAgreement.class,list);
        List<Integer> keyList=new ArrayList<>();
        keyList.add(supplierBN);
        DALObject check =map.getItem(DalSupplierAgreement.class ,keyList);
        if (check==null ||(check.getClass()!=DalSupplierAgreement.class)){
            String s="the instance that return from Mapper is null";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        else{
            log.info("create new Object");
            dalSupplierAgreement = (DalSupplierAgreement) check;
        }
    }

    public SupplierAgreement(DalSupplierAgreement dalSupplierAgreement) {
        this.dalSupplierAgreement = dalSupplierAgreement;
    }

    public void updateMinimalAmountOfSA(int minimalAmount) throws Exception {
        if(minimalAmount < 0) throw new Exception("minimal amount must be a positive number");
        dalSupplierAgreement.updateMinimalAmountOfSA(minimalAmount);
    }

    public void updateDiscountOfSA(int discount) throws Exception {
        if(discount < 0 || discount > 100) throw new Exception("discount amount must be a number between 0 to 100");
        dalSupplierAgreement.updateDiscountOfSA(discount);
    }

    public void updateConstantTime(boolean constantTime) throws Exception { dalSupplierAgreement.updateConstantTime(constantTime); }

    public void updateShipToUs(boolean shipToUs) throws Exception {
        dalSupplierAgreement.updateShipToUs(shipToUs);
    }

    public int getMinimalAmount() {
        return dalSupplierAgreement.getMinimalAmount();
    }

    public int getDiscount() {
        return dalSupplierAgreement.getDiscount();
    }

    public boolean getConstantTime() {
        return dalSupplierAgreement.getConstantTime();
    }

    public boolean getShipToUs() {
        return dalSupplierAgreement.getShipToUs();
    }
}

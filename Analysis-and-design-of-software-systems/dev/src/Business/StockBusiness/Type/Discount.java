package Business.StockBusiness.Type;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Discount {

    final static Logger log=Logger.getLogger(Discount.class);


    public Discount(int storeID,int id,Integer typeID,Integer categoryID,int sup,double percent,Date start,Date end){
        checkValue(id,percent);
        log.warn("public Discount1");
        if (end.before(start) || end.before(new Date(System.currentTimeMillis()))){
            String s="the Date is illegal";
            log.warn(s);
            throw new IllegalArgumentException(s);

        }
        String pattern="dd-MM-yyyy";
        DateFormat df=new SimpleDateFormat(pattern);
        String strStart=df.format(start);
        String strEnd=df.format(end);
        log.warn("public Discount2");
        init(storeID,id,(typeID==0)? null:typeID,(categoryID==0)? null:categoryID,(sup==0)? null:sup,percent,strStart,strEnd);
    }
    public Discount(){
    }
    protected abstract void init(int storeID, int id,Integer typeID,Integer categoryID,Integer sup, double percent,String start,String end);

    private void checkValue(Object... o){
        String s="the value of arg is illegal";;
        for (int i = 0; i < o.length; i++) {
            if (o[i] instanceof Integer && (Integer)o[i]<1)
            {
                log.debug(s);
                throw new IllegalArgumentException(s);
            }
            if (o[i] instanceof Float && ((float)o[i]<0 | (float)o[i]>=1)) {
                log.debug(s);
                throw new IllegalArgumentException(s);
            }
        }
    }



    public abstract int get_discountID();

    public abstract double get_percent();

    public abstract Date get_start();

    public abstract Date get_end();

    public abstract void addTo(ProductType productType);

    public abstract void removeFrom(ProductType productType);

    public boolean relevant() {
        Date today=new Date();
        return today.before(get_end()) && today.after(get_start());
    }
}

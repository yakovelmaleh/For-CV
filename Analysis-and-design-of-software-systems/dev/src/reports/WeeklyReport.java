package reports;

import Utility.Tuple;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.List;

public class WeeklyReport implements Report{
    private Dictionary<Integer, Dictionary<Integer, Tuple<Integer,Boolean>>> _list;
    private int _storeID;
    private Date _date=new Date(System.currentTimeMillis());
    private final String TYPE="WeeklyReport";
    final static Logger log=Logger.getLogger(WeeklyReport.class);
    public WeeklyReport(int id,Dictionary<Integer, Dictionary<Integer, Tuple<Integer,Boolean>>> output) {
        if (output==null || id<1)
        {
            String s=String.format("the values of ? is illegal",TYPE);
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        _list=output;
        _storeID=id;
    }

    @Override
    public String getType() {
        log.debug("getType()");
        return TYPE;
    }

    @Override
    public int getStore() {
        log.debug("getStore()");
        return _storeID;
    }

    @Override
    public Date getDate() {
        log.debug("getDate()");
        return _date;
    }

    @Override
    public int sizeOfList() {
        return _list.size();
    }

    @Override
    public String toString(){
        List<Integer> ids= Collections.list(_list.keys());
        String out="";
        for(int i:ids){
            out+="\nproducts for product type number: "+i+"\n";
            List<Integer> pids=Collections.list(_list.get(i).keys());
            for(int j:pids){
                out+="product "+j+" is in "+(_list.get(i).get(j).item2==true?"storage":"the store")+" on shelf number "+_list.get(i).get(j).item1+"\n";
            }
        }
        return out;
    }
}

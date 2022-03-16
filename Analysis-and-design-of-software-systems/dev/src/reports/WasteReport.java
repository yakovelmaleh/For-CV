package reports;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

public class WasteReport implements Report{
    private List<Integer> _list;
    private int _storeID;
    private Date _date=new Date(System.currentTimeMillis());
    private final String TYPE="WasteReport";
    final static Logger log=Logger.getLogger(WasteReport.class);
    public WasteReport(int id,List<Integer> list) {
        if (list==null || id<1)
        {
            String s=String.format("the values of ? is illegal",TYPE);
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        _storeID=id;
        _list=list;
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
        return _list.toString();
    }
}

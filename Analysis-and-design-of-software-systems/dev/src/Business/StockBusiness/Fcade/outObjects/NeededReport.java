package Business.StockBusiness.Fcade.outObjects;

import java.util.Date;
import java.util.Dictionary;

public class NeededReport {
    int storeID;
    Date date;
    private Dictionary<Integer, Integer> _list;

    public NeededReport(int storeID, Date date, Dictionary<Integer, Integer> _list) {
        this.storeID = storeID;
        this.date = date;
        this._list = _list;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Dictionary<Integer, Integer> get_list() {
        return _list;
    }

    public void set_list(Dictionary<Integer, Integer> _list) {
        this._list = _list;
    }
}

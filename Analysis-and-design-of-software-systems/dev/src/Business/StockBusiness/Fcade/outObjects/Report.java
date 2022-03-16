package Business.StockBusiness.Fcade.outObjects;

import java.util.Date;

public class Report {
    int store;
    Date date;
    String text;
    String type;

    public Report(int store, Date date, String text, String type) {
        this.store = store;
        this.date = date;
        this.text = text;
        this.type = type;
    }

    @Override
    public String toString() {
        return ""+type+" report:\nGenerated on: "+date+"\nFrom store: "+store+"\n"+text;
    }
}

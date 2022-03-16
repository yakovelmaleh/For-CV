package Business.StockBusiness.Fcade.outObjects;

import java.util.Date;

public class SaleDiscount {
    int dicountID;

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public double getPrecent() {
        return precent;
    }

    Date start;
    Date end;
    double precent;


    public SaleDiscount(int dicountID, Date start, Date end, double precent) {
        this.dicountID = dicountID;
        this.start = start;
        this.end = end;
        this.precent = precent;
    }

    @Override
    public String toString() {
        return "Discount ID: "+dicountID+"\nDiscount is: "+precent+"%\nStarts on: "+start+"\nEnds on: "+end+"\n";
    }

}

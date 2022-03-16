package Business.StockBusiness.Fcade.outObjects;

import java.util.Date;

public class SupplierDiscount {
    int dicountID;
    Date start;
    Date end;
    double precent;
    int supplier;

    public SupplierDiscount(int dicountID, Date start, Date end, double precent, int supplier) {
        this.dicountID = dicountID;
        this.start = start;
        this.end = end;
        this.precent = precent;
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return "Discount ID: "+dicountID+"\nDiscount is: "+precent+"%\nStarts on: "+start+"\nEnds on: "+end+"\nSupplier: "+supplier+"\n";
    }
}

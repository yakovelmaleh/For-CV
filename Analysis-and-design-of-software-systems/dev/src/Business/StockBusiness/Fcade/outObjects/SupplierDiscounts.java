package Business.StockBusiness.Fcade.outObjects;

import java.util.List;

public class SupplierDiscounts {
    int productType;
    List<SupplierDiscount> discounts;

    public SupplierDiscounts(int productType, List<SupplierDiscount> discounts) {
        this.productType = productType;
        this.discounts = discounts;
    }

    @Override
    public String toString() {
        return "All supplier discounts for product type: "+productType+"\n"+discounts.toString();
    }
    public int size(){
        return discounts.size();
    }
}

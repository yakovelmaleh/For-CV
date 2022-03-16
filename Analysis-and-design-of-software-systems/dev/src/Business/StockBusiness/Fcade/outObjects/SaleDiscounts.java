package Business.StockBusiness.Fcade.outObjects;

import java.util.List;

public class SaleDiscounts {
    int productType;

    public List<SaleDiscount> getDiscounts() {
        return discounts;
    }

    List<SaleDiscount> discounts;

    public SaleDiscounts(int productType, List<SaleDiscount> discounts) {
        this.productType = productType;
        this.discounts = discounts;
    }

    @Override
    public String toString() {
        return "All sale discounts for product type: "+productType+"\n"+discounts.toString();
    }

    public int size() {
        return discounts.size();
    }
}

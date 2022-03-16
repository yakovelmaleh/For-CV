package Business.SupplierBusiness.facade.outObjects;

import java.time.LocalDate;

public class Item {
    private final int ItemId;
    private final String name;
    private final double price;
    private final LocalDate expirationDate;

    public Item(Business.SupplierBusiness.Item item) {
        ItemId = item.getItemId();
        name = item.getName();
        price = item.getPrice();
        expirationDate = item.getExpirationDate();
    }

    public String toString(boolean shift) {
        if(shift)
            return "\tItem: \n" +
                "\t\tItemId: " + ItemId + "\n" +
                "\t\tname: " + name +"\n" +
                "\t\tprice: " + price + "\n" +
                "\t\texpiration date: " + expirationDate;
        return "Item: \n" +
                "\tItemId: " + ItemId + "\n" +
                "\tname: " + name + "\n" +
                "\tprice: " + price + "\n" +
                "\texpiration date: " + expirationDate;
    }

    public String toString(String amount){
        return "\tItem: \n" +
                "\t\tItemId: " + ItemId + "\n" +
                "\t\tAmount: " + amount + "\n" +
                "\t\tname: " + name +"\n" +
                "\t\tprice: " + price + "\n" +
                "\t\texpiration date: " + expirationDate;
    }

    public String toStringId(){
        return "" +  ItemId;
    }
}

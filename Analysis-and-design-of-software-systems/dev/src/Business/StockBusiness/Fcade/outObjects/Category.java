package Business.StockBusiness.Fcade.outObjects;

import java.util.List;

public class Category {
    int categoryID;
    int superID;
    String name;
    List<Integer> childIDs;
    List<Integer> products;

    public Category(int categoryID, int superID, String name, List<Integer> childIDs, List<Integer> products) {
        this.categoryID = categoryID;
        this.superID = superID;
        this.name = name;
        this.childIDs = childIDs;
        this.products = products;
    }

    @Override
    public String toString() {
        return "Category ID: "+categoryID+"\nCategory name: "+name+"\nIs sub-category of: "+(superID==0?"None":superID)+
                "\nIDs of child categories: "+childIDs.toString()+"\nIDs of item types in category: "+products+"\n\n";
    }
}

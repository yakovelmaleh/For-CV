package Business.StockBusiness.Fcade.outObjects;

import java.util.List;

public class AllType {
    List<Integer> typeIDs;
    public AllType(List<Integer> IDs){
        typeIDs=IDs;
    }

    @Override
    public String toString() {
        return "The IDs of all item types currently in storage: "+typeIDs.toString()+"\n";
    }
    public List<Integer> getData(){
        return typeIDs;
    }
    public int size(){
        return typeIDs.size();
    }
}

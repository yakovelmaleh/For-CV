package Business.SupplierBusiness.facade.outObjects;

import Business.ApplicationFacade.outObjects.BranchServiceDTO;
import Business.Transportation.TransportationService;

import java.util.Hashtable;

public class Order {
    private final int orderId;
    private final int supplierBN;
    private final double totalAmount;
    private final int branchId;
    private final Hashtable<Integer, Integer> items;
    private final int transportationId;
    private final boolean isArrived;

    public Order(Business.SupplierBusiness.Order order) {
        orderId = order.getOrderId();
        supplierBN = order.getSupplierBN();
        totalAmount = order.getTotalAmount();
        branchId = order.getBranchID();
        items = order.getAmounts();
        transportationId = order.getTransportationId();
        isArrived = order.isArrived();
    }

    public String toString() {
        TransportationService tra=new TransportationService(null);
        try {
            return "Order: \n" +
                    "\torderId: " + orderId + "\n" +
                    "\tsupplierBN: " + supplierBN + "\n" +
                    "\ttotal amount: " + totalAmount + "\n" +
                    "\tbranch: " + new BranchServiceDTO(tra.getBranchById(branchId));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String toStringId() {
        return "" + orderId;
    }

    public String toStringTotalAmount() {
        return totalAmount + "";
    }

    public String toStringAmount(String itemId) {
        return "" + items.get(Integer.parseInt(itemId));
    }

    public String toStringSupplierBN() { return "" + supplierBN; }

    public int getTransportationId(){ return  transportationId;}

    public Hashtable<Integer, Integer> getItems() {
        return items;
    }

    public int getBranchId() {
        return branchId;
    }

    public boolean getIsArrived() { return isArrived; }

    public int getOrderId() {
        return orderId;
    }

    public int getSupplierBN() {
        return supplierBN;
    }
}

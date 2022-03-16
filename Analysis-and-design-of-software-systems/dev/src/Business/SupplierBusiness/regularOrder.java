package Business.SupplierBusiness;

import DataAccess.DalSuppliers.DalOrder;

public class regularOrder extends Order {

    public regularOrder(int supplierBN, int orderId, int branchId) {
        super(supplierBN, orderId,  branchId, 0);
    }

    public regularOrder(DalOrder dalOrder) {
        super(dalOrder);
    }

    public void addItemToOrder(Item item, int amount) throws Exception {
        if (amount < 1) throw new Exception("amount must be at least 1");
        if (items.get(item) != null) {
            dalOrder.updateAmountOfOrder(item.getItemId(), items.get(item) + amount);
            items.put(item, items.get(item) + amount);
            //dalOrder.addItemToOrder(item.getItemId(), items.get(item) + amount);
        } else {
            items.put(item, amount);
            dalOrder.addItemToOrder(item.getItemId(), amount);
        }
        if (item.getQuantityDocument() == null) updateTotalAmount(dalOrder.getTotalAmount() + item.getPrice() * amount);
        else updateTotalAmount(item, amount);
        updateTotalWeight(item, amount);
    }

    public void updateTotalAmount(double totalAmount) {
        dalOrder.updateTotalAmount(totalAmount);
    }

    public void updateBranchId(int branchID) {
        dalOrder.updateBranchId(branchID);
    }

    private void updateTotalAmount(Item item, int amount) throws Exception {
        QuantityDocument qd = item.getQuantityDocument();
        updateTotalAmount(dalOrder.getTotalAmount() + item.getPrice() * amount);
        if (qd.getMinimalAmount() <= items.get(item)) {
            double discount = qd.getDiscount() / 100.0;
            updateTotalAmount(dalOrder.getTotalAmount() - item.getPrice() * discount * amount);
        }
    }

    private void updateTotalWeight(Item item, int amount) throws Exception {
        dalOrder.updateTotalWeight(dalOrder.getTotalWeight() + item.getWeight() * amount);
    }

    public void removeItemFromRegularOrder(int itemId) throws Exception {
        for (Item item : items.keySet()) {
            if (item.getItemId() == itemId) {
                dalOrder.updateTotalAmount(dalOrder.getTotalAmount() - items.get(item) * item.getPrice());
                dalOrder.updateTotalWeight(dalOrder.getTotalWeight() - items.get(item) * item.getWeight());
                items.remove(item);
                dalOrder.removeItemFromOrder(itemId);
                break;
            }
        }
    }

    public void removeAmountItemFromRegularOrder(int itemId, int amount) throws Exception { ////I Think it need to be modified
        for (Item item : items.keySet()) {
            if (item.getItemId() == itemId) {
                int newAmount = items.get(item) - amount;
                if (newAmount < 0) throw new Exception("amount must be less then the amount of the item in the order");
                if (newAmount == 0) items.remove(item);
                else items.put(item, items.get(item) - amount);
                break;
            }
        }
    }
}

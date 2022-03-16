/*package BusinessLayer.SupplierBusiness;

import BusinessLayer.SupplierBusiness.facade.Tresponse;
import BusinessLayer.SupplierBusiness.facade.outObjects.Order;
import BusinessLayer.SupplierBusiness.facade.response;

import java.time.LocalDate;
import java.util.Hashtable;
import java.util.List;

public interface IOrderService {
    response LoadData();
    Tresponse<Order> addRegularOrder(int supplierBn , int branchId, Hashtable<Integer, Integer> items);
    response addNeededOrder(int typeID, int neededAmount, int branchID);
    response addItemToOrder(int supplierBN , int orderId , int itemId , int amount);
    response removeOrder(int supplierBN , int orderId);
    Tresponse<Order> showOrderOfSupplier(int supplierBN , int orderId);
    Tresponse<List<Order>> showAllOrdersOfSupplier(int supplierBN);
    response showTotalAmount(int supplierBN , int orderId) throws Exception;
    response showDeliverTime(int supplierBN , int orderId);
    response updateDeliverTime(int supplierBN , int orderId , LocalDate deliverTime);
    Tresponse<Order> getOrder(int orderId);
    Tresponse<List<Order>> getOrdersByTransportation(int transportationID);
    Tresponse<Order> addOrderToTransportation(BusinessLayer.SupplierBusiness.Order Order);

    response removeAmountItemFromRegularOrder(int branchId, int orderId, int itemId, int amount);
}

 */

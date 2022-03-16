package Business.StockBusiness.Fcade;

import Business.ApplicationFacade.outObjects.TransportationServiceDTO;
import Business.StockBusiness.Fcade.outObjects.*;
import Business.SupplierBusiness.facade.SupplierService;
import Business.SupplierBusiness.facade.Tresponse;
import Business.SupplierBusiness.facade.response;
import Utility.Tuple;

import java.util.Date;
import java.util.Dictionary;
import java.util.List;

public interface iStorageService {
    public Tresponse<Report> getWeeklyReport();
    // need List format for CLI
    public Tresponse<Report> getWeeklyReport(List<Integer> c);

    public Tresponse<Report> getNeededReport();
    public Tresponse<NeededReport> getNeededReportToOrder();
    public Tresponse<Report> getWasteReport();
    public response addCategory(String name);
    public response addCategory(String name, int superCategory);
    public Tresponse<Categories> getCategories();
    public Tresponse<Category> getCategoryInfo(int id);
    public response editCategory(int Id, String name, int superCategory);
    public response editCategory(int Id, String name);
    //added sale price
    public response addProductType(String name, int minAmount, double basePrice, double salePrice, String producer, int supID, int category);
    public Tresponse<AllType> getProductTypes();
    public Tresponse<ProductType> getProductTypeInfo(int id);
    //added sale price + ID
    public response editProductType(int ID,String name, int minAmount, double basePrice, double salePrice, String producer, int supID, int category);

    public response addSaleProductDiscount(int productTypeID, double percent, Date start,Date end);
    public response addSaleCategoryDiscount(int catID, double percent, Date start,Date end);
    public response addSupplierDiscount(int typeID, double percent, Date start,Date end,int supId);
    public Tresponse<List<Integer>> getProductsByType(int typeID);
    public response addProduct(int typeID, Date expiration );
    public response removeProduct(int ID);
    public response reportDamage(int ID);
    public Tresponse<Product> getProductInfo(int ID);
    public Tresponse<Integer> getShelvesAmount(int typeID);
    public Tresponse<Integer> getStorageAmount(int typeID);
    public Tresponse<SupplierDiscounts> getSupplierDiscounts(int typeID);
    public Tresponse<SaleDiscounts> getSaleDiscounts(int typeID);
    public response relocateProduct(int ID, boolean toStorage, int targetShelf);
    public Tresponse<Integer> addStore(int ID);
    public Tresponse<List<Integer>> getStores();
    public response useStore(int ID, SupplierService ss);

    public  List<Tuple<Integer,Dictionary<Integer,Integer>>>  acceptTrans(TransportationServiceDTO acceptT);
}

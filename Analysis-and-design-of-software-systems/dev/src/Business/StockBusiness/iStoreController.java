package Business.StockBusiness;


import Business.StockBusiness.Type.Category;
import Business.StockBusiness.Type.ProductType;
import Business.StockBusiness.Type.SaleDiscount;
import Business.StockBusiness.Type.SupplierDiscount;
import Business.StockBusiness.instance.InstanceController;
import Business.StockBusiness.instance.Product;
import Business.StockBusiness.instance.Shelf;
import reports.Report;

import java.util.Date;
import java.util.Dictionary;
import java.util.List;

public interface iStoreController {
    //res: SC
    public int getID();
    public Report getWeeklyReport();
    public Report getWeeklyReport(List<Integer> c);
    public Report getNeededReport();
    public Report getWasteReport();
    public void setList(Dictionary<ProductType, InstanceController> dictionary);//for testing

    public int counterCategory();
    public boolean containCategory(Category c);//for testing
    public boolean containProductType(String c);//for testing
    public void setCategories(Dictionary<Integer,Category> dic);
    public Category getCategory(int catID);
    public Category addCategory(String name, int superCategory);
    public List<Integer> getCategories();
    //added sale price
    public void addProductType(String name, int minAmount, double basePrice,double salePrice, String producer, int supID, int category);

    public List<Integer> getProductTypes();
    public ProductType getProductTypeInfo(int id);
    public int getShelvesAmount(int typeID);
    public int shelvesAmountExist();
    public int storageAmountExist();
    public int getStorageAmount(int typeID);
    public List<Integer> getProductByType(int typeID);
    //res: Category
    public Category addCategory(String name);
    //res:PT+SC
    public void addSaleProductDiscount(int productTypeID, double percent, Date start, Date end);
    public int counterDiscount();
    public void addSaleCategoryDiscount(int productTypeID, double percent, Date start,Date end);
    public void addSupplierDiscount(int categoryID, double percent, Date start,Date end,int supId);
    public List<SupplierDiscount> getSupplierDiscounts(int typeID);
    public List<SaleDiscount> getSaleDiscounts(int typeID);

    //res:Category
    public void editCategory(int Id, String name, int superCategory) throws Exception;
    public void editCategory(int Id, String name) throws Exception;
    //res:PT

    //added sale price
    public void editProductType(int id,String name, int minAmount, double basePrice, double salePrice, String producer, int supID, int category);

    //res: InstanceController+PT
    public void addProduct(int typeID, Date expiration ) throws Exception;
    public void removeProduct(int ID);
    public void reportDamage(int ID);
    public Product getProductInfo(int ID);
    public void relocateProduct(int ID, boolean toStorage, int targetShelf);
    public void setShelves(List<Shelf> list);


    int getProductTypeByName(String name);

    void removeSupplier(int itemId, int supplierId);

    int getTypeID(int id);
}

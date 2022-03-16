package Business.StockBusiness;

import Business.StockBusiness.Type.Category;
import Business.StockBusiness.Type.ProductType;
import Business.StockBusiness.Type.SaleDiscount;
import Business.StockBusiness.Type.SupplierDiscount;
import Business.StockBusiness.instance.InstanceController;
import Business.StockBusiness.instance.Location;
import Business.StockBusiness.instance.Product;
import Business.StockBusiness.instance.Shelf;
import DataAccess.DALObject;
import DataAccess.DalStock.DALStoreController;
import DataAccess.SMapper;
import Utility.Tuple;
import org.apache.log4j.Logger;
import reports.NeededReport;
import reports.Report;
import reports.WasteReport;
import reports.WeeklyReport;

import java.util.*;
import java.util.stream.Collectors;

public class StoreController implements iStoreController {
    DALStoreController dal;

    static public int MAX_PRODUCTS_ON_PROTUCTTYPE=1000;
    private List<Shelf> _shelves=new ArrayList<>();
    private Dictionary<ProductType,InstanceController> _products=new Hashtable<>();
    private Dictionary<Integer,Category> _category=new Hashtable<>();
    private Dictionary<Integer,SaleDiscount> saleCategoryDiscount=new Hashtable<>();
    final static Logger log=Logger.getLogger(StoreController.class);

    public List<SaleDiscount> getSaleCategoryDiscounts(int cat){
        List<SaleDiscount> ret = new ArrayList<>();
        for(SaleDiscount sd: Collections.list(saleCategoryDiscount.elements())){
            if(sd.getCategoryID()==cat) ret.add(sd);
        }
        Category c=getCategory(cat);
        if(c.get_superCategory()!=null && c.get_superCategory().get_categoryID()>0) ret.addAll(getSaleCategoryDiscounts(c.get_superCategory().get_categoryID()));
        return ret;
    }

    public StoreController(int storeID,int shelves,int storeSelves,int maxProductsInShelf){
        String error;
        //dal=Util.initDal(DALStoreController.class,storeID,storeSelves,shelves,0,0,0,maxProductsInShelf);
        List<Tuple<Object,Class>> list=new ArrayList<>();
        list.add(new Tuple<>(storeID,Integer.class));
        list.add(new Tuple<>(storeSelves,Integer.class));
        list.add(new Tuple<>(shelves,Integer.class));
        list.add(new Tuple<>(0,Integer.class));
        list.add(new Tuple<>(0,Integer.class));
        list.add(new Tuple<>(counterCategory(),Integer.class));
        SMapper map= SMapper.getMap();
        map.setItem(DALStoreController.class,list);
        List<Integer> keyList=new ArrayList<>();
        keyList.add(storeID);
        DALObject check =map.getItem(DALStoreController.class ,keyList);
        if (check==null ||(check.getClass()!=DALStoreController.class)){
            String s="the instance that return from Mapper is null";
            log.warn(s);
            throw new IllegalArgumentException(s);

        }
        else{
            log.info("create new Object");
        }
        dal=(DALStoreController) check;
        ////////////////////////////////
        if (storeSelves>shelves) {
            error="the number of store shelves greater then the number of all shelves.";
            log.warn(error);
            throw new IllegalArgumentException(error);
        }
        for (int i=1;i<=storeSelves; i++)
            _shelves.add(new Shelf(storeID,i,0,maxProductsInShelf));
        for (int i=storeSelves+1;i<=shelves; i++)
            _shelves.add(new Shelf(storeID,i,1,maxProductsInShelf));
    }
    public StoreController(){//for testing
//        _storeID=1;
//        _numberOfShelves=10;
//        _storeShelves=5;
//        for (int i=1;i<=_storeShelves; i++)
//            _shelves.add(new Shelf(i,Location.Storage,1000));
//        for (int i=_storeShelves+1;i<=_numberOfShelves; i++)
//            _shelves.add(new Shelf(i,Location.Storage,1000));
    }
    public StoreController(int storeID){
        List<Integer> list=new ArrayList<>();
        list.add(storeID);
        dal=(DALStoreController) SMapper.getMap().getItem(DALStoreController.class,list);
        if (dal==null)
        {
            log.warn("storeID is not in DB");
            throw new IllegalArgumentException("storeID is not in DB");
        }

        loadSaleCategoryDiscount();
        loadCategories();
        loadTypeProducts();
        loadShelves();



    }
    public void loadCategories(){
        List<Integer> list=dal.getCategories();
        List<Category> categories=new ArrayList<>();
        Category c;
        for (Integer i: list) {
            c=new Category(getID(), i);
            categories.add(c);
            _category.put(i,c);
        }
        for (Category cat: categories)
            cat.initCategory(categories);
    }
    public void loadSaleCategoryDiscount(){
        List<Integer> list=dal.getSaleCategoryDiscount();
        for (Integer i:list)
            saleCategoryDiscount.put(i,new SaleDiscount(getID(),i));
    }
    public void loadTypeProducts(){
        List<Integer> list=dal.getTypes();
        log.warn(list);
        for (Integer i: list){
            ProductType p_i=new ProductType(getID(),i);
            InstanceController ic_i=new InstanceController(getID(),i);
            _products.put(p_i,ic_i);
            p_i.initProducts(ic_i.getProducts());
        }
        log.warn(_products);
        for (ProductType p: Collections.list(_products.keys()))
        {
            Category c=_category.get(p.get_categoryID());
            List<Integer> discounts=new ArrayList<>();
            List<SaleDiscount> obj=new ArrayList<>();
            c.addAllDiscountCategory(discounts);
            for (SaleDiscount s: Collections.list(saleCategoryDiscount.elements()))
                if(discounts.contains(s.get_discountID()))
                    obj.add(s);
            p.initSaleDiscount(obj);
        }
    }
    public void loadShelves(){
        List<Integer> list=dal.getListShelves();
        for (Integer i: list){
            _shelves.add(new Shelf(getID(),i));
        }
    }

    public static int getMaxProdOnType() {
        return MAX_PRODUCTS_ON_PROTUCTTYPE;
    }

    @Override
    public int getID() {
        log.debug("got inside getID Method");
        return dal.getStoreID();
    }

    @Override
    public Report getWeeklyReport() {
        log.debug("got inside getWeeklyReport() Method.");
        Dictionary<Integer,Dictionary<Integer, Tuple<Integer,Boolean>>> output=new Hashtable<>();
        List<ProductType> pt=Collections.list(_products.keys());
        for (ProductType p: pt){
            output.put(p.get_typeID(), _products.get(p).getWeeklyReport());

        }
        return new WeeklyReport(getID(),output);
    }

    @Override
    public Report getWeeklyReport(List<Integer> c) {
        log.debug("got inside getWeeklyReport(int... c) Method.");
        Dictionary<Integer,Dictionary<Integer,Tuple<Integer,Boolean>>> output=new Hashtable<>();
        for (int i=0; i<c.size(); i++ )
        {
            checkValidCategory(c.get(i));
            weeklyCatRec(c.get(i),output);
        }
        return new WeeklyReport(getID(),output);
    }
    private void weeklyCatRec(int cid, Dictionary<Integer,Dictionary<Integer,Tuple<Integer,Boolean>>> out){
        for(int j:_category.get(cid).get_productTypes()) {
            ProductType p = checkIDProductTypeExist(j);
            out.put(p.get_typeID(), _products.get(p).getWeeklyReport());
        }
        for(Category cat: _category.get(cid).get_categories()){
            weeklyCatRec(cat.get_categoryID(),out);
        }
    }


    @Override
    public Report getNeededReport() {
        log.debug("got inside getNeededReport() Method.");
        Dictionary<Integer,Integer> output=new Hashtable<>();
        for (Enumeration<ProductType> pt=_products.keys(); pt.hasMoreElements();)
        {
            ProductType p=pt.nextElement();
            if (p.getNeededReport()>0)
                output.put(p.get_typeID(),p.getNeededReport());
        }
        return new NeededReport(getID(),output);
    }

    @Override
    public Report getWasteReport() {
        log.debug("got inside getWasteReport() Method.");
        List<Integer> list=new ArrayList<>();
        List<InstanceController> ic= Collections.list(_products.elements());
        for (int i = 0; i < ic.size(); i++) {
            ic.get(i).getWasteReport(list);
        }
        return new WasteReport(getID(),list);
    }

    @Override
    public void setList(Dictionary<ProductType, InstanceController> dictionary) {
        log.debug("setList(Dictionary<ProductType, InstanceController> dictionary)");
        _products=dictionary;
    }

    @Override
    public int counterCategory() {
        log.debug("got inside counterCategory Method");
        return _category.size();
    }

    @Override
    public boolean containCategory(Category c) {
        return Collections.list(_category.elements()).contains(c);
    }

    @Override
    public boolean containProductType(String c) {
        for (Enumeration<ProductType> pt=_products.keys();pt.hasMoreElements();)
            if (pt.nextElement().get_name().equals(c))
            {
                return true;
            }
        return false;
    }

    @Override
    public void setCategories(Dictionary<Integer, Category> dic) {
        _category=dic;
    }

    @Override
    public Category getCategory(int catID) {
        log.debug("got inside getCategory(int catID) Method with: "+catID);
        return _category.get(catID);
    }

    @Override
    public Category addCategory(String name, int superCategory) {
        log.debug("got inside addCategory(String name, int superCategory) Method with: " + name + "," + superCategory);
        checkValidCategory(superCategory);
        checkValidNameCategory(name);
        Category tmp = _category.get(superCategory);
        dal.setCategoryCounter(dal.categoryCounter()+1);
        int catID =dal.categoryCounter();
        Category output;
        try {
            output = new Category(getID(),catID, name, tmp);
        }
        catch (Exception e){
            dal.setCategoryCounter(dal.categoryCounter()-1);
            throw e;
        }
        _category.put(catID,output);
        try {
            tmp.addCategory(output);

        } catch (Exception e) {
            dal.setCategoryCounter(dal.categoryCounter()-1);
            _category.remove(catID);
            throw e;
        }
        log.info(String.format("new Category '%s' added the Store #%d",name,getID()));
        return output;
    }


    @Override
    public List<Integer> getCategories() {
        log.debug("got inside getCategories() Method.");
        List<Category> list= Collections.list(_category.elements());
         return list.stream().map(Category::get_categoryID).collect(Collectors.toList());
    }

    @Override
    public void addProductType(String name, int minAmount, double basePrice, double salePrice, String producer, int supID, int category) {
        log.debug(String.format("addProductType(String name, int minAmount, float basePrice, float salePrice, String producer, int supID, int category)" +
                        " Method  with: %s, %d, %f, %f, %s, %d, %d",
                name,minAmount,basePrice,salePrice,producer,supID,category));
        checkValidNameProductType(name);
        checkValidCategory(category);
        Category cat=_category.get(category);
        List<Integer> discounts= new ArrayList<>();
        cat.addAllDiscountCategory(discounts);
        dal.setTypeCounter(dal.getTypeCounter()+1);
        int typeID=dal.getTypeCounter();
        ProductType newProductType=new ProductType(getID(),typeID,name,minAmount,basePrice,salePrice,producer,supID,category);
        try {
            cat.addProductType(typeID);
            for (Integer i: discounts){
                newProductType.addDiscount(saleCategoryDiscount.get(i));
            }
        }
        catch (Exception e){
            dal.setTypeCounter(dal.getTypeCounter()-1);
        }
        _products.put(newProductType,new InstanceController(getID(),typeID));
        log.info(String.format("new ProductType %s added the Store #%d",name,getID()));
    }

    @Override
    public List<Integer> getProductTypes() {
        log.debug("got inside  getProductTypes() Method.");
        return Collections.list(_products.keys()).stream().map(ProductType::get_typeID).collect(Collectors.toList());
    }

    @Override
    public ProductType getProductTypeInfo(int id) {
        log.debug(String.format("got inside  getProductTypeInfo(int id) Method with:%d",id));
        return checkIDProductTypeExist(id);
    }

    @Override
    public int getShelvesAmount(int typeID) {
        log.debug(String.format("got inside  getShelvesAmount(int typeID) Method with:%d",typeID));
        int ret=0;
        for(Shelf s:_shelves){
            if(s.get_typeID()==typeID && s.get_location()==Location.Shelves) {
                ret+=s.get_cur();
                log.info("typeID: "+typeID+" shelfID: "+s.get_shelfID()+" Location: "+s.get_location()+", type: "+ s.get_typeID()+", cur: "+ s.get_cur());
            }
        }
        return ret;
    }

    @Override
    public int shelvesAmountExist() {
        log.debug("got inside  shelvesAmountExist() Method.");
        return (int)_shelves.stream().filter(x -> x.get_location() == Location.Shelves).count();
    }

    @Override
    public int storageAmountExist() {
        log.debug("got inside  storageAmountExist() Method.");
        return (int)_shelves.stream().filter(x -> x.get_location() == Location.Storage).count();
    }

    @Override
    public int getStorageAmount(int typeID) {
        int ret=0;
        for(Shelf s:_shelves){
            if(s.get_typeID()==typeID && s.get_location()==Location.Storage) {
                ret+=s.get_cur();
            }
        }
        return ret;
    }

    @Override
    public List<Integer> getProductByType(int typeID) {
        return  _products.get(checkIDProductTypeExist(typeID)).getProducts();
    }

    @Override
    public Category addCategory(String name) {
        log.debug(String.format("got inside addCategory(String name) Method with: %s",name));
        checkValidNameCategory(name);
        dal.setCategoryCounter(dal.categoryCounter()+1);
        int catId=dal.categoryCounter();
        log.debug(catId);
        try{
            Category c=new Category(getID(),catId,name);
            _category.put(catId,c);
            return c;
        }
        catch (Exception e)
        {
            dal.setCategoryCounter(dal.categoryCounter()-1);
            throw e;
        }
    }

    @Override
    public void addSaleProductDiscount(int productTypeID, double percent, Date start, Date end) {
        log.debug(String.format("got inside addSaleProductDiscount(int productTypeID, float percent, Date start, Date end)" +
                " Method with: %d, %f, "+start+" , "+end,productTypeID,percent));

        ProductType p=checkIDProductTypeExist(productTypeID);
        try {
            dal.set_discountCounter(dal.get_discountCounter()+1);
            p.addSaleProductDiscount(dal.get_discountCounter(), percent, start, end);
            log.warn("h3");
        }
        catch (Exception e){
            dal.set_discountCounter(dal.get_discountCounter()-1);
            log.warn("h4");
            throw e;
        }
    }

    @Override
    public int counterDiscount() {
        log.debug(String.format("got inside counterDiscount() Method."));
        return dal.get_discountCounter();
    }

    @Override
    public void addSaleCategoryDiscount(int catID, double percent, Date start, Date end) {
        log.debug(String.format("got inside addSaleCategoryDiscount(int CatID, float percent, Date start, Date end)" +
                " Method with: %d, %f, "+start+ ","+end,catID,percent));
        checkValidCategory(catID);
        Category c=_category.get(catID);
        List<Integer> list=c.getAllProductType();
        ProductType tmp;
        SaleDiscount discount=null;
        dal.set_discountCounter(dal.get_discountCounter()+1);
        int count=counterDiscount();
        try {
            discount = new SaleDiscount(getID(),count,0,catID, percent, start, end);
            c.addDiscount(count);
            for (int i=0; i<list.size(); i++){
                tmp=checkIDProductTypeExist(list.get(i));
                tmp.addDiscount(discount);
            }
            log.info(String.format("add new discount #%s to Category #%s",count,catID));
        }
        catch (Exception e){
            dal.set_discountCounter(dal.get_discountCounter()-1);
            c.removeDiscount(count);
            for (int i=0; i<list.size(); i++){
                tmp=checkIDProductTypeExist(list.get(i));
                tmp.removeDiscount(discount);
            }
            throw e;
        }
        saleCategoryDiscount.put(count,discount);
    }


    @Override
    public void addSupplierDiscount(int typeID, double percent, Date start, Date end, int supId) {
        log.debug(String.format("got inside addSupplierDiscount(int categoryID, float percent, Date start, Date end, int supId)" +
                " Method with: %d, %f,  "+start+" , "+end+" , %d",typeID,percent,supId));

        ProductType p=checkIDProductTypeExist(typeID);

        try {
            dal.set_discountCounter(counterDiscount()+1);
            p.addSupplierDiscount(counterDiscount(),percent,start,end,supId);
        }
        catch (Exception e){
            dal.set_discountCounter(counterDiscount()-1);
            throw e;
        }
    }

    @Override
    public List<SupplierDiscount> getSupplierDiscounts(int typeID) {
        log.debug(String.format("got inside getSupplierDiscounts(int typeID) Method with: %d",typeID));
        return checkIDProductTypeExist(typeID).getSupplierDiscounts();
    }

    @Override
    public List<SaleDiscount> getSaleDiscounts(int typeID) {
        log.debug(String.format("got inside getSaleDiscounts(int typeID) Method with: %d",typeID));
        ProductType pt=checkIDProductTypeExist(typeID);
        List<SaleDiscount> lsd=pt.getSalePrice();
        return lsd;
    }

    @Override
    public void editCategory(int Id, String name, int superCategory) throws Exception {
        log.debug(String.format("got inside editCategory(int Id, String name, int superCategory) Method with: %d, %s, %d",Id,name,superCategory));
        editCategoryInBL(Id,name,superCategory);
    }


    @Override
    public void editCategory(int Id, String name) throws Exception {
        log.debug(String.format("got inside editCategory(int Id, String name) Method with: %d, %s",Id,name));
        editCategoryInBL(Id,name);
    }

    @Override
    public void editProductType(int id, String name, int minAmount, double basePrice, double salePrice, String producer, int supID, int category) {
        log.debug(String.format("editProductType(int id, String name, int minAmount, float basePrice, float salePrice" +
                ", String producer, int supID, int category) Method with: "+id+" "+name+" "+minAmount+" "+basePrice+" "+producer+" "+supID+" "+category));
        checkValidCategory(category);
        ProductType pt=checkIDProductTypeExist(id);
        //checks

        Category lastCat=_category.get(pt.get_categoryID());
        List<Integer> saleIDToDelete=new ArrayList<>();
        lastCat.addAllDiscountCategory(saleIDToDelete);
        List<SaleDiscount> saleDiscountsToDelete=new ArrayList<>();

        Category newCat=_category.get(category);
        List<Integer> saleIDToAdd=new ArrayList<>();
        newCat.addAllDiscountCategory(saleIDToAdd);
        List<SaleDiscount> saleDiscountsToAdd=new ArrayList<>();


        for (Integer sale: saleIDToDelete){
            SaleDiscount s=saleCategoryDiscount.get(sale);
            if (!saleDiscountsToDelete.contains(s))
                saleDiscountsToDelete.add(s);
        }
        for (Integer sale: saleIDToAdd){
            SaleDiscount s=saleCategoryDiscount.get(sale);
            if (!saleDiscountsToAdd.contains(s))
                saleDiscountsToAdd.add(s);
        }
        pt.edit(name,minAmount,basePrice,producer,supID,category,saleDiscountsToDelete,saleDiscountsToAdd);

    }

    @Override
    public void addProduct(int typeID, Date expiration) throws Exception {
        log.debug(String.format("got inside addProduct(int typeID, Date expiration) Method with: %d, "+expiration,typeID));
        ProductType tmp=checkIDProductTypeExist(typeID);
        //if(tmp.get_shelfCurr()+tmp.get_storageCurr()>=MAX_PRODUCTS_ON_PROTUCTTYPE) throw new Exception("type "+typeID+" has "+MAX_PRODUCTS_ON_PROTUCTTYPE+" products and cant add more");

        Shelf s=findPlaceForNewProduct(typeID);
        log.warn("shelf: "+s.get_typeID()+", "+s.get_shelfID()+", "+s.get_location());
        log.warn("products="+tmp.get_products());
        int productID=_products.get(tmp).addProduct(expiration,s.get_location(),s.get_shelfID());
        log.debug(String.format("the productID: %d:",productID));
        try {
            tmp.addProduct(productID,s.get_location());
            //s.set_typeID(typeID);
            log.warn("adding product in store controller.");
            s.addProduct();
            log.warn("done adding product in store controller.");
        }
        catch (Exception e){
            log.warn(e);
            _products.get(tmp).removeProduct(productID);
            throw e;
        }

    }

    @Override
    public void removeProduct(int ID) {
        log.debug(String.format("got inside removeProduct(int ID) Method with: %d",ID));
        int typeID=ID/MAX_PRODUCTS_ON_PROTUCTTYPE;
        int productID=ID;
        ProductType tmp=checkIDProductTypeExist(typeID);
        Product p=_products.get(tmp).removeProduct(productID);
        try {
            tmp.removeProduct(productID,p.get_location());
        }
        catch (Exception e)
        {
            log.warn(e);
            _products.get(tmp).addProduct(p);
        }
        log.warn("p is: "+p.getType()+" shelf is: "+p.getShelf()+" shelves: "+_shelves);
        _shelves.get(p.getShelf()-1).removeProduct();
    }

    @Override
    public void reportDamage(int ID) {
        log.debug(String.format("got inside reportDamage(int ID) Method with: %d",ID));
        ProductType pt=checkIDProductTypeExist(ID/MAX_PRODUCTS_ON_PROTUCTTYPE);
        InstanceController ic=_products.get(pt);
        Product p=ic.reportDamage(ID);
        pt.reportDamage(p);
    }

    @Override
    public Product getProductInfo(int ID) {
        log.debug(String.format("got inside getProductInfo(int ID) Method with: %d",ID));
        return _products.get(checkIDProductTypeExist(ID/MAX_PRODUCTS_ON_PROTUCTTYPE)).getProduct(ID);
    }

    @Override
    public void relocateProduct(int ID, boolean toStorage, int targetShelf) {
        log.debug(String.format("got inside relocateProduct(int ID, boolean toStorage, int targetShelf) Method with: %d,%s,%d",ID,toStorage,targetShelf));
        String s;
        Shelf shelf=null;
        try {
            shelf = _shelves.get(targetShelf - 1);
        }
        catch (Exception e){
            log.warn("shelf doesnt exist");
        }
        if (shelf==null)
        {
            s=String.format("the targetShelf is illegal");
            log.warn(s);
            throw  new IllegalArgumentException(s);
        }
        Product p=getProductInfo(ID);
        log.warn(p.toString());
        if ((toStorage & targetShelf<=dal.get_storeShelves()) ||(!toStorage & targetShelf>dal.get_storeShelves())){
            s=String.format("the target does not in storage/store");
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        if (shelf.get_typeID()==0){
            shelf.set_typeID(ID/MAX_PRODUCTS_ON_PROTUCTTYPE);
        }
        log.warn("adding product "+p.get_id()+" to shelf "+shelf.get_shelfID());
        shelf.addProduct();
        log.warn("removing product "+p.get_id()+" from shelf "+_shelves.get(p.getShelf()-1).get_shelfID());
        Shelf sh=_shelves.get(p.getShelf()-1);
        sh.removeProduct();
        checkIDProductTypeExist(ID/MAX_PRODUCTS_ON_PROTUCTTYPE).relocateProduct(toStorage,sh.get_location());
        p.set_location(new Tuple<>(targetShelf,toStorage?Location.Storage:Location.Shelves));
    }

    @Override
    public void setShelves(List<Shelf> list) {//for testing
        _shelves=list;
    }

    @Override
    public int getProductTypeByName(String name) {
        for (ProductType p : Collections.list(_products.keys()))
            if (p.get_name().equals(name))
                return p.get_typeID();
        String info="no find ProductType with name: "+name;
        log.warn(info);
        throw new IllegalArgumentException(info);
    }

    @Override
    public void removeSupplier(int itemId, int supplierId) {
        List<ProductType> list=Collections.list(_products.keys()).stream().filter(x-> x.get_typeID()==itemId).collect(Collectors.toList());
        if (list.size()!=1){
            String info="fid move then one element";
            log.warn(info);
            throw new IllegalArgumentException(info);
        }
        ProductType pt=list.get(0);
        pt.removeSupplier(supplierId);
    }

    @Override
    public int getTypeID(int id) {
        return id/StoreController.getMaxProdOnType();
    }

    private void checkValidCategory(int catID){
        log.debug("got inside checkValidCategory(int catID) Method with: "+catID);
        String s;
        if (catID<0) {
            s=String.format("the value: %d is not valid. (<0)",catID);
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        if (_category.get(catID)==null) {
            s=String.format("the value: %d is not exist in the system.",catID);
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
    }
    private void checkValidNameCategory(String name){
        log.debug("got inside checkValidNameCategory(String name) Method with:"+name);
        String s;
        if (name==null || name.equals("")) {
            s="the name of the category is empty.";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        for (Enumeration<Category> c = _category.elements(); c.hasMoreElements();){
            if (c.nextElement().get_name().equals(name)){
                s=String.format("the name %s is exist in other category.",name);
                log.warn(s);
                throw new IllegalArgumentException(s);
            }
        }
    }
    private void checkValidNameProductType(String name){
        log.debug("got inside checkValidNameProductType(String name) Method with:"+name);
        String s;
        if (name==null || name.equals("")) {
            s="the name of the ProductType is empty.";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        for (Enumeration<ProductType> c = _products.keys(); c.hasMoreElements();) {
            if (c.nextElement().get_name().equals(name)){
                s=String.format("the name %s is exist in other ProductType.",name);
                log.warn(s);
                throw new IllegalArgumentException(s);
            }
        }
    }
    private ProductType checkIDProductTypeExist(int check){
        log.debug("got inside checkIDProductTypeExist(int check) Method with:"+check);
        String s;
        if (check<0) {
            s="the value of ProductType is illegal <0";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        for (Enumeration<ProductType> pt= _products.keys(); pt.hasMoreElements();) {
            ProductType tmp=pt.nextElement();
            if (tmp.get_typeID() == check)
                return tmp;
        }
        s=String.format("the number #%s, is not ID of any ProductType in Store #%s",check,getID());
        log.warn(s);
        throw new IllegalArgumentException(s);
    }
    private void editCategoryInBL(int Id, String name, Integer... superCategory) throws Exception {
        checkValidCategory(Id);
        if ((Arrays.stream(superCategory).count() == 1)) {
            if(Id==superCategory[0]) throw new Exception("category cannot be its own child");
            checkValidCategory(superCategory[0]);
        }

        for (Enumeration<Category> c=_category.elements(); c.hasMoreElements();)
        {
            Category tmp=c.nextElement();
            if (tmp.get_categoryID()!=Id & tmp.get_name().equals(name))
            {
                String s=String.format("the name: %s , is exist in the system ",name);
                log.warn(s);
                throw new IllegalArgumentException(s);
            }
        }
        //until here is only checks


        Category c=_category.get(Id);
        if (c.get_superCategory()!=null) {
            c.get_superCategory().removeCategory(c);
            c.fixDiscount();
        }
        if ((Arrays.stream(superCategory).count() == 1)) {
            c.edit(name, _category.get(superCategory[0]));
            c.get_superCategory().addCategory(c);
        }
        else
            _category.get(Id).edit(name);
        log.info(String.format("the Category #%d, edit his details",Id));
    }



    private Shelf findPlaceForNewProduct(int typeID) {
        for (int i=0 ;i<dal.get_numberOfShelves(); i++)
        {
            Shelf s=_shelves.get(i);
            log.warn(""+s.get_typeID()+", "+s.get_shelfID()+", "+s.get_location());
            if (s.get_typeID()==typeID && !s.isFull()) {
                s.set_typeID(typeID);
                return s;
            }
        }
        log.warn(String.format("the product with productType #%d need new shelf.",typeID));
        for (int i=0 ;i<dal.get_numberOfShelves(); i++)
        {
            Shelf s=_shelves.get(i);
            log.warn(s.get_typeID());
            if (s.get_typeID()==0 && !s.isFull()) {
                s.set_typeID(typeID);
                return s;
            }
        }
        String s=String.format("does not have a place to newProduct of type #%d",typeID);
        log.warn(s);
        throw new IllegalArgumentException(s);
    }


    public void logTypeProductList(int tid){
        Enumeration<ProductType> e=_products.keys();
        while(e.hasMoreElements()) {
            ProductType pt=e.nextElement();
            if (pt.get_typeID()==tid) log.debug("products for: "+tid+" "+pt.get_products());
        }
    }
}

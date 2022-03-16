////package tests;
//
//import Business.StockBusiness.StoreController;
//import Business.StockBusiness.Type.Category;
//import Business.StockBusiness.Type.ProductType;
//import Business.StockBusiness.iStoreController;
//import Business.StockBusiness.instance.InstanceController;
//import Business.StockBusiness.instance.Location;
//import Business.StockBusiness.instance.Shelf;
//import org.junit.Assert;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import reports.Report;
//
//import java.util.*;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class iStoreControllerTest {
//    iStoreController sc;
//
//    public iStoreControllerTest(){
//        sc=new StoreController();
//    }
//    @BeforeEach
//    void setUp() {
//        Category c=mock(Category.class);
//        when(c.get_categories()).thenReturn(new ArrayList<>());
//    }
//
//
//    @Test
//    void getID() {
//        int check=sc.getID();
//        if (check<0)
//            Assert.fail();
//        for (int i=0; i<20; i++)
//            if (check!=sc.getID())
//                Assert.fail();
//    }
//    private List<ProductType> listOfProductType(int num){
//        List<ProductType> productTypeList=new ArrayList<>();
//        for (int i = 0; i < num; i++) {
//            productTypeList.add(mock(ProductType.class));
//        }
//        return productTypeList;
//    }
//    private List<InstanceController> listOfIC(int num) {
//        List<InstanceController> list = new ArrayList<>();
//        for (int i = 0; i < num; i++) {
//            list.add(mock(InstanceController.class));
//        }
//        return list;
//    }
//
//    @Test
//    void getWeeklyReport() {
//        try {
//            Report r = sc.getWeeklyReport();
//            Assertions.assertEquals(r.getType(), "WeeklyReport", "the type should be WeeklyReport");
//            report(r);
//
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            Assertions.fail();
//        }
//    }
//    @Test
//    void getWeeklyReport2() {
//        try {
//            Dictionary<ProductType, InstanceController> d = createList(20);
//            for (InstanceController ic: Collections.list(d.elements()))
//            {
//                when(ic.getWeeklyReport()).thenReturn(new Hashtable<>());
//            }
//            sc.setList(d);
//            Report r2 = sc.getWeeklyReport();
//            Assertions.assertEquals(r2.sizeOfList(), 20, "should be 20");
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            Assertions.fail();
//        }
//    }
//
//    void report(Report r){
//        Date date=new Date();
//        date.setDate(date.getDate()+1);
//        Assertions.assertTrue(r.getDate().before(date),"the date is not valid");
//        Assertions.assertEquals(r.getStore(),sc.getID(),"the storeId is not equals");
//    }
//
//    private Dictionary<ProductType,InstanceController> createList(int i){
//        Dictionary<ProductType,InstanceController> d=new Hashtable<>();
//        List<ProductType> list1=listOfProductType(i);
//        List<InstanceController> list2=listOfIC(i);
//        for (int j = 0; j < i; j++) {
//            d.put(list1.get(j), list2.get(j));
//            when(list1.get(j).get_typeID()).thenReturn(j);
//        }
//        return d;
//    }
//
//    @Test
//    void getNeededReport() {
//        Report r=sc.getNeededReport();
//        Assertions.assertEquals(r.getType(), "NeededReport", "the type should be NeededReport");
//        Assertions.assertTrue(r.getDate().before(new Date(System.currentTimeMillis()+15)),"the date is not valid");
//        Assertions.assertEquals(r.getStore(),sc.getID(),"the storeId is not equals");
//    }
//
//    @Test
//    void getNeededReport2() {
//        Dictionary<ProductType,InstanceController> d=createList(20);
//        int i=1;
//        for (Enumeration<ProductType> ic=d.keys(); ic.hasMoreElements();) {
//            ProductType p=ic.nextElement();
//            when(p.getNeededReport()).thenReturn(20);
//            when(p.get_typeID()).thenReturn(i++);
//        }
//        sc.setList(d);
//        Report r2=sc.getNeededReport();
//
//        Assertions.assertEquals(20,r2.sizeOfList(),"should be 20");
//
//        d=createList(20);
//        for (Enumeration<ProductType> ic=d.keys(); ic.hasMoreElements();)
//            when(ic.nextElement().getNeededReport()).thenReturn(0);
//        sc.setList(d);
//        r2=sc.getNeededReport();
//        Assertions.assertEquals(0,r2.sizeOfList(),"should be 0");
//
//    }
//
//
//
//    @Test
//    void getWasteReport() {
//        Report r=sc.getWasteReport();
//        Assertions.assertEquals(r.getType(), "WasteReport", "the type should be WasteReport");
//        report(r);
//    }
//
//    @Test
//    void counterCategory() {
//        sc=new StoreController();
//        int counter=sc.counterCategory();
//        for (int i=0; i<20; i++)
//        {
//            Assertions.assertEquals(counter+i,sc.counterCategory());
//            sc.addCategory("test "+i);
//        }
//    }
//
//    @Test
//    void getCategory() {
//        for (int i=0; i<20; i++)
//        {
//            Assertions.assertEquals(i,sc.counterCategory(),"the SC does not added Category #"+i);
//            sc.addCategory("test "+i);
//        }
//    }
//
//    @Test
//    void addCategoryOnlyName() {
//        Category c=sc.addCategory("test");
//        Assertions.assertTrue(sc.containCategory(c));
//        try {
//            sc.addCategory("test");
//            Assertions.fail();
//        }
//        catch (Exception e){
//            Assertions.assertTrue(true);
//        }
//    }
//    @Test
//    void addCategory() {
//        try{
//            sc.addCategory("test",6);
//            Assertions.fail();
//        }
//        catch (Exception e){
//        }
//    }
//    @Test
//    void addCategory2() { ///
//        try {
//            Category c1 = sc.addCategory("test1");
//            Category c2 = sc.addCategory("test2", c1.get_categoryID());
//            Assertions.assertTrue(sc.containCategory(c2));
//            Assertions.assertTrue(sc.containCategory(c1));
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            Assertions.fail();
//        }
//
//    }
//
//
//
//    @Test
//    void getCategories() {
//        sc.setCategories(categoryDictionary(20));
//        Assertions.assertEquals(20,sc.getCategories().size());
//    }
//
//    @Test
//    void addProductType() {
//
//        try {
//            sc.addProductType("t1",1,15,15,"t",5,7);
//            Assertions.fail();
//        }
//        catch (Exception e){
//            Dictionary<Integer,Category> c=new Hashtable<>();
//            for (int i=0 ;i<20 ; i++){
//                Category cat=mock(Category.class);
//                when(cat.get_categoryID()).thenReturn(i);
//                c.put(i,cat);
//            }
//            sc.setCategories(c);
//            try {
//                sc.addProductType("t1", 1, 15, 5, "t", 5, 7);
//                sc.addProductType("t1", 1, 15, 15, "t", 5, 7);
//                Assertions.fail();
//            }
//            catch (Exception e1){
//                Assertions.assertTrue(sc.containProductType("t1"));
//            }
//        }
//    }
//
////    @Test
////    @ParameterizedTest
////    @ValueSource(ints = {0,1,2,5,18})
////    void getProductTypes(int i) {
////        sc.setList(createList(i));
////        Assertions.assertEquals(i,sc.getProductTypes());
////    }
//
//    @Test
//    void getProductTypeInfo() {
//        try{
//            sc.getProductTypeInfo(1);
//            Assertions.fail();
//        }
//        catch (Exception e){
//        }
//    }
//
//
//
//    @Test
//    void addSaleProductDiscount() {
//        try {
//            sc.addSaleProductDiscount(-1, 4, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 4));
//            Assertions.fail();
//        }
//        catch (Exception e){
//        }
//    }
//    private Dictionary<Integer,Category> categoryDictionary(int j){
//        Dictionary<Integer,Category> c=new Hashtable<>();
//        for (int i=0; i<j ;i++)
//        {
//            Category cat=mock(Category.class);
//            when(cat.get_categoryID()).thenReturn(i);
//            c.put(i,cat);
//        }
//        return c;
//    }
//
//
////    @Test
////    void editCategory() {
////        try {
////            sc.editCategory(5,"5");
////            Assertions.fail();
////        }
////        catch (Exception e){
////            sc.setCategories(categoryDictionary(20));
////            sc.editCategory(5,"name");
////        }
////    }
//
//
//    @Test
//
//    void addProduct() {
//        try {
//            Dictionary<ProductType, InstanceController> pt = new Hashtable<>();
//            setRealValue(20, pt);
//            for (Enumeration<ProductType> p = pt.keys(); p.hasMoreElements(); ) {
//                ProductType pp = p.nextElement();
//                List<Integer> list1=pp.get_products();
//                List<Integer> list2=pt.get(pp).getProduts();
//                Collections.sort(list1);
//                Collections.sort(list2);
//                Assertions.assertEquals(list1, list2);
//            }
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            Assertions.fail();
//        }
//    }
//    private void setRealValue(int j,Dictionary<ProductType,InstanceController> pt){
//
//        ProductType p=new ProductType();
//        InstanceController ic=new InstanceController();
//        pt.put(p,ic);
//        sc.setList(pt);
//        List<Shelf> shelves=new ArrayList<>();
//
//        for (int i = 0; i < 20; i++) {
//            Shelf s=mock(Shelf.class);
//            when(s.isFull()).thenReturn(false);
//            when(s.get_location()).thenReturn(Location.Shelves);
//            when(s.get_shelfID()).thenReturn(i+1);
//            shelves.add(s);
//        }
//
//        sc.setShelves(shelves);
//
//        for (int i=0; i<j ;i++) {
//            Date tmp=new Date();
//            tmp.setDate(tmp.getDate()+1);
//            try {
//                sc.addProduct(1000, tmp);
//            }
//            catch (Exception e) {
//            }
//        }
//    }
//
//    @Test
//
//    void removeProduct() {
//        try {
//            Dictionary<ProductType, InstanceController> pt = new Hashtable<>();
//            setRealValue(20, pt);
//            for (int i = 0; i < 10; i++) {
//                sc.removeProduct(1000*1000 + i);
//            }
//            for (Enumeration<ProductType> p = pt.keys(); p.hasMoreElements(); ) {
//                ProductType pp = p.nextElement();
//                List<Integer> list1=pp.get_products();
//                List<Integer> list2=pt.get(pp).getProduts();
//                Collections.sort(list1);
//                Collections.sort(list2);
//                Assertions.assertEquals(list1, list2);
//            }
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            Assertions.fail();
//        }
//
//    }
//
//    @Test
//    void reportDamage() {
//        try {
//            Dictionary<ProductType, InstanceController> pt = new Hashtable<>();
//            setRealValue(20, pt);
//            for (int i = 0; i < 10; i++) {
//                sc.reportDamage(1000*1000 + i + 1);
//            }
//            for(ProductType pp: Collections.list(pt.keys())){
//                List<Integer> list1=pp.get_products();
//                List<Integer> list2=pt.get(pp).getProduts();
//                Collections.sort(list1);
//                Collections.sort(list2);
//                Assertions.assertEquals(list1, list2);
//            }
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            Assertions.fail();
//
//        }
//
//    }
//
//
//}
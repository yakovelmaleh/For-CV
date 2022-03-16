package Business.StockBusiness.Type;

import DataAccess.DALObject;
import DataAccess.DalStock.DALCategory;
import DataAccess.SMapper;
import Utility.Tuple;
import Utility.Util;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Category {
    final static Logger log=Logger.getLogger(Category.class);
    private List<Category> _categories=new ArrayList<>();
    private Category _superCategory=null;
    private DALCategory dal;

    public Category(int id, Integer i) {
        List<Integer> list=new ArrayList<>();
        list.add(id);
        list.add(i);
        dal=(DALCategory) SMapper.getMap().getItem(DALCategory.class,list);
    }

    public Category get_superCategory() {
        return _superCategory;
    }
    public Category(DALCategory d){dal=d;}//for loading


    public Category(int storeID,int _categoryID, String _name, Category tmp) {
        checkValues(_categoryID,_name);
        dal=Util.initDal(DALCategory.class,storeID,_categoryID, tmp.get_categoryID(), _name);
        _superCategory=tmp;
        List<Integer> discount=new ArrayList<>();
        tmp.addAllDiscountCategory(discount);
        try{
            dal.setDiscounts(discount);
        }
        catch (Exception e){
            String info="can not save discounts";
            log.warn(info);
            throw new IllegalArgumentException(info);
        }
    }

    private void checkValues(Object... o){
        for(Object o1: o){
            if (o1 instanceof String && o1.equals(""))
            {
                String s="the value is illegal";
                log.warn(s);
                throw new IllegalArgumentException(s);
            }
            else if (o1 instanceof Integer && (Integer)o1<1){
                String s="the value is illegal";
                log.warn(s);
                throw new IllegalArgumentException(s);
            }
        }
    }

    public Category(int storeId,int catId, String name) {
        checkValues(catId,name);
        if (catId<1 | name==null || name.equals(""))
        {
            String s="the value is illegal";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        //dal=Util.initDal(DALCategory.class,storeId,catId,null,name);
        Class c=DALCategory.class;
        List<Tuple<Object,Class>> list=new ArrayList<>();
        list.add(new Tuple<>(storeId,Integer.class));
        list.add(new Tuple<>(catId,Integer.class));
        list.add(new Tuple<>(null,Integer.class));
        list.add(new Tuple<>(name,String.class));
        SMapper map= SMapper.getMap();
        map.setItem(c,list);
        List<Integer> keyList=new ArrayList<>();
        keyList.add(storeId);
        keyList.add(catId);
        DALObject check =map.getItem(c ,keyList);
        if (c==null || check==null ||(check.getClass()!=c)){
            String s="the instance that return from Mapper is null for: "+c;
            log.warn(s);
            throw new IllegalArgumentException(s);

        }
        else{
            log.info("create new Object");
        }
        dal=(DALCategory) check;
    }

    public List<Category> get_categories() {
        log.debug("get_categories()");
        return _categories;
    }

    public int get_categoryID() {
        log.debug("get_categoryID()");
        return dal.getCategoryID();
    }


    public String get_name() {
        log.debug("get_name()");
        return dal.getName();
    }

    public void set_name(String name) {
        if (name==null || name.equals(""))
        {
            String s="the value is illegal";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        try{
            dal.setName(name);
        }
        catch (Exception e){
            String info="can not save new Name in DataBase";
            log.warn(info);
            throw new IllegalArgumentException(info);
        }
    }

    public List<Integer> get_productTypes() {
        log.debug("get_productTypes()");
        return dal.getProductTypes();
    }


    public List<Integer> get_productDiscounts() {
        log.debug("get_productDiscounts()");
        return dal.getDiscounts();
    }
    public void addAllDiscountCategory(List<Integer> list){
        List<Integer> _productDiscounts=dal.getDiscounts();
        for (Integer i: _productDiscounts) {
            if (!list.contains(i))
                list.add(i);
        }
        if (_superCategory!=null)
            _superCategory.addAllDiscountCategory(list);
    }


    public void addCategory(Category output) {
        if (output==null || output==this){
            String s="the Category is illegal";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        try{
            dal.addCategory(output.get_categoryID());
        }
        catch (Exception e){
            String s="can not add the category to DB";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        _categories.add(output);
    }

    public void addProductType(int typeID) {
        log.debug(String.format("addProductType(int typeID)",typeID));
        if (typeID<1)
        {
            String s="the Category is illegal";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        try{
            dal.addProductType(typeID);
        }
        catch (Exception e){
            String s="can not add the type to DB";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
    }

    public List<Integer> getAllProductType() {
        log.debug("getAllProductType()");
        List<Integer> list=dal.getProductTypes();
        for (Category c:_categories) {
            list.addAll(c.getAllProductType());
        }
        return list;
    }

    public void addDiscount(int count) {
        log.debug(String.format("addDiscount(int count)",count));
        checkValues(count);
        try{
            dal.addDiscount(count);
        }
        catch (Exception e){
            String s="can not add Discount to DB";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
    }

    public void removeDiscount(int count) {
        log.debug(String.format("removeDiscount(int count)",count));
        checkValues(count);
        try{
            dal.removeDiscount(count);
        }
        catch (Exception e){
            String s="can not remove Discount to DB";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
    }

    public void edit(String name, Category superCategory) {
        log.debug(String.format("edit(String name, Category superCategory) Value:",name));
        checkValues(name);
        if (superCategory==null || superCategory==this)
        {
            String s="the superCategory is illegal";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        if (checkRec(superCategory))
        {
            String s=String.format("the Category #%d can not be a child of itself",dal.getCategoryID());
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        String lastName=dal.getName();
        try{
            dal.setName(name);
        }
        catch (Exception e){
            String s="can not edit the name of this Category in DB";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        List<Integer> lastDiscount=dal.getDiscounts();
        try{
            List<Integer> list=dal.getDiscounts();
            _superCategory.addAllDiscountCategory(list);
            dal.setDiscounts(list);
        }
        catch (Exception e){
            dal.setName(lastName);
            String s="can not edit discount of this Category in DB";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        try{
            dal.setSuperCategory(superCategory.get_categoryID());
        }
        catch (Exception e){
            dal.setName(lastName);
            dal.setDiscounts(lastDiscount);
            String s="can not edit discount of this Category in DB";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        _superCategory=superCategory;
        log.info(String.format("the values of Category #? changed.",dal.getCategoryID()));
    }

    private boolean checkRec(Category superCategory) {
        if (_categories.contains(superCategory))
            return true;
        for (Category c: _categories){
            if (c.checkRec(superCategory))
                return true;
        }
        return false;
    }

    public void edit(String name) {
        log.debug(String.format("edit(String name) Value:",name));
        checkValues(name);
        try{
            dal.setName(name);
        }
        catch (Exception e){
            String s="can not edit the name of this Category in DB";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        _superCategory=null;
        log.info(String.format("the values of Category #? changed.",dal.getCategoryID()));
    }

    @Override
    public String toString() {
        return "Category{" +
                "_categories=" + _categories.stream().map(Category::get_categoryID).collect(Collectors.toList()) +
                ", _categoryID=" + dal.getCategoryID() +
                ", _name='" + dal.getName() + '\'' +
                ", _superCategory=" + ((_superCategory==null)? "null":_superCategory.get_categoryID()) +
                ", _productTypes=" + dal.getProductTypes() +
                ", _productDiscounts=" + dal.getDiscounts() +
                '}';
    }

    public void removeCategory(Category c) {
        try{
            dal.removeCategory(c.get_categoryID());
        }
        catch (Exception e){
            String s="can not remove the Category in DB";
            log.warn(s);
            throw new IllegalArgumentException(s);
        }
        _categories.remove(c);
    }

    public void fixDiscount() {
        if (_superCategory!=null) {
            List<Integer> discounts=dal.getDiscounts();
            _superCategory.fixDiscount(discounts);
            try{
                dal.setDiscounts(discounts);
            }
            catch (Exception e){
                String s="can not fix the Category in DB";
                log.warn(s);
                throw new IllegalArgumentException(s);
            }
        }
    }
    private void fixDiscount(List<Integer> list){
        for (Integer i: dal.getDiscounts())
            if (list.contains(i))
                list.remove(i);
        if (_superCategory!=null)
            _superCategory.fixDiscount(list);
    }

    public void initCategory(List<Category> categories) {
        for (Category c: categories){
            if (dal.getParent()==c.get_categoryID())
                _superCategory=c;
            if (c.dal.getParent()==get_categoryID())
                _categories.add(c);
        }
    }
}

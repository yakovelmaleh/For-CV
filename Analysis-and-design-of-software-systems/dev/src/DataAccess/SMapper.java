package DataAccess;

import DataAccess.DalStock.*;
import DataAccess.DalSuppliers.*;
import Utility.Tuple;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class SMapper {

    private class IntKey{
        private List<Integer> pk;

        IntKey(List<Integer> k){pk = k;}
        @Override
        public boolean equals(Object obj) {
            if(obj != null && obj instanceof IntKey) {
                IntKey o = (IntKey)obj;
                for(int i=0;i<pk.size();i++){
                    try{
                        o.pk.get(i);
                    }catch(Exception e){
                        return false;
                    }
                    if(!o.pk.get(i).equals(pk.get(i))) return false;
                }
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            Integer s=0;
            for(Integer i:pk){
                s+=i;
            }
            return s.hashCode();
        }
    }

    private static List<Class> allDAL=new ArrayList<>();
    private static SMapper instance=null;
    final static Logger log=Logger.getLogger(SMapper.class);
    DalController DC;
    HashMap<Class,HashMap<IntKey, DALObject>> map;
    private SMapper(String dbname){
            DC=new DalController(dbname);
            map=new HashMap<>();
            init();
    }
    private void init(){
        // THIS NEEDS UPDATE ON EACH NEW DAL OBJECT
        allDAL.add(DALCategory.class);
        allDAL.add(DALShelf.class);
        allDAL.add(DALSaleDiscount.class);
        allDAL.add(DALSupplierDiscount.class);
        allDAL.add(DALProduct.class);
        allDAL.add(DALStoreController.class);
        allDAL.add(DALInstanceController.class);
        allDAL.add(DALProductType.class);

        allDAL.add(DalItem.class);
        allDAL.add(DalOrder.class);
        allDAL.add(DalQuantityDocument.class);
        allDAL.add(DalSupplierAgreement.class);
        allDAL.add(DalSupplierCard.class);
        allDAL.add(DalSupplierController.class);


        for(Class c: allDAL){
            try {
                Constructor con = c.getConstructor(null);
                Method cre = c.getMethod("getCreate");
                DC.noSelect((String) cre.invoke(con.newInstance()),null);
                log.info("creating "+c.getName());
            } catch (Exception e){ log.warn("Class "+c.getName()+" not created in DB due to "+e);}
        }
    }

    public static SMapper getMap(String dbname) {
        if (instance == null) {
            instance = new SMapper(dbname);
        }
        return instance;
    }

    public static SMapper getMap() {
        return instance;
    }
    
    public DALObject getItem(Class cls, List<Integer> pk) {
        if(map.containsKey(cls)){
            IntKey k=new IntKey(pk);
            if(!map.get(cls).containsKey(k)){
                try {
                    Method met=cls.getMethod("getSelect");
                    Constructor con=cls.getConstructor();
                    String select=(String) met.invoke(con.newInstance(),null);
                    Tuple<List<Class>,List<Object>> tup=DC.Select(select, pk);
                    DALObject out = fromRS(tup, cls);
                if(out==null) {
                    log.warn("mapper returned null on:"+cls);
                    return null;
                }
                map.get(cls).put(k, out);
                return out;
                }catch (Exception e){
                    log.warn("mapper returned null on:"+cls);
                    return null;
                }
            }
            return map.get(cls).get(k);
        }
        else{
            map.put(cls, new HashMap<>());
            return getItem(cls,pk);
        }
    }

    private DALObject fromRS(Tuple<List<Class>,List<Object>> tup, Class cls) {
        Class[] tarr= new Class[tup.item1.size()];
        tarr=tup.item1.toArray(tarr);
        Object[] varr= new Object[tup.item2.size()];
        varr=tup.item2.toArray(varr);

        try {
            Constructor con = cls.getConstructor(tarr);
            DALObject out = (DALObject) con.newInstance(varr);
            return out;
        }
        catch (Exception e) {
            log.warn(e);
            return null;
        }
    }

    public int setItem(Class cls, List<Tuple<Object,Class>> params) {
        try{
            Method met=cls.getMethod("getInsert");
            Constructor con=cls.getConstructor();
            String insert=(String) met.invoke(con.newInstance(),null);
            int ret= DC.noSelect(insert,params);
            return ret;
        }
        catch (Exception e){
            log.warn(e);
            return 0;
        }
    }

    public int deleteItem(Class cls, List<Integer> pk){
        try{
            Method met=cls.getMethod("getDelete");
            Constructor con=cls.getConstructor();
            String delete=(String) met.invoke(con.newInstance(),null);
            List<Tuple<Object,Class>> ls=new ArrayList<>();
            for(Integer k: pk){
                ls.add(new Tuple<>(k,Integer.class));
            }
            int ret = DC.noSelect(delete,ls);
            if(map.containsKey(cls) && map.get(cls).containsKey(pk)) map.get(cls).remove(pk);
            return ret;
        }
        catch (Exception e){
            log.warn(e);
            return 0;
        }
    }

}

package Utility;

import DataAccess.DALObject;
import DataAccess.SMapper;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public  class Util {
    final static Logger log=Logger.getLogger(Util.class);
    public static <T> T initDal(Class<T> c, int storeID, Integer key, Object... o){
        List<Tuple<Object,Class>> list=new ArrayList<>();
        list.add(new Tuple<>(storeID,Integer.class));
        list.add(new Tuple<>(key,Integer.class));
        for (Object o1: o) {
            if (o1==null)
                list.add(new Tuple<>(null,Integer.class));
            else
                list.add(new Tuple<>(o1,o1.getClass()));
        }
        List<Integer> keyList=new ArrayList<>();
        keyList.add(storeID);
        keyList.add(key);
        SMapper map=SMapper.getMap();
        DALObject check =map.getItem(c,keyList);
        if (check!=null) {
            log.warn("entry is already in DB, will not attempt insert.");
            return (T) check;
        }
        map.setItem(c,list);

        check =map.getItem(c ,keyList);
        if (c==null || check==null ||(check.getClass()!=c)){
            String s="the instance that return from SMapper is null for: "+c;
            log.warn(s);
            throw new IllegalArgumentException(s);

        }
        else{
            log.info("create new Object");
        }
        return (T) check;
    }
}

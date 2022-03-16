package DataAccess;

import Utility.Tuple;
import org.apache.log4j.Logger;
import org.sqlite.SQLiteConfig;


import java.sql.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DalController {
    final static Logger log=Logger.getLogger(DalController.class);
    String dbname;

    public DalController(String dbname) {
        this.dbname = dbname;
    }

    private Connection connect() throws Exception {
        //DriverManager.registerDriver(new com.sqlite.jdbc.Driver());
        Class.forName("org.sqlite.JDBC");
        String url = "jdbc:sqlite:"+System.getProperty("user.dir")+"\\" + dbname;
        Connection conn = null;
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            conn = DriverManager.getConnection(url , config.toProperties());
        } catch (SQLException e) {
            log.warn("failed to make SQL connection");
           throw new Exception("cant connect");
        }
        return conn;
    }

    // String String === Val Type
    public int noSelect(String query, List<Tuple<Object,Class>> params) throws Exception {
        if (query==null) return 0;
        List<String> doQuary= Arrays.asList(query.split(";"));
        if(doQuary.size()>1 && params!=null) throw new Exception("non create multi-query");
        boolean isDefault = false;
        int ret=0;
        for(int j=0;j<doQuary.size();j++) {
            query=doQuary.get(j);
            try (Connection conn = this.connect();
                 PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                if (params != null) {
                    for (int i = 1; i <= params.size() && !isDefault; i++) {
                        Tuple<Object, Class> tuple = params.get(i-1);
                        if (null==(tuple.item1)) {
                            preparedStatement.setNull(i, java.sql.Types.INTEGER);
                        }
                        else if (Integer.class.equals(tuple.item2)) {
                            preparedStatement.setInt(i, (Integer)(tuple.item1));
                        } else if (String.class.equals(tuple.item2)) {
                            preparedStatement.setString(i, (String) tuple.item1);
                        } else if (Double.class.equals(tuple.item2)) {
                            preparedStatement.setDouble(i, (Double)(tuple.item1));
                        } else {
                            isDefault = true;
                        }
                    }
                    if (isDefault) throw new Exception("illegal type");
                }
                ret += preparedStatement.executeUpdate();
            } catch (Exception e) {
                log.warn("noSelect query: " + query +" params: " + params+ "\nfailed due to " + e.getMessage());
                throw e;
            }
        }
        return ret;
    }

    public Tuple<List<Class>,List<Object>> Select(String query, List<Integer> params) throws Exception {
        Connection conn = this.connect();
        try {
            log.warn("Select query: " + query +" params: " + params+ "\nfailed due to ");
            PreparedStatement preparedStatement  = conn.prepareStatement(query);
            for(int i = 0 ; i < params.size() ; i++){
                preparedStatement.setInt(i+1 , params.get(i));
            }
            ResultSet rs = preparedStatement.executeQuery();
            return fromRS(rs);
        } catch (Exception e) {
            log.warn("Select query: " + query +" params: " + params+ "\nfailed due to " + e.getMessage());
            throw new Exception("select field");
        }
        finally{
            conn.close();
        }
    }

    private Tuple<List<Class>,List<Object>> fromRS(ResultSet rs){
        List<Class> types=new ArrayList<>();
        List<Object> vals=new ArrayList<>();
        if (rs != null ) {
            try {
                rs.next();
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    int type = rsmd.getColumnType(i);
                    if (type == Types.VARCHAR || type == Types.CHAR) {
                        types.add(String.class);
                        vals.add(rs.getString(i));
                    } else if (type == Types.FLOAT || type==Types.DOUBLE || type == Types.REAL) {
                        types.add(Double.class);
                        vals.add(rs.getDouble(i));
                    } else {
                        types.add(Integer.class);
                        vals.add(rs.getInt(i));
                    }
                }
                if (rs.next()) return null;
            }
            catch (Exception e){
                log.warn("DalController from RS error due to: "+e);
                return null;
            }
        } else return null;

        types.add(DalController.class);
        vals.add(this);

        return new Tuple<>(types,vals);
    }

    public List<Tuple<List<Class>,List<Object>>> SelectMany(String query, List<Integer> params) throws Exception {
        Connection conn = this.connect();
        try {
            PreparedStatement preparedStatement  = conn.prepareStatement(query);
            for(int i = 0 ; i < params.size() ; i++){
                if(params.get(i)==null) preparedStatement.setNull(i+1,Types.INTEGER);
                else preparedStatement.setInt(i+1 , params.get(i));
            }
            ResultSet rs = preparedStatement.executeQuery();
            return fromRSMany(rs);
        } catch (SQLException e) {
            throw new Exception("select field");
        }
        finally{
            conn.close();
        }
    }

    private List<Tuple<List<Class>,List<Object>>> fromRSMany(ResultSet rs){
        List<Class> types=new ArrayList<>();
        List<Object> vals=new ArrayList<>();
        List<Tuple<List<Class>,List<Object>>> out=new ArrayList<>();
        if (rs != null ) {
            try {
                while(rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        int type = rsmd.getColumnType(i);
                        if (type == Types.VARCHAR || type == Types.CHAR) {
                            types.add(String.class);
                            vals.add(rs.getString(i));
                        } else if (type == Types.FLOAT || type == Types.DOUBLE || type == Types.REAL) {
                            types.add(Double.class);
                            vals.add(rs.getDouble(i));
                        } else {
                            types.add(Integer.class);
                            vals.add(rs.getInt(i));
                        }
                    }
                    types.add(DalController.class);
                    vals.add(this);
                    out.add(new Tuple<>(types,vals));
                }
                return out;
            }
            catch (Exception e){
                log.warn(e.getMessage());
                return null;
            }
        } else return null;
    }

}

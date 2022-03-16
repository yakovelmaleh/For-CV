package DataAccess;


import Business.Transportation.Truck;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TruckMapper extends Mapper{

    static  private TruckMapper mapper=null;
    private Map<Long,Truck> trucks;

    public static TruckMapper getMapper( ){
        if(mapper==null){
            mapper=new TruckMapper();
        }
        return mapper;
    }

    private TruckMapper( ){
        super();
        trucks=new HashMap<>();
    }

    private List< Truck> selectAll() throws Exception {
        String sql = "SELECT * FROM Trucks";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Truck tr=new Truck(rs.getLong("ID"),rs.getInt("License"),rs.getInt("MaxWeight"),rs.getInt("NetWeight"),rs.getString("Model"));
               trucks.put(rs.getLong("ID"),tr);
            }
        } catch (SQLException e) {
            //throw new IOException("failed to get all trucks from database");
        }
        return new ArrayList<>(trucks.values());
    }
    public List< Truck> getTrucksByWeight(double weight) throws Exception {
        String sql = "SELECT * FROM Trucks WHERE MaxWeight>="+weight+ " ORDER BY License ASC;";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Truck tr=new Truck(rs.getLong("ID"),rs.getInt("License"),rs.getInt("MaxWeight"),rs.getInt("NetWeight"),rs.getString("Model"));
                trucks.put(rs.getLong("ID"),tr);
            }
        } catch (SQLException e) {
            //throw new IOException("failed to get all trucks from database");
        }
        return new ArrayList<>(trucks.values());
    }
    private Truck select(long id) throws  Exception {

            String sql = "SELECT * FROM Trucks WHERE ID=" + id;
            try (Connection conn = connect();
                 Statement stmt  = conn.createStatement();
                 ResultSet rs    = stmt.executeQuery(sql)){
                // loop through the result set
                while (rs.next()) {
                    return new Truck(rs.getLong("ID"),rs.getInt("License"),rs.getInt("MaxWeight"),rs.getInt("NetWeight"),rs.getString("Model"));
                }
            } catch (SQLException e) {
                //throw new IOException("failed to get all trucks from database");
            }
            return null;
    }

    public List<Truck> getTrucks() throws Exception {

        return selectAll();
    }

    public void addTruck(long id, int maxweight,String model, int netWeight, int license){

        String sql = "INSERT INTO Trucks (ID,MaxWeight,Model,NetWeight,License) VALUES(?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1,id );
            pstmt.setInt(2,maxweight );
            pstmt.setString(3,model );
            pstmt.setInt(4,netWeight );
            pstmt.setInt(5, license);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            //System.out.println(e.getMessage());
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
    }

    public Truck getTruck(long id) throws Exception {

        if(trucks.containsKey(id))
            return trucks.get(id);
        else{
            Truck it=select(id);
            if (it!=null)
                return it;
           // throw new IOException("Truck does not exists");
            return null;
        }
    }
}

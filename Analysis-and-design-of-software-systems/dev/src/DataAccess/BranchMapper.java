package DataAccess;

import Business.Transportation.Address;
import Business.Transportation.Branch;
import Business.Type.Area;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BranchMapper extends Mapper{

    static private BranchMapper mapper = null;
    static private String dbName;
    private HashMap<Integer, Branch> branches;


    public static BranchMapper getMapper() {
        if (mapper == null) {
            mapper = new BranchMapper(dbName);
        }
        return mapper;
    }

    private BranchMapper(String name) {
        super();
        branches = new HashMap<>();
    }

    /**
     * select all rows in the Branch table
     */
    private List<Branch> selectAll() throws Exception {
        String sql = "SELECT * FROM Branches";
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            // loop through the result set
            while (rs.next()) {
                Address add=new Address(rs.getInt("Number"),rs.getString("Street"),rs.getString("City"));
                Area are=Area.valueOf(rs.getString("Area"));
                branches.put(rs.getInt("BID"), new Branch(rs.getString("Phone"),rs.getString("ContactName"),rs.getInt("BID"),add,are));
            }
        } catch (SQLException e) {
            throw new IOException("failed to get all branches from database");
        }
        return new ArrayList<>(branches.values());
    }

    private Branch select(int id) throws  Exception{
        Branch branch= null;
        String sql = "SELECT * FROM Branches WHERE BID="+ id ;
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            if (rs.next()) {
                Address add=new Address(rs.getInt("Number"),rs.getString("Street"),rs.getString("City"));
                Area are= Area.valueOf(rs.getString("Area"));
               branch = new Branch(rs.getString("Phone"),rs.getString("ContactName"),rs.getInt("BID"),add,are);
               branches.put(id,branch);
            }
        } catch (SQLException e) {
            throw new IOException("failed to get branch from database");
        }
        return branch;
    }
    private String getBranchString(int id) throws  Exception{
        Branch branch= null;
        String sql = "SELECT * FROM Branches WHERE BID="+ id ;
        try (Connection conn = connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            if (rs.next()) {
                Address add=new Address(rs.getInt("Number"),rs.getString("Street"),rs.getString("City"));
                Area are= Area.valueOf(rs.getString("Area"));
                branch = new Branch(rs.getString("Phone"),rs.getString("ContactName"),rs.getInt("BID"),add,are);
                branches.put(id,branch);
            }
        } catch (SQLException e) {
            throw new IOException("failed to get branch from database");
        }
        if(branch!=null)
            return branch.toString();
        return "";
    }

    public List<Branch> getBranches() throws Exception {
        return selectAll();
    }

    public Branch getBranch(int id) throws Exception {
        if(branches.containsKey(id)){
            return branches.get(id);
        }
        else {
            Branch sup= select(id);
            if(sup!=null){
                return sup;
            }
            throw new IllegalArgumentException("branch with id: " + id +"does not exist");
        }
    }

    public int insertNewBranch(String street, String city, int number, int enter, String area, String cn, String phone) {
        String getNextBID = "SELECT max(BID)+1 AS mx FROM Branches";
        String addBranch = "INSERT INTO Branches VALUES(?,?,?,?,?,?,?,?)";
        int bid = 0;
        try (Connection con = connect();
             PreparedStatement nextID = con.prepareStatement(getNextBID);
             PreparedStatement pre = con.prepareStatement(addBranch)) {
            ResultSet res = nextID.executeQuery();
            bid = res.getInt("mx");
            if (bid == 0) bid++;
            pre.setInt(1, bid);
            pre.setString(2,street);
            pre.setString(3,city);
            pre.setInt(4,number);
            pre.setInt(5,enter);
            pre.setString(6,area);
            pre.setString(7,cn);
            pre.setString(8,phone);
            pre.executeUpdate();
        } catch (Exception e) {
            //System.out.println("[insertNewBranch-branchMapper] ->" + e.getMessage());
        }
        Branch branch = new Branch(phone,cn,bid,new Address(number,street,city),Area.valueOf(area));
        branches.put(bid,branch);
        return bid;
    }
}

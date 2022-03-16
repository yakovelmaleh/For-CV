package DataAccess;

import Business.Employees.ShiftPKG.ConstConstraint;
import Business.Employees.ShiftPKG.Constraint;
import Business.Employees.ShiftPKG.TempConstraint;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstraintMapper extends Mapper{
    private static ConstraintMapper conMapper = null;
    private final HashMap<Integer, Constraint> constraints;

    public static ConstraintMapper getInstance() {
        if (conMapper == null)
            conMapper = new ConstraintMapper();
        return conMapper;
    }

    private ConstraintMapper(){
        super();
        constraints = new HashMap<>();
    }


    public int getNextCID() {
        int nextCID = 1;
        ResultSet res;
        String query = "SELECT max(CID)+1 as maxCID FROM (SELECT CID from ConstConstraints union all SELECT CID FROM TempConstraints)";
        try (Connection con = connect(); PreparedStatement pre = con.prepareStatement(query)) {
            res = pre.executeQuery();
            if (res.next())
                nextCID = res.getInt("maxCID")==0?1:res.getInt("maxCID");
        } catch (Exception e) {
            //System.out.println("[getNextCID] ->" +e.getMessage());
        }
        return nextCID;

    }

    public void insertConstConstraint(ConstConstraint c) {
        boolean res =false;
        String query = "INSERT INTO ConstConstraints (CID,EID,ShiftType,DayOfWeek,Reason) VALUES (?,?,?,?,?)";
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query)) {
            pre.setInt(1, c.getCID());
            pre.setInt(2,c.getEID());
            pre.setString(3, c.getShiftType().name());
            pre.setString(4, c.getDay().name());
            pre.setString(5, c.getReason());
            res = pre.executeUpdate()>0;
        } catch (Exception e) {
            //System.out.println("[insertConstConstraint] ->" +e.getMessage());
        }finally {
            if (res)
                constraints.put(c.getCID(), c);
        }
    }


    public void insertTempConstraint(TempConstraint c) {
        if(!constraintExist(c.getEID(),c.getDate(),c.getShiftType().name())) {
            boolean res = false;
            String query = "INSERT INTO TempConstraints (CID,EID,ShiftType,Date,Reason) VALUES (?,?,?,?,?)";
            try (Connection con = connect();
                 PreparedStatement pre = con.prepareStatement(query)) {
                pre.setInt(1, c.getCID());
                pre.setInt(2, c.getEID());
                pre.setString(3, c.getShiftType().name());
                pre.setString(4, c.getDate().toString());
                pre.setString(5, c.getReason());
                res = pre.executeUpdate() > 0;
            } catch (Exception e) {
                  //System.out.println("[insertTempConstrain] ->" +e.getMessage());
            } finally {
                if (res)
                    constraints.put(c.getCID(), c);
            }
        }
    }

    public boolean constraintExist(int eid, LocalDate date,String shiftType) {
        String query1 = "SELECT * FROM TempConstraints WHERE EID=? AND Date=? AND ShiftType=?";
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query1)) {
            pre.setInt(1,eid);
            pre.setString(2,date.toString());
            pre.setString(3,shiftType);
            ResultSet res = pre.executeQuery();
            return res.next();
        } catch (Exception e) {
            //System.out.println("[constraintExist] ->" +e.getMessage());
            return false;
        }
    }

    public void deleteConstraint(int cid) {
        String query = String.format("DELETE FROM ConstConstraints WHERE CID= %d",cid);
        String query2 = String.format("DELETE FROM TempConstraints WHERE CID= %d",cid);
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query);PreparedStatement pre2 = con.prepareStatement(query2)) {
            pre.executeUpdate();
            pre2.executeUpdate();
        } catch (Exception e) {
            //System.out.println("[deleteConstraint] ->" +e.getMessage());
        }
        constraints.remove(cid);
    }

    public Map<Integer, Constraint> selectAllConstraints() {
        String query1 = "SELECT * FROM ConstConstraints";
        String query2 = "SELECT * FROM TempConstraints";
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query1);PreparedStatement pre2 = con.prepareStatement(query2)) {
            ResultSet res = pre.executeQuery();
            ResultSet res2 = pre2.executeQuery();
            while (res.next()) {
                addConstConstraintToMap(res);
            }
            while (res2.next()) {
                addTempConstraintToMap(res2);
            }
        } catch (Exception e) {
            //System.out.println("[selectAllConstraints] ->" +e.getMessage());
        }
        return constraints;
    }

    private Constraint addConstConstraintToMap(ResultSet res) throws SQLException {
        int CID = res.getInt("CID");
        if (constraints.containsKey(CID))
            return constraints.get(CID);
        int EID = res.getInt("EID");
        String shiftType = res.getString("ShiftType");
        DayOfWeek day = DayOfWeek.valueOf(res.getString("DayOfWeek"));
        String reason = res.getString("Reason");
        ConstConstraint constCon = new ConstConstraint(CID, EID, day, shiftType, reason);
        constraints.put(constCon.getCID(), constCon);
        return constCon;
    }

    private Constraint addTempConstraintToMap(ResultSet res) throws SQLException {
        int CID = res.getInt("CID");
        if (constraints.containsKey(CID))
            return constraints.get(CID);
        int EID = res.getInt("EID");
        String shiftType = res.getString("ShiftType");
        LocalDate date = LocalDate.parse(res.getString("Date"));
        String reason = res.getString("Reason");
        TempConstraint tempCon = new TempConstraint(CID,EID,date,shiftType,reason);
        constraints.put(tempCon.getCID(), tempCon);
        return tempCon;
    }


    public List<Constraint> getConstraint(int eid, LocalDate date) {
        List<Constraint> listCon = new ArrayList<>();
        String query1 = "SELECT * FROM TempConstraints WHERE EID=? AND Date=?";
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query1)) {
            pre.setInt(1,eid);
            pre.setString(2,date.toString());
            ResultSet res = pre.executeQuery();
            while (res.next()) {

                    addTempConstraintToMap(res);
                listCon.add(constraints.get(res.getInt("CID")));
            }
        } catch (Exception e) {
            //System.out.println("[getConstraint1] ->" +e.getMessage());
        }
        return listCon;
    }

    public Constraint getConstraint(int cid) {
        if (constraints.containsKey(cid))
            return constraints.get(cid);
        else {
            String query1 = "SELECT * FROM TempConstraints WHERE CID=?";
            String query2 = "SELECT * FROM ConstConstraints WHERE CID=?";
            try (Connection con = connect();
                 PreparedStatement pre = con.prepareStatement(query1); PreparedStatement pre2 = con.prepareStatement(query2)) {
                pre.setInt(1, cid);
                ResultSet res = pre.executeQuery();
                if (res.next()) {
                    return addTempConstraintToMap(res);
                } else {
                    pre2.setInt(1, cid);
                    ResultSet res2 = pre2.executeQuery();
                    if (res2.next()) {
                        return addConstConstraintToMap(res);
                    }
                }
            } catch (Exception e) {
                //System.out.println("[getConstraint2] ->" +e.getMessage());
            }
            return null;
        }
    }

    public List<Constraint> getConstraintsOfEID(int eid) {
        ArrayList<Constraint> consList = new ArrayList<>();
        String query1 ="SELECT * FROM ConstConstraints WHERE EID=?";
        String query2 = "SELECT * FROM TempConstraints WHERE EID=?";
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query1);PreparedStatement pre2 = con.prepareStatement(query2)) {
            pre.setInt(1,eid);
            pre2.setInt(1,eid);
            ResultSet res = pre.executeQuery();
            ResultSet res2 = pre2.executeQuery();
            while (res.next()) {
                Constraint c =  addConstConstraintToMap(res);
                consList.add(c);
            }
            while (res2.next()) {
                Constraint c = addTempConstraintToMap(res2);
                consList.add(c);
            }
        } catch (Exception e) {
            //System.out.println("[getConstraintsOfEID] ->" +e.getMessage());
        }
        return consList;
    }

    public void updateReason(int cid, String newReason) {
        updateIntString(cid,newReason,"Reason","CID","ConstConstraints");
        updateIntString(cid,newReason,"Reason","CID","TempConstraints");
    }

    public void updateShiftType(int cid, String newType) {
        updateIntString(cid,newType,"ShiftType","CID","ConstConstraints");
        updateIntString(cid,newType,"ShiftType","CID","TempConstraints");
    }
}

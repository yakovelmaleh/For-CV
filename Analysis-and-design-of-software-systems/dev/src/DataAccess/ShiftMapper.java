package DataAccess;

import Business.Employees.EmployeePKG.Employee;
import Business.Employees.ShiftPKG.Shift;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftMapper extends Mapper {
    private static ShiftMapper shiftMapper = null;
    private final HashMap<Integer, Shift> shifts;
    private Map<String, Map<String, Integer>> defaults;

    public static ShiftMapper getInstance() {
        if (shiftMapper == null)
            shiftMapper = new ShiftMapper();
        return shiftMapper;
    }

    private ShiftMapper() {
        super();
        shifts = new HashMap<>();
        defaults = new HashMap<>();
    }


    public void insertNewShift(Shift s) {
        boolean res = false;
        String query = "INSERT INTO Shifts (SID,Date,ShiftType,WasSelfMake,BID) VALUES (?,?,?,?,?)";
        List<String> qrs = new ArrayList<>();
        Map<String, Integer> roles = s.getRolesAmountDB();
        roles.forEach((role, amount) -> {
            qrs.add(String.format("INSERT INTO ShiftsAndRolesAmount VALUES(%d,'%s',%d)", s.getSID(), role, amount));
        });
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query);
             Statement sts = con.createStatement()) {
            pre.setInt(1, s.getSID());
            pre.setString(2, s.getDate().toString());
            pre.setString(3, s.getShiftType().name());
            pre.setBoolean(4, s.WasSelfMake());
            pre.setInt(5, getCurrBranchID()==0?1:getCurrBranchID());
            res = pre.executeUpdate() > 0;
            for (String q : qrs) {
                sts.addBatch(q);
            }
            sts.executeBatch();
        } catch (Exception e) {
            //System.out.println("[insertNewShift]" + e.getMessage());
        } finally {
            if (res)
                shifts.put(s.getSID(), s);
        }
    }

    public void insertEmpToShift(int sid, int eid, String role) {
        String query = "INSERT INTO ShiftsAndEmployees (SID,EID,Role) VALUES (?,?,?)";
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query)) {
            pre.setInt(1, sid);
            pre.setInt(2, eid);
            pre.setString(3, role);
            pre.executeUpdate();
        } catch (Exception e) {
            //System.out.println("[insertEmpToShift]" + e.getMessage());
        }
    }

    private List<Shift> loadShifts(String query1, String query2) {
        Map<Integer, Shift> shiftsFU = new HashMap<>();
        Map<Integer, Map<Integer, String>> SER = new HashMap<>();
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query1); PreparedStatement pre2 = con.prepareStatement(query2)) {
            pre.setInt(1, getCurrBranchID());
            ResultSet res = pre.executeQuery();
            ResultSet res2 = pre2.executeQuery();
            while (res.next()) {
                int SID = res.getInt("SID");
                if (!shifts.containsKey(SID)) {
                    Map<String, Integer> rolesAmount = new HashMap<>();
                    int amount = res.getInt("Amount");
                    String role = (res.getString("Role"));
                    rolesAmount.put(role, amount);
                    Shift s = new Shift(SID, rolesAmount, LocalDate.parse(res.getString("Date")), res.getString("ShiftType"), res.getBoolean("WasSelfMake"));
                    shiftsFU.put(SID, s);
                    shifts.put(SID, s);
                } else {
                    Shift s = shifts.get(SID);
                    int amount = res.getInt("Amount");
                    String role = (res.getString("Role"));
                    s.addRoleAmount(role,amount);
                    if (!shiftsFU.containsKey(s.getSID()))
                        shiftsFU.put(s.getSID(), s);
                }
            }
            while (res2.next()) {
                int SID = res2.getInt("SID");
                if (shifts.get(SID).getEmployees() != null) {
                    if (!SER.containsKey(SID)) {
                        SER.put(SID, new HashMap<>());
                    }
                    SER.get(SID).put(res2.getInt("EID"), res2.getString("Role"));
                }
            }
        } catch (Exception e) {
            //System.out.println("[loadShifts]" + e.getMessage());
        }
        shiftsFU.forEach((sid, shift) -> {
            if (SER.containsKey(sid)) {
                Map<Integer, String> emps = SER.get(sid);
                Map<Employee, String> employees = new HashMap<>();
                emps.forEach((eid, role) -> {
                    Employee emp = EmployeeMapper.getInstance().get(eid);
                    employees.put(emp, role);
                });
                shift.setEmployees(employees);
            }
        });
        return new ArrayList<>(shiftsFU.values());

    }

    public List<Shift> selectShiftsFromUntil(LocalDate from, LocalDate until) {
        String query1 = String.format("SELECT s.SID,Date,ShiftType,WasSelfMake,Role,Amount " +
                "FROM Shifts as s JOIN ShiftsAndRolesAmount as sar" +
                " ON s.SID = sar.SID AND BID = ? " +
                " WHERE Date >= '%s' AND Date <= '%s'", from.toString(), until.toString());
        String query2 = String.format("SELECT sae.* FROM ShiftsAndEmployees as sae JOIN Shifts as s " +
                "ON sae.SID = s.SID WHERE BID = %d AND Date >= '%s' AND Date <= '%s'", getCurrBranchID(),from.toString(),until.toString());
        return loadShifts(query1, query2);
    }

    public Shift getShift(int sid) {
        if (shifts.containsKey(sid))
            return shifts.get(sid);
        String query1 = String.format("SELECT s.SID,Date,ShiftType,WasSelfMake,Role,Amount " +
                "FROM Shifts as s JOIN ShiftsAndRolesAmount as sar" +
                " ON s.SID = sar.SID AND BID = ?  " +
                " WHERE s.SID = %d", sid);
        String query2 = String.format("SELECT sae.* FROM ShiftsAndEmployees as sae JOIN Shifts as s " +
                "ON sae.SID = s.SID WHERE BID = %d AND s.SID = %d", getCurrBranchID(), sid);
        List<Shift> ls = loadShifts(query1, query2);
        return ls.isEmpty()? null : ls.get(0);
    }

    public List<Shift> getShiftsOfEID(int eid) {
        String query1 = String.format("SELECT s.SID,Date,ShiftType,WasSelfMake,sar.Role,Amount" +
                " FROM ShiftsAndEmployees as sae JOIN Shifts as s ON sae.SID = s.SID" +
                " JOIN ShiftsAndRolesAmount as sar ON sar.SID = s.SID" +
                " WHERE EID = %d AND BID = ?", eid);
        String query2 = String.format("SELECT sae.* FROM ShiftsAndEmployees as sae JOIN Shifts as s ON sae.SID = s.SID" +
                " WHERE BID = %d AND EID = %d", getCurrBranchID(), eid);
        return loadShifts(query1, query2);
    }

    public Shift getShiftByDate(LocalDate date, String shiftType) {
        for (Map.Entry<Integer, Shift> s : shifts.entrySet()) {
            if (s.getValue().getDate().isEqual(date) && s.getValue().getShiftType().name().equals(shiftType))
                return s.getValue();
        }
        String query = String.format("SELECT SID FROM Shifts WHERE BID = %d AND Date = '%s' AND ShiftType = '%s'"
                , getCurrBranchID(), date.toString(), shiftType);
        int sid = -1;
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query)) {
            ResultSet res = pre.executeQuery();
            if (res.next())
                sid = res.getInt("SID");
        } catch (Exception e) {
            //System.out.println("[getShiftByDate]" + e.getMessage());
        }
        if (sid == -1)
            return null;
        return getShift(sid);
    }



    public void updateWasSelfMake(int sid, boolean wasSelfMake) {
        updateIntboolean(sid, wasSelfMake, "WasSelfMake", "SID", "Shifts");
    }

    public void deleteEmpFromShift(int sid, int eid, String roleOfRemoved) {
        String query = String.format("DELETE FROM ShiftsAndEmployees WHERE SID= ? And EID=? AND Role= '%s'",roleOfRemoved);
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query)) {
            pre.setInt(1, sid);
            pre.setInt(2, eid);
            pre.executeUpdate();
        } catch (Exception e) {
            //System.out.println("[deleteEmpFromShift]" + e.getMessage());
        }
    }

    public void updateAmountRole(int sid, int newAmount, String role) {
        String query = "UPDATE ShiftsAndRolesAmount SET Amount= ? WHERE SID= ? And Role= ?";
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query)) {
            pre.setInt(1, newAmount);
            pre.setInt(2, sid);
            pre.setString(3, role);
            pre.executeUpdate();
        } catch (Exception e) {
            //System.out.println("[updateAmountRole]" + e.getMessage());
        }
    }

    public int getNextSID() {
            int nextID = 1;
            ResultSet res;
            String query = "SELECT Max(SID)+1 as nextID FROM Shifts";
            try (Connection con = connect(); PreparedStatement pre = con.prepareStatement(query)) {
                res = pre.executeQuery();
                if (res.next())
                    nextID = res.getInt("nextID") == 0? 1: res.getInt("nextID");
            } catch (Exception e) {
                //System.out.println("[getNextID] ->" +e.getMessage());
            }
            return nextID;
    }

    public Map<String, Map<String, Integer>> getDefaults() {
        if (hasDefaultShifts()) {
            return defaults;
        }
        ResultSet res = null;
        String query = String.format("SELECT * FROM Defaults WHERE BID =%d", getCurrBranchID());
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query)) {
            res = pre.executeQuery();
            if (res != null) {
                try {
                    if (!res.next())
                        return null;
                    while (res.next()) {
                        String shiftType = res.getString("ShiftType");
                        if (!defaults.containsKey(shiftType)) {
                            defaults.put(shiftType, new HashMap<>());
                        }
                        defaults.get(shiftType).put(res.getString("Role"), res.getInt("Amount"));
                    }
                } catch (Exception e) {
                    //System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            //System.out.println("[getDefaults] -> " + e.getMessage());
        }
        return defaults;
    }

    public boolean hasDefaultShifts() {
        return !defaults.isEmpty();
    }

    public void insertDefaults(Map<String, Map<String, Integer>> defaults) {
        List<String> inserts = new ArrayList<>();
        defaults.forEach((s, mapRoleAmount) -> {
            mapRoleAmount.forEach((role, amount) -> {
                inserts.add(String.format("INSERT INTO Defaults VALUES('%s','%s',%d,%d)", s, role, amount, getCurrBranchID()));
            });
        });
        try (Connection con = connect();
             Statement queries = con.createStatement()) {
            for (String q : inserts)
                queries.addBatch(q);
            queries.executeBatch();
        } catch (Exception e) {
            //System.out.println("[insertDefaults]-> " + e.getMessage());
        }
        this.defaults = defaults;
    }

    @Override
    public void setCurrBranchID(int currBranchID) {
        super.setCurrBranchID(currBranchID);
        shifts.clear();
        defaults.clear();
        getDefaults();
    }

}

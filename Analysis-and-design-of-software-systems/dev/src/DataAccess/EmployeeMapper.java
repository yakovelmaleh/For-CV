package DataAccess;

import Business.Employees.EmployeePKG.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmployeeMapper extends Mapper {
    private static EmployeeMapper empMapper = null;
    private final HashMap<Integer, Employee> employees;
    private final String tableName = "Employees";
    private final String id = "EID";
    private boolean needToUpdateEmps = true;

    public static EmployeeMapper getInstance() {
        if (empMapper == null)
            empMapper = new EmployeeMapper();
        return empMapper;
    }


    private EmployeeMapper() {
        super();
        employees = new HashMap<>();
        createTables();
    }

    public Employee get(int EID) {
        Employee emp = null;
        ResultSet res;
        if (employees.containsKey(EID))
            return employees.get(EID);
        String query = String.format("SELECT * FROM %s WHERE %s=%d AND BID=%d", tableName, this.id, EID, getCurrBranchID());
        try (Connection con = connect(); PreparedStatement pre = con.prepareStatement(query)) {
            res = pre.executeQuery();
            if (res.next()) {
                int[] bankDetails = {res.getInt("AccountNumber"), res.getInt("BankID"), res.getInt("BranchNumber")};
                int[] terms = {res.getInt("EducationFund"), res.getInt("DaysOff"), res.getInt("SickDays")};
                emp = new Employee(res.getInt("EID"), res.getString("Name"), bankDetails, res.getInt("Salary"),
                        LocalDate.parse(res.getString("StartWorkingDate")), terms);
                employees.put(emp.getEID(), emp);
            }
        } catch (Exception e) {
            //System.out.println("[get] ->" + e.getMessage());
        } finally {
            if (emp != null)
                loadAllRoles(emp);
        }
        return emp;
    }

    public HashMap<Integer, Employee> getEmployees() {
        return employees;
    }


    public boolean containsKey(int eid) {
        if (employees.containsKey(eid))
            return true;
        return get(eid) != null;
    }

    private void loadAllRoles(Employee emp) {
        String query = "SELECT * FROM RolesAndEmployees WHERE EID= ?";
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query)) {
            pre.setInt(1, emp.getEID());
            ResultSet res = pre.executeQuery();
            List<String> roles = new ArrayList<>();
            while (res.next()) {
                roles.add(res.getString("Role"));
            }
            emp.setRole(roles);
        } catch (Exception e) {
            //System.out.println("[loadAllRoles] ->" + e.getMessage());
        }
    }


    public boolean insert(Employee emp,boolean driver) {
        boolean res = false;
        String query1 = String.format("INSERT INTO %s VALUES(?,?,?,?,?,?,?,?,?,?,?,?);", tableName);
        String query2 = "INSERT INTO RolesAndEmployees VALUES(?,?)";
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query1);
             PreparedStatement pre2 = con.prepareStatement(query2)) {
            pre.setInt(1, emp.getEID());
            pre.setString(2, emp.getName());
            pre.setString(3, emp.getStartWorkingDate().toString());
            pre.setInt(4, emp.getSalary());
            pre.setInt(5, emp.getBankAccount().getBankID());
            pre.setInt(6, emp.getBankAccount().getBankBranch());
            pre.setInt(7, emp.getBankAccount().getAccountNum());
            pre.setInt(8, emp.getTermsOfEmployment().getEducationFun());
            pre.setInt(9, emp.getTermsOfEmployment().getDaysOff());
            pre.setInt(10, emp.getTermsOfEmployment().getSickDays());
            pre.setInt(11, 1);
            pre.setInt(12, driver? -1 :getCurrBranchID());
            pre2.setInt(1, emp.getEID());
            pre2.setString(2, emp.getRole().get(0).name());
            res = pre.executeUpdate() > 0;
            res = res && pre2.executeUpdate() > 0;
        } catch (Exception e) {
            //System.out.println("[insert-emp] ->" + e.getMessage());
        } finally {
            if (res)
                employees.put(emp.getEID(), emp);
        }
        return res;
    }


    public List<Integer> getAllBranches() {
        List<Integer> branches = new ArrayList<>();
        String query = "SELECT BID FROM Branches";
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query)) {
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                branches.add(res.getInt("BID"));
            }
        } catch (Exception e) {
            //System.out.println("[getAllBranches-emp] ->" + e.getMessage());
        }
        return branches;
    }



    public Employee delete(int fireEID) {
        Employee removed = employees.remove(fireEID);
        updateIntInt(fireEID, 0, "Active", id, tableName);
        return removed;
    }


    public void updateName(int updateEID, String newName) {
        updateIntString(updateEID, newName, "Name", id, tableName);
    }

    public void updateSalary(int updateEID, int newSalary) {
        updateIntInt(updateEID, newSalary, "Salary", id, tableName);
    }

    public void updateBankAccountNum(int updateEID, int newAccountNumber) {
        updateIntInt(updateEID, newAccountNumber, "AccountNumber", id, tableName);
    }

    public void updateBankBranch(int updateEID, int newBranch) {
        updateIntInt(updateEID, newBranch, "BranchNumber", id, tableName);
    }

    public void updateBankID(int updateEID, int newBankID) {
        updateIntInt(updateEID, newBankID, "BankID", id, tableName);
    }

    public void updateEducationFund(int updateEID, int newEducationFund) {
        updateIntInt(updateEID, newEducationFund, "EducationFund", id, tableName);
    }

    public void updateDaysOff(int updateEID, int newAmount) {
        updateIntInt(updateEID, newAmount, "DaysOff", id, tableName);
    }

    public void updateSickDays(int updateEID, int newAmount) {
        updateIntInt(updateEID, newAmount, "SickDays", id, tableName);
    }


    public List<Employee> loadEmployeesInBranch() {
        if (needToUpdateEmps) {
            String query = String.format("SELECT * FROM %s as E JOIN RolesAndEmployees as R ON E.EID=R.EID WHERE (BID=? OR BID=-1) AND Active = 1", tableName);
            try (Connection con = connect();
                 PreparedStatement pre = con.prepareStatement(query)) {
                pre.setInt(1, getCurrBranchID());
                ResultSet res = pre.executeQuery();
                while (res.next()) {
                    if (!employees.containsKey(res.getInt(id))) {
                        int[] bankDetails = {res.getInt("AccountNumber"), res.getInt("BankID"), res.getInt("BranchNumber")};
                        int[] terms = {res.getInt("EducationFund"), res.getInt("DaysOff"), res.getInt("SickDays")};
                        Employee emp = new Employee(res.getInt("EID"), res.getString("Name"), bankDetails, res.getInt("Salary"),
                                LocalDate.parse(res.getString("StartWorkingDate")), terms);
                        emp.addRole(res.getString("Role"));
                        employees.put(emp.getEID(), emp);
                    } else {
                        String role = res.getString("Role");
                        Employee employee = employees.get(res.getInt(id));
                        employee.addRole(role);
                    }
                }
            } catch (Exception e) {
                //System.out.println("[loadEmployeesInBranch-emp] ->" + e.getMessage());
            }
            needToUpdateEmps = false;
            return new ArrayList<>(employees.values());
        } else return new ArrayList<>(employees.values());
    }

    public void addRole(int eid, String role) {
        String addRole = String.format("INSERT INTO RolesAndEmployees VALUES (%d,'%s')", eid, role);
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(addRole)) {
            pre.executeUpdate();
        } catch (Exception e) {
            //System.out.println("[addRole]-> " + e.getMessage());
        }
    }

    public void resetEmps() {
        employees.clear();
        needToUpdateEmps = true;
    }

    public List<String> getManagerMessages(int bid, int eid) {
        String query = String.format("SELECT * FROM ManagerAlerts WHERE BID= %d AND EID=%d AND Date >= date('now')",bid,eid);
        List<String> l = new ArrayList<>();
        List<Integer> mids = new ArrayList<>();
        try (Connection con = connect();
             PreparedStatement pre = con.prepareStatement(query);
                Statement deletes = con.createStatement()) {
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                l.add(res.getString("Message"));
                mids.add((res.getInt("MID")));
            }
            for (Integer mid : mids){
                deletes.addBatch(String.format("DELETE FROM ManagerAlerts WHERE MID=%d;",mid));
            }
            deletes.executeBatch();

        } catch (Exception e) {
            //System.out.println("[getManagerMessages-emp] ->" + e.getMessage());
        }
        return l;
    }
}

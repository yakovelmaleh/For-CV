package Business.Employees.ShiftPKG;

import Business.Employees.EmployeePKG.Employee;
import Business.Type.RoleType;
import Business.Type.ShiftType;
import DataAccess.ShiftMapper;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.util.*;

public class Shift {
    private final static Logger log = Logger.getLogger(Shift.class);
    //-------------------------------------fields------------------------------------

    private final int SID;
    private Map<Employee, RoleType> employees;
    private Map<RoleType, Integer> rolesAmount;
    private Map<RoleType, List<Employee>> optionals;
    private boolean complete;  //employees == roles amount
    private final LocalDate date;
    private final ShiftType shiftType;
    private boolean wasSelfMake;
    private boolean hasShiftManager;

    //------------------------------------constructor--------------------------------

    public Shift(int SID, Map<RoleType, Integer> rolesAmount, Map<RoleType, List<Employee>> optionals, LocalDate date, ShiftType shiftType) {
        this.SID = SID;
        employees = new HashMap<>();
        this.rolesAmount = rolesAmount;
        insertMissingRolesAmount();
        this.optionals = optionals;
        this.complete = false;
        this.date = date;
        this.shiftType = shiftType;
        this.wasSelfMake = false;
        if (optionals.containsKey(RoleType.ShiftManager))
            hasShiftManager = !optionals.get(RoleType.ShiftManager).isEmpty();
        log.debug("shift: " + SID + " created");
    }

    //for load shift
    public Shift(int SID, Map<String, Integer> rolesAmount, LocalDate date, String shiftType, boolean wasSelfMake) {
        this.SID = SID;
        employees = new HashMap<>();
        this.rolesAmount = new HashMap<>();
        rolesAmount.forEach((s, integer) -> this.rolesAmount.put(RoleType.valueOf(s), integer));
        this.optionals = null;
        this.date = date;
        this.shiftType = ShiftType.valueOf(shiftType);
        this.wasSelfMake = wasSelfMake;
        this.hasShiftManager = false;
        log.debug("shift: " + SID + " created");
    }


    //-------------------------------------methods----------------------------------

    //shift controller call this function after create shift to insert shift manager to this shift
    public List<Employee> insertShiftManager() {
        Employee sm = optionals.get(RoleType.ShiftManager).remove(0);
        employees.put(sm, RoleType.ShiftManager);
        ShiftMapper.getInstance().insertEmpToShift(SID, sm.getEID(), RoleType.ShiftManager.name());
        List<Employee> listOfSM = new LinkedList<>();
        listOfSM.add(sm);
        return listOfSM;
    }

    public List<Employee> self_make() {
        if (!wasSelfMake) {
            for (Map.Entry<RoleType, Integer> e : rolesAmount.entrySet()) {
                if (e.getKey().equals(RoleType.ShiftManager)) continue;
                RoleType role = e.getKey();
                int amount = e.getValue();
                while (amount > 0) {
                    if (optionals.get(role) == null || optionals.get(role).isEmpty()) {
                        break;
                    } else {
                        Employee emp = optionals.get(role).remove(0);  // delete from optionals
                        employees.put(emp, role);  //add to employees
                        ShiftMapper.getInstance().insertEmpToShift(SID, emp.getEID(), role.name());
                        removeEmpFromOptionals(emp);
                        amount--;
                    }
                }
            }
            ArrayList<Employee> emps = new ArrayList<>();
            employees.forEach((emp, roleType) -> emps.add(emp));
            updateComplete();
            wasSelfMake = true;
            ShiftMapper.getInstance().updateWasSelfMake(SID, wasSelfMake);
            return emps;
        }
        return new ArrayList<>();
    }

    public void addEmpToShift(RoleType role, Employee emp) {
        employees.put(emp, role);
        ShiftMapper.getInstance().insertEmpToShift(SID, emp.getEID(), role.name());
        removeEmpFromOptionals(emp);
        updateComplete();
        if (role.equals(RoleType.ShiftManager))
            hasShiftManager = true;
        log.debug("EID: " + emp.getEID() + "added to SID: " + SID);
    }

    public void updateComplete() {
        boolean newComplete = isComplete();
        if (newComplete != complete)
            complete = newComplete;
    }

    public void removeEmpFromShift(Employee emp) {
        RoleType roleOfRemoved = employees.remove(emp);  //return null if EID isn't in this shift
        ShiftMapper.getInstance().deleteEmpFromShift(SID, emp.getEID(), roleOfRemoved.name());
        updateComplete();
        if (roleOfRemoved.equals(RoleType.ShiftManager))
            hasShiftManager = false;
        (emp.getRole()).forEach(roleType -> {
            optionals.get(roleType).add(emp);
        });
        log.debug("EID: " + emp.getEID() + "removed from SID: " + SID);
    }

    public void removeFireEmp(Employee emp) {
        RoleType roleOfRemoved = employees.remove(emp);  //remove employee if he is in this shift
        if (roleOfRemoved != null) {
            ShiftMapper.getInstance().deleteEmpFromShift(SID, emp.getEID(), roleOfRemoved.name());
            complete = false;
            if (roleOfRemoved.equals(RoleType.ShiftManager))
                hasShiftManager = false;
        }
        for (Map.Entry<RoleType, List<Employee>> o : optionals.entrySet()) {
            List<Employee> listOfEmp = o.getValue();
            if (listOfEmp != null) listOfEmp.remove(emp);
        }
        log.debug("EID: " + emp.getEID() + " removed(fire) from SID: " + SID);
    }

    public void addToOptionals(Employee emp, RoleType role) {
        if (!optionals.get(role).contains(emp))
            optionals.get(role).add(emp);
    }

    public void removeEmpFromOptionals(Employee emp) {
        for (Map.Entry<RoleType, List<Employee>> e : optionals.entrySet()) {
            List<Employee> l = e.getValue();
            if (!l.isEmpty())
                l.remove(emp);
        }
        log.debug("EID: " + emp.getEID() + " remove from optionals");
    }


    public List<Employee> updateRolesAmount(RoleType role, int newAmount) {
        int oldAmount = rolesAmount.get(role);
        rolesAmount.replace(role, newAmount);
        ShiftMapper.getInstance().updateAmountRole(SID, newAmount, role.name());
        List<Employee> toRemove = new ArrayList<>();
        for (int i = 0; i < oldAmount - newAmount; i++) {
            for (Map.Entry<Employee, RoleType> m : employees.entrySet()) {
                if (m.getValue().equals(role)) {
                    removeEmpFromShift(m.getKey());
                    break;
                }
            }
        }
        updateComplete();
        return toRemove;
    }


    private boolean roleIsFull(RoleType role) {
        int amount = rolesAmount.get(role);
        long count = employees.entrySet().stream().filter(x -> x.getValue().name().equals((role.name()))).count();
        return count >= amount;
    }


    private boolean isComplete() {
        int amount = 0;
        for (Map.Entry<RoleType, Integer> role : rolesAmount.entrySet())
            amount += role.getValue();
        return amount == employees.size();
    }

    //check if there is employee that optionals and his role not full
    public boolean optionalIsEmpty() {
        for (Map.Entry<RoleType, List<Employee>> role : optionals.entrySet()) {
            if (role.getKey().equals(RoleType.Driver) || role.getKey().equals(RoleType.StoreKeeper))
                    continue;
            if (!role.getValue().isEmpty() && !roleIsFull(role.getKey()))
                return false;
        }
        return true;
    }

    public boolean EIDIsOptionForSID(Employee emp) {
        for (Map.Entry<RoleType, List<Employee>> role : optionals.entrySet()) {
            if (role.getValue().contains(emp) && !roleIsFull(role.getKey()))
                return true;
        }
        return false;
    }

    public boolean canWork(Employee emp, RoleType role) {
        //check if EID can work in this shift (in optionals)
        //check if there is empty role for this roleType in rolesAmount
        List<Employee> list = optionals.get(role);
        boolean canWork = list.contains(emp);
        return canWork && !roleIsFull((role));
    }


    public void addRoleAmount(String role, int amount) {
        RoleType roleType = RoleType.valueOf(role);
        if (rolesAmount.containsKey(roleType)) {
            if (rolesAmount.get(roleType) != amount)
                rolesAmount.replace(roleType, amount);
        } else
            rolesAmount.put(roleType, amount);
    }

    public void setOptionals(Map<RoleType, List<Employee>> ops) {
        this.optionals = ops;
    }

    public void setHasShiftManager() {
        employees.forEach((employee, roleType) -> {
            if (roleType.equals(RoleType.ShiftManager))
                hasShiftManager = true;
        });
    }

    public void setEmployees(Map<Employee, String> employees) {
        this.employees = new HashMap<>();
        employees.forEach((emp, role) -> this.employees.put(emp, RoleType.valueOf(role)));
    }

    public Map<String, Integer> getRolesAmountDB() {
        Map<String, Integer> res = new HashMap<>();
        rolesAmount.forEach((roleType, amount) -> {
            res.put(roleType.name(), amount);
        });
        return res;
    }

    public void insertMissingRolesAmount() {
        EnumSet<RoleType> allRoles = EnumSet.allOf(RoleType.class);
        for (RoleType role : allRoles) {
            if (!rolesAmount.containsKey(role))
                rolesAmount.put(role, 0);
        }
    }

    public boolean StoreKeeperAvailable() {
        return !optionals.get(RoleType.StoreKeeper).isEmpty();
    }


    public boolean DriverAvailable() {
        return !optionals.get(RoleType.Driver).isEmpty();
    }

    public List<Integer> getAllAvailableDrivers() {
        List<Integer> ld = new ArrayList<>();
        optionals.get(RoleType.Driver).forEach(employee -> ld.add(employee.getEID()));
        return ld;
    }

    public Employee addStoreKeeper() {
        Employee emp = optionals.get(RoleType.StoreKeeper).get(0);
        addEmpToShift(RoleType.StoreKeeper,emp);
        return emp;
    }
    //-------------------getters&setters----------------------------------------

    public LocalDate getDate() {
        return date;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public Map<Employee, RoleType> getEmployees() {
        return employees;
    }

    public int getSID() {
        return SID;
    }

    public Map<RoleType, Integer> getRolesAmount() {
        return rolesAmount;
    }

    public Map<RoleType, List<Employee>> getOptionals() {
        return optionals;
    }

    public boolean getComplete() {
        return complete;
    }

    public boolean WasSelfMake() {
        return wasSelfMake;
    }

    public boolean HasShiftManager() {
        return hasShiftManager;
    }


    public void incrementDriverStoreKeeper() {
        int before = rolesAmount.get(RoleType.Driver);
        updateRolesAmount(RoleType.Driver,before+1);
        updateRolesAmount(RoleType.StoreKeeper,before+1);
    }

    public void removeDriverAndStorekeeper(int driverID) {

    }

    public Employee getStoreKeeperToRemove() {
        for(Map.Entry<Employee,RoleType> e : employees.entrySet()){
            if(e.getValue().equals(RoleType.StoreKeeper))
                return e.getKey();
        }
        return null; //never
    }

    public void decrementDriverAndStoreKeeper() {
        int before = rolesAmount.get(RoleType.Driver);
        updateRolesAmount(RoleType.Driver,before-1);
        updateRolesAmount(RoleType.StoreKeeper,before-1);
    }
}

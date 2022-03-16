package Business.Employees.ShiftPKG;

import Business.Employees.EmployeePKG.Employee;
import Business.Type.RoleType;
import Business.Type.ShiftType;
import DataAccess.ConstraintMapper;
import DataAccess.ShiftMapper;
import org.apache.log4j.Logger;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class ShiftController {
    private final static Logger log = Logger.getLogger(ShiftController.class);
    //-------------------------------------fields------------------------------------

    private Map<RoleType, List<Employee>> AllOptionals;

    //------------------------------------constructor---------------------------------

    public ShiftController(Map<RoleType, List<Employee>> optionals) {
        this.AllOptionals = optionals;
        log.debug("finished constructor shiftController");
    }

    //--------------------------------------methods----------------------------------

    public void addConstConstraint(int EID, DayOfWeek day, ShiftType shiftType, String reason) {
        //create constraint and add to constraints field
        int CID = ConstraintMapper.getInstance().getNextCID();
        ConstConstraint newCon = new ConstConstraint(CID, EID, day, shiftType, reason);
        ConstraintMapper.getInstance().insertConstConstraint(newCon);
        log.debug("added new const constraint for EID: " + EID + ", Day: " + day.name() + " , ShiftType: " + shiftType);
    }

    public void addTempConstraint(Employee emp, LocalDate c_date, ShiftType shiftType, String reason) {
        Shift s = getShiftByDate(c_date, shiftType);
        int CID = ConstraintMapper.getInstance().getNextCID();
        TempConstraint newCon = new TempConstraint(CID, emp.getEID(), c_date, shiftType, reason);
        ConstraintMapper.getInstance().insertTempConstraint(newCon);
        s.removeEmpFromOptionals(emp);
        log.debug("added new const constraint for EID: " + emp.getEID() + ", Date: " + c_date + " , ShiftType: " + shiftType);
    }

    public void removeConstraint(int CID) {
        ConstraintMapper.getInstance().deleteConstraint(CID);
    }

    public void defaultShifts(Map<ShiftType, Map<RoleType, Integer>> defaultShifts) {
        ShiftMapper.getInstance().insertDefaults(convertToString(defaultShifts));
        log.debug("default shifts defined");
    }


    //assume that sunday-thursday will be default shifts and on friday only morning shift.
    public List<Shift> createWeekShifts() {
        LocalDate date = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
        List<Shift> shiftsWithoutShiftManager = new ArrayList<>();
        Map<ShiftType, Map<RoleType, Integer>> defaultShifts = getDefaults();
        for (int i = 0; i < 5; i++) {
            for (Map.Entry<ShiftType, Map<RoleType, Integer>> m : defaultShifts.entrySet()) {
                ShiftType shiftType = m.getKey();
                if (shiftAlreadyCreated(date.plusDays(i), shiftType)) continue;
                Shift s = createShiftPrivate(deepCopyRolesA(defaultShifts.get(shiftType)), date.plusDays(i), shiftType);  // default shift
                if (!s.HasShiftManager())
                    shiftsWithoutShiftManager.add(s);
            }
        }
        Shift friday = createShiftPrivate(deepCopyRolesA(defaultShifts.get(ShiftType.Morning)), date.plusDays(5), ShiftType.Morning);  // default shift
        if (!friday.HasShiftManager())
            shiftsWithoutShiftManager.add(friday);
        return shiftsWithoutShiftManager;
    }

    private Map<RoleType, Integer> deepCopyRolesA(Map<RoleType, Integer> roleTypeIntegerMap) {
        Map<RoleType,Integer> copy = new HashMap<>();
        roleTypeIntegerMap.forEach(copy::put);
        return copy;
    }

    private Map<ShiftType, Map<RoleType, Integer>> getDefaults() {
        Map<ShiftType, Map<RoleType, Integer>> defaults = new HashMap<>();
        Map<String, Map<String, Integer>> df = ShiftMapper.getInstance().getDefaults();
        df.forEach((shiftType, mapRoleAmount) -> {
            defaults.put(ShiftType.valueOf(shiftType),new HashMap<>());
            Map<RoleType,Integer> toAdd = defaults.get(ShiftType.valueOf(shiftType));
            mapRoleAmount.forEach((role, amount) -> {
                toAdd.put(RoleType.valueOf(role),amount);
            });
        });
        return defaults;
    }
    private Map<String, Map<String, Integer>> convertToString(Map<ShiftType, Map<RoleType, Integer>> defaultShifts) {
        Map<String, Map<String, Integer>> df = new HashMap<>();
        defaultShifts.forEach((shiftType, mapRoleAmount) -> {
            df.put(shiftType.name(),new HashMap<>());
            Map<String,Integer> toAdd = df.get(shiftType.name());
            mapRoleAmount.forEach((roleType, amount) -> {
                toAdd.put(roleType.name(),amount);
            });
        });
        return df;
    }


    private Shift createShiftPrivate(Map<RoleType, Integer> rolesAmount, LocalDate date, ShiftType shiftType) {
        //delete from optionals by constraints
        Map<RoleType,List<Employee>> optionals = deepCopy(AllOptionals);
        createOptionals(optionals, date, shiftType);
        int SID = ShiftMapper.getInstance().getNextSID();
        Shift s = new Shift(SID, rolesAmount, optionals, date, shiftType);
        if (s.HasShiftManager()) {
            List<Employee> shiftManager = s.insertShiftManager();
            createBuildConstraintsAndRemoveFromOpt(shiftManager, shiftType, date);  //create build constraint for shift manager
        }
        ShiftMapper.getInstance().insertNewShift(s);
        return s;
    }


    public void createShift(Map<RoleType, Integer> rolesAmount, LocalDate date, ShiftType shiftType) {
        //delete from optionals by constraints
        Map<RoleType,List<Employee>> optionals = deepCopy(AllOptionals);
        createOptionals(optionals, date, shiftType);
        int SID = ShiftMapper.getInstance().getNextSID();
        Shift s = new Shift(SID, rolesAmount, optionals, date, shiftType);
        if (s.HasShiftManager()) {
            List<Employee> shiftManager = s.insertShiftManager();
            createBuildConstraintsAndRemoveFromOpt(shiftManager, shiftType, date);  //create build constraint for shift manager
        }
        ShiftMapper.getInstance().insertNewShift(s);
    }

    private void createOptionals(Map<RoleType, List<Employee>> optionals, LocalDate date, ShiftType shiftType) {
        Map<Integer, Constraint> constraints = ConstraintMapper.getInstance().selectAllConstraints();
        for (Map.Entry<Integer, Constraint> c : constraints.entrySet()) {
            if (c.getValue().relevant(date, shiftType))
                RemoveEmpFromOptionals(c.getValue().getEID(), optionals);
        }
    }

    private void loadOptionals(Map<RoleType, List<Employee>> ops, LocalDate date, ShiftType shiftType, Map<Employee, RoleType> employees) {
        createOptionals(ops,date,shiftType);
        employees.forEach((employee, roleType) -> RemoveEmpFromOptionals(employee.getEID(),ops));
    }


    //self make for all shifts that next week and not was self make
    public void selfMakeWeekShifts() {
        List<Shift> shifts = ShiftMapper.getInstance().selectShiftsFromUntil(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)), LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).plusWeeks(1));
        loadShift(shifts,AllOptionals);
        for (Shift s : shifts) {
            List<Employee> listOfEmployees = s.self_make();// algorithm that choose employees for the shift
            createBuildConstraintsAndRemoveFromOpt(listOfEmployees, s.getShiftType(), s.getDate());  //add constraint for all the employees in this shift cause employee can work in 1 shift per day
        }
    }

    //add to specific role in this Shift
    public void addEmpToShift(int SID, RoleType role, Employee emp) {
        Shift s = get(SID,AllOptionals);
        s.addEmpToShift(role, emp);
        List<Employee> l = new ArrayList<>();
        l.add(emp);
        createBuildConstraintsAndRemoveFromOpt(l,s.getShiftType(),s.getDate());
    }

    private Shift get(int sid,Map<RoleType, List<Employee>> optionals) {
        Shift s = ShiftMapper.getInstance().getShift(sid);
        if(s!=null) {
            ArrayList<Shift> ls = new ArrayList<>();
            ls.add(s);
            loadShift(ls, optionals);
            return ls.get(0);
        }
        return null;
    }

    private Shift getByDate(LocalDate date,ShiftType shiftType,Map<RoleType, List<Employee>> optionals) {
        Shift s = ShiftMapper.getInstance().getShiftByDate(date,shiftType.name());
        if(s!=null) {
            ArrayList<Shift> ls = new ArrayList<>();
            ls.add(s);
            loadShift(ls, optionals);
            return ls.get(0);
        }
        return null;
    }
    public void removeEmpFromShift(int SID, Employee emp) {
        Shift s = get(SID,AllOptionals);
        s.removeEmpFromShift(emp);
        emp.getRole().forEach(roleType -> s.addToOptionals(emp, roleType));
        removeBuildConstraint(emp, s);
    }

    private void removeBuildConstraint(Employee emp, Shift s) {
        List<Constraint> constraintsOfEmpInDateS = ConstraintMapper.getInstance().getConstraint(emp.getEID(), s.getDate());
        constraintsOfEmpInDateS.forEach(tempCon -> {
            log.debug("Build constraint - CID: " + tempCon.getCID() + " removed");
            ConstraintMapper.getInstance().deleteConstraint(tempCon.getCID());
            if (s.getShiftType().equals(ShiftType.Morning)) {
                Shift opShift = getShiftByDate(s.getDate(), ShiftType.Night);
                if (opShift != null) emp.getRole().forEach(roleType -> opShift.addToOptionals(emp, roleType));
            } else {
                Shift opShift = getShiftByDate(s.getDate(), ShiftType.Morning);
                if (opShift != null) emp.getRole().forEach(roleType -> opShift.addToOptionals(emp, roleType));
            }
        });
    }

    //when fire employee from the branch
    public void removeFireEmp(Employee emp) {
        List<Shift> shifts = ShiftMapper.getInstance().selectShiftsFromUntil(LocalDate.now(), LocalDate.now().plusWeeks(2));
        loadShift(shifts,AllOptionals);
        for (Shift s : shifts)
            s.removeFireEmp(emp);
    }

    //when add new employee to the branch
    public void addToOptionals(Employee emp, RoleType role) {
        List<Shift> shifts = ShiftMapper.getInstance().selectShiftsFromUntil(LocalDate.now(), LocalDate.now().plusWeeks(2));
        loadShift(shifts,AllOptionals);
        for (Shift s : shifts) {
            s.addToOptionals(emp, role);
        }
    }

    public List<Shift> getShifts(LocalDate until) {
        List<Shift> ls = ShiftMapper.getInstance().selectShiftsFromUntil(LocalDate.now(), until);
        loadShift(ls,AllOptionals);
        return ls;
    }

    private void loadShift(List<Shift> ls,Map<RoleType, List<Employee>> optionals){
        ls.forEach(shift -> {
            if (shift.getOptionals() == null) {
                shift.insertMissingRolesAmount();
                Map<RoleType, List<Employee>> ops = deepCopy(optionals);
                loadOptionals(ops, shift.getDate(), shift.getShiftType(),shift.getEmployees());
                shift.setOptionals(ops);
                shift.updateComplete();
                shift.setHasShiftManager();
            }
        });
    }

    public List<Shift> getMyShifts(Employee emp) {
        List<Shift> ls = ShiftMapper.getInstance().getShiftsOfEID(emp.getEID());
        loadShift(ls,AllOptionals);
        return ls;
    }

    public List<Constraint> getMyConstraints(int EID) {
        return ConstraintMapper.getInstance().getConstraintsOfEID(EID);
    }

    public void updateAmountRole(int SID, RoleType role, int newAmount) {
        Shift s = get(SID,AllOptionals);
        List<Employee> toRemove = s.updateRolesAmount(role, newAmount);
        toRemove.forEach(employee -> {
            removeBuildConstraint(employee, s);
        });
    }


    public void updateReasonConstraint(int CID, String newReason) {
        ConstraintMapper.getInstance().updateReason(CID, newReason);
        ConstraintMapper.getInstance().getConstraint(CID).updateReason(newReason);
        log.debug("CID: " + CID + " update his reason");
    }



    public void updateShiftTypeConstraint(int CID, ShiftType newType) {
        ConstraintMapper.getInstance().updateShiftType(CID, newType.name());
        ConstraintMapper.getInstance().getConstraint(CID).updateShiftType(newType);
        log.debug("CID: " + CID + " update his shift type");
    }


    //remove emp from every shift's optionals
    private void RemoveEmpFromOptionals(int EID, Map<RoleType, List<Employee>> optionals) {
        for (Map.Entry<RoleType, List<Employee>> e : optionals.entrySet()) {
            List<Employee> l = e.getValue();
            if (!l.isEmpty()) {
                l.removeIf(emp -> emp.getEID() == EID);
            }
        }
        log.debug("EID: " + EID + " remove from optionals");
    }


    private void createBuildConstraintsAndRemoveFromOpt(List<Employee> listOfEmployees, ShiftType shiftType, LocalDate date) {
        for (Employee emp : listOfEmployees) {
            TempConstraint bConstraint;
            int CID = ConstraintMapper.getInstance().getNextCID();
            if (shiftType.equals(ShiftType.Morning)) {
                Shift s = getShiftByDate(date, ShiftType.Night);
                if (s != null)
                    s.removeEmpFromOptionals(emp);
                bConstraint = new TempConstraint(CID, emp.getEID(), date, ShiftType.Night, "Work in morning shift this day");
            } else {
                Shift s = getShiftByDate(date, ShiftType.Morning);
                if (s != null)
                    s.removeEmpFromOptionals(emp);
                bConstraint = new TempConstraint(CID, emp.getEID(), date, ShiftType.Morning, "Work in Night shift this day");
            }
            ConstraintMapper.getInstance().insertTempConstraint(bConstraint);
        }
    }

    //return null if not exists
    private Shift getShiftByDate(LocalDate date, ShiftType shiftType) {
        return getByDate(date,shiftType,AllOptionals);
    }

    public boolean shiftAlreadyCreated(LocalDate date, ShiftType shiftType) {
        return getByDate(date,shiftType,AllOptionals) != null;
    }

    public boolean constraintIsExist(int CID) {
        return (ConstraintMapper.getInstance().getConstraint(CID)) != null;
    }


    private Map<RoleType, List<Employee>> deepCopy(Map<RoleType, List<Employee>> optionals) {
        Map<RoleType, List<Employee>> copy = new HashMap<>();
        optionals.forEach((roleType, le) -> {
            List<Employee> cloneL = new ArrayList<>();
            for(Employee emp : le)
                cloneL.add(emp);
            copy.put(roleType, cloneL);
        });
        return copy;
    }

    public boolean optionalIsEmpty(int SID) {
        return get(SID,AllOptionals).optionalIsEmpty();
    }

    public boolean checkIfSIDExist(int SID) {
        return get(SID,AllOptionals) != null;
    }

    public boolean EIDIsOptionForSID(int SID, Employee emp) {
        return get(SID,AllOptionals).EIDIsOptionForSID(emp);
    }

    //can work in this shift - is optional and role is not full
    public boolean canWork(int SID, Employee emp, RoleType role) {
        return get(SID,AllOptionals).canWork(emp, role);
    }

    public boolean wasSelfMake(LocalDate date, ShiftType shiftType) {
        return Objects.requireNonNull(getShiftByDate(date, shiftType)).WasSelfMake();
    }

    public boolean shiftIsEmpty(int SID) {
        return get(SID,AllOptionals).getEmployees().isEmpty();
    }

    public boolean EIDWorkInSID(int SID, Employee emp) {
        return get(SID,AllOptionals).getEmployees().containsKey(emp);
    }

    public boolean hasShiftManager(LocalDate date, ShiftType shiftType) {
        return Objects.requireNonNull(getShiftByDate(date, shiftType)).HasShiftManager();
    }

    public boolean driverOrStoreKeeper(int sid, Employee eid) {
        Shift s = get(sid,AllOptionals);
        return !s.getEmployees().get(eid).equals(RoleType.Driver) && !s.getEmployees().get(eid).equals(RoleType.StoreKeeper);
    }

    public boolean StoreKeeperAvailable(LocalDate date, ShiftType shiftType) {
        return getShiftByDate(date,shiftType).StoreKeeperAvailable();
    }

    public List<Integer> getAllAvailableDrivers(LocalDate date, ShiftType shiftType) {
        return getShiftByDate(date,shiftType).getAllAvailableDrivers();
    }

    public void addDriverAndStoreKeeperToShift(Employee driver, LocalDate date, ShiftType shiftType) {
        Shift s = getShiftByDate(date,shiftType);
        s.incrementDriverStoreKeeper();
        s.addEmpToShift(RoleType.Driver,driver);
        Employee sk = s.addStoreKeeper();
        List<Employee> l = new ArrayList<>();
        l.add(sk);
        l.add(driver);
        createBuildConstraintsAndRemoveFromOpt(l,s.getShiftType(),s.getDate());
    }


    public void setAllOptionals(Map<RoleType, List<Employee>> allOptionals) {
        AllOptionals = allOptionals;
    }

    public Map<Integer, Constraint> getConstraints() {
        return ConstraintMapper.getInstance().selectAllConstraints();
    }

    public boolean hasDefaultShifts() {
        return ShiftMapper.getInstance().hasDefaultShifts();
    }


    public boolean DriverAvailable(LocalDate date, ShiftType shiftType) {
        return getShiftByDate(date,shiftType).DriverAvailable();
    }

    public void removeDriverFromShiftAndStorekeeper(Employee driver, LocalDate date, ShiftType shiftType) {
        Shift s = getShiftByDate(date,shiftType);
        removeEmpFromShift(s.getSID(),driver);
        Employee skToRemove = s.getStoreKeeperToRemove();
        removeEmpFromShift(s.getSID(),skToRemove);
        s.decrementDriverAndStoreKeeper();
    }

    public void removeDriverFromShift(Employee driver, LocalDate date, ShiftType shiftType) {
        Shift s = getShiftByDate(date,shiftType);
        removeEmpFromShift(s.getSID(),driver);
    }
    public void addDriverToShift(Employee driver, LocalDate date, ShiftType shiftType) {
        Shift s = getShiftByDate(date,shiftType);
        s.addEmpToShift(RoleType.Driver,driver);
        List<Employee> l = new ArrayList<>();
        l.add(driver);
        createBuildConstraintsAndRemoveFromOpt(l,s.getShiftType(),s.getDate());
    }

    public Shift getShift(int sid) {
        return get(sid,AllOptionals);
    }
}

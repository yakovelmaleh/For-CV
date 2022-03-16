package Business.ApplicationFacade;

import Business.ApplicationFacade.outObjects.Constraint;
import Business.ApplicationFacade.outObjects.Shift;
import Business.Employees.EmployeePKG.Employee;
import Business.Employees.ShiftPKG.ShiftController;
import Business.Type.RoleType;
import DataAccess.EmployeeMapper;
import org.apache.log4j.Logger;

import java.util.*;

public class Utils {
    final static Logger log = Logger.getLogger(Utils.class);
    private ShiftController shiftController;
    private boolean needToUpdateOps;
    private Map<RoleType, List<Business.Employees.EmployeePKG.Employee>> ops;

    public Utils() {
        needToUpdateOps = true;
        ops = new HashMap<>();
    }

    public ShiftController getShiftController() {
        return shiftController;
    }


    /**
     * Input checks
     */

    public void setShiftController(ShiftController shiftController) {
        this.shiftController = shiftController;
    }


//    protected <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
//        log.debug("checking if is enum");
//        for (E e : enumClass.getEnumConstants()) {
//            if (e.name().equals(value)) {
//                return true;
//            }
//        }
//        log.error("Illegal value of enum string: " + value + " of enum class: " + enumClass);
//        return false;
//    }
//


    /**
     * Convert Help Functions of lists
     */
    protected void generate_optionals() {
        if (needToUpdateOps) {
            EnumSet<RoleType> allRoles = EnumSet.allOf(RoleType.class);
            ops.clear();
            for (RoleType role : allRoles) {
                ops.put(role, new ArrayList<>());
            }
            List<Business.Employees.EmployeePKG.Employee> empsInBranch = EmployeeMapper.getInstance().loadEmployeesInBranch();
            empsInBranch.forEach(employee -> {
                employee.getRole().forEach(roleType -> {
                    ops.get(roleType).add(employee);
                });
            });
            setNeedToUpdateOps(false);
            if(shiftController!=null)
                shiftController.setAllOptionals(ops);
        }
    }

    protected List<Business.ApplicationFacade.outObjects.Employee> convertEmployee(List<Business.Employees.EmployeePKG.Employee> allEmployees) {
        log.debug("converting employees of business layer to out objects list");
        List<Business.ApplicationFacade.outObjects.Employee> employees = new ArrayList<>();
        allEmployees.forEach(e -> {
            employees.add(new Business.ApplicationFacade.outObjects.Employee(e));
        });
        log.debug("Done.");
        return employees;
    }

    protected List<Shift> convertShifts(List<Business.Employees.ShiftPKG.Shift> allShifts) {
        log.debug("converting shift of business layer to out objects list");
        List<Shift> shifts = new ArrayList<>();
        allShifts.forEach(s -> {
            Map<Business.ApplicationFacade.outObjects.Employee, String> emps = new HashMap<>();
            Map<String, List<Business.ApplicationFacade.outObjects.Employee>> ops = new HashMap<>();
            s.getEmployees().forEach((employee, roleType) -> {
                emps.put(new Business.ApplicationFacade.outObjects.Employee(employee), roleType.name());
            });
            s.getOptionals().forEach((roleType, employees1) -> {
                List<Business.ApplicationFacade.outObjects.Employee> e = new ArrayList<>();
                employees1.forEach(employee -> {
                    e.add(new Business.ApplicationFacade.outObjects.Employee(employee));
                });
                ops.put(roleType.name(), e);
            });
            shifts.add(new Shift(s, emps, ops));
        });
        log.debug("Done.");
        return shifts;
    }

    protected List<Constraint> convertConstrains(List<Business.Employees.ShiftPKG.Constraint> allConstraints) {
        log.debug("converting constraints of business layer to out objects list");
        List<Constraint> constraints = new ArrayList<>();
        allConstraints.forEach(c -> {
            constraints.add(new Constraint(c));
        });
        log.debug("Done.");
        return constraints;
    }

    public void setNeedToUpdateOps(boolean needToUpdateOps) {
        this.needToUpdateOps = needToUpdateOps;
    }

    public Map<RoleType, List<Employee>> getOps() {
        generate_optionals();
        return ops;
    }


}

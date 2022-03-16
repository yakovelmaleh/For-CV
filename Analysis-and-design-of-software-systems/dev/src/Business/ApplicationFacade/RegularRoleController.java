package Business.ApplicationFacade;

import Business.ApplicationFacade.iControllers.iRegularRoleController;
import Business.ApplicationFacade.outObjects.Constraint;
import Business.ApplicationFacade.outObjects.Employee;
import Business.ApplicationFacade.outObjects.Shift;
import Business.Employees.EmployeePKG.Driver;
import Business.Employees.ShiftPKG.ShiftController;
import Business.Type.RoleType;
import Business.Type.ShiftType;
import DataAccess.BranchMapper;
import DataAccess.DriverMapper;
import DataAccess.EmployeeMapper;
import DataAccess.ShiftMapper;
import org.apache.log4j.Logger;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class RegularRoleController implements iRegularRoleController {
    final static Logger log = Logger.getLogger(RegularRoleController.class);
    private Business.Employees.EmployeePKG.Employee currConnectedEmp;
    private final EmployeeMapper employeeMapper = EmployeeMapper.getInstance();
    private final Utils utils;
    private final ShiftController sc;

    public RegularRoleController(){
        this.currConnectedEmp = null;
        utils = new Utils();
        sc = new ShiftController(utils.getOps());
        utils.setNeedToUpdateOps(true);
        utils.setShiftController(sc);
    }

    /**
     * Logins EID with his/her role of to the system
     *
     * @param EID  The Identification number of the employee
     * @return A response object. The response should contain a error message in case of an error
     */
    public void Login(int EID) {
        log.debug("entered login function with user id: " + EID);
        currConnectedEmp = employeeMapper.get(EID);
        log.debug("successfully logged in - user fields are updated to +" + EID);
    }

    /**
     * Logs out the connected employee
     *
     * @return A response object. The response should contain a error message in case of an error
     */
    public void Logout() {
        log.debug("enter logout function when current connected id is: " + currConnectedEmp.getEID());
        currConnectedEmp= null;
        log.debug("successfully logged out - user fields are updated to -1 and null role");
    }

    /**
     * Add a constraint of const day-type with type-shift and reason of a employee
     *
     * @param day       Day of the week the employee can't work
     * @param shiftType Type of shift of the day
     * @param reason    The reason of the constraint
     * @return A response object. The response should contain a error message in case of an error
     */
    public void addConstConstraint(DayOfWeek day, String shiftType, String reason) {
        log.debug("enter add const constraint function");
        sc.addConstConstraint(currConnectedEmp.getEID(), day, ShiftType.valueOf(shiftType), reason);
        log.debug("successfully added const constraint");
    }

    /**
     * Add a constraint on date {@link LocalDate} with type-shift and reason of a employee
     *
     * @param c_date    The date that the employee can't work
     * @param shiftType Type of shift of the day
     * @param reason    The reason of the constraint
     * @return A response object. The response should contain a error message in case of an error
     */
    public void addTempConstraint(LocalDate c_date, String shiftType, String reason) {
        log.debug("enter add constraint function");
        sc.addTempConstraint(currConnectedEmp,c_date, ShiftType.valueOf(shiftType), reason);
        log.debug("added temp constraint");
    }

    /**
     * Remove a constraint with identifier CID that the employee sees
     *
     * @param CID Identifier of the constraint to be removed
     * @return A response object. The response should contain a error message in case of an error
     */
    public void removeConstraint(int CID) {
        log.debug("enter remove constraint function");
        sc.removeConstraint(CID);
        log.debug("removed add constraint");
    }


    /**
     * Edit/Update an existing constraint with CID with the new reason
     *
     * @param CID       Identifier of the constraint to be updated
     * @param newReason The new reason of the constraint
     * @return A response object. The response should contain a error message in case of an error
     */
    public void updateReasonConstraint(int CID, String newReason){
        log.debug("enter update reason constraint function");
        sc.updateReasonConstraint(CID, newReason);
        log.debug("updated reason constraint");
    }

    /**
     * Edit/Update an existing constraint with CID with a different shift-type
     *
     * @param CID     Identifier of the constraint to be updated
     * @param newType The new shift type
     * @return A response object. The response should contain a error message in case of an error
     */
    public void updateShiftTypeConstraint(int CID, String newType) {
        log.debug("enter update shift type in constraint function");
        sc.updateShiftTypeConstraint(CID, ShiftType.valueOf(newType));
    }

    /**
     * Gets the currently connected employee his/er's shifts
     *
     * @return A response object with a value set to employee containing the details,
     * otherwise the response should contain a error message in case of an error
     */
    public ResponseData<List<Shift>> getMyShifts() {
        log.debug("entered getting shifts of employee: "+currConnectedEmp.getEID());
        utils.generate_optionals();
        List<Business.Employees.ShiftPKG.Shift> myShifts = sc.getMyShifts(currConnectedEmp);
        return new ResponseData<>(utils.convertShifts(myShifts));
    }

    /**
     * Gets the details of a connected employee
     *
     * @return A response object with a value set to the employee
     * otherwise the response should contain a error message in case of an error
     */
    public ResponseData<Employee> getEmployeeDetails() {
       return new ResponseData<>(new Employee(currConnectedEmp));
    }

    /**
     * get all current connected user's constraints
     *
     * @return A response List. The response should contain a error message in case of an error
     */
    public ResponseData<List<Constraint>> getMyConstraints() {
        return new ResponseData<>(utils.convertConstrains(sc.getMyConstraints(currConnectedEmp.getEID())));
    }

    /**
     * Loads the relevant data of a specific branch with BID identifier
     * Note : the BID is chosen in the first window options before identifying to the system the employee
     *
     * @param BID Identifier of the branch (1-9)
     * @return A response object. The response should contain a error message in case of an error
     */
    public void EnterBranch(int BID) {
        log.debug("loading data of branch id: "+BID);
        if(BID!=employeeMapper.getCurrBranchID()) {
            employeeMapper.resetEmps();
            employeeMapper.setCurrBranchID(BID);
            ShiftMapper.getInstance().setCurrBranchID(BID);
            utils.setNeedToUpdateOps(true);
            utils.generate_optionals();
        }
    }
    public boolean checkEIDExists(int id){
        return employeeMapper.containsKey(id);
    }
    /**
     * gets all branches available from database
     *
     * @return A response List. The response should contain a error message in case of an error
     */
    public ResponseData<List<String>> getBranches() {
        List<Integer> allBranches = employeeMapper.getAllBranches();
        return new ResponseData<>(allBranches.stream().map(String::valueOf).collect(Collectors.toList()));
    }

    /**
     * In order to create a new branch in the system, one must enter the unique code and all the details of the PersonnelEmployee of that branch
     *
     * @param newEID      identifier of the new PersonnelManager
     * @param name        his name
     * @param bankDetails bank details of the PersonnelEmployee
     * @param salary      his salary
     * @param terms
     * @return A response object. The response should contain a error message in case of an error
     */

    public void createBranch(int newEID, String name, int[] bankDetails, int salary, int[] terms, String street, String city, int number, int enter, String area, String cn, String phone) {
        log.debug("enter create branch function");
        log.debug("creating instance of the personnel manager in this new branch");
        Business.Employees.EmployeePKG.Employee m = new Business.Employees.EmployeePKG.Employee(newEID, name, bankDetails, salary, RoleType.PersonnelManager, LocalDate.now(), terms);
        int bid = BranchMapper.getMapper().insertNewBranch(street,city,number,enter,area,cn,phone);
        employeeMapper.setCurrBranchID(bid);
        employeeMapper.insert(m,false);
        log.debug("successfully created branch");
    }

    /**
     * gets all role types
     *
     * @return A response String. The response should contain a error message in case of an error
     */
    public ResponseData<List<String>> getRoleTypes() {
        return new ResponseData<>((new ArrayList<>(EnumSet.allOf(RoleType.class))).stream().map(Enum::name).collect(Collectors.toList()));
    }

    /**
     * gets all shift types
     *
     * @return A response String. The response should contain a error message in case of an error
     */
    public ResponseData<List<String>> getShiftTypes() {
        return new ResponseData<>((new ArrayList<>(EnumSet.allOf(ShiftType.class))).stream().map(Enum::name).collect(Collectors.toList()));
    }

    /**
     * checks if default shifts are initialized
     *
     * @return true if yes else false
     */
    public ResponseData<Boolean> hasDefaultShifts() {
        return new ResponseData<>(sc.hasDefaultShifts());
    }

    public Utils getUtils() {
        return utils;
    }


    public boolean isQualified(int eid, String role) {
        return employeeMapper.get(eid).isQualified(RoleType.valueOf(role));
    }
    public boolean checkConstExist(int CID){
        return sc.constraintIsExist(CID);
    }

    public boolean empConnected() {
        return currConnectedEmp !=null;
    }

    public boolean checkIfMyConst(int cid) {
        return sc.getConstraints().get(cid).getEID() == currConnectedEmp.getEID();
    }

    public boolean checkIfShiftExist(LocalDate date, String shiftType) {
        return sc.shiftAlreadyCreated(date,ShiftType.valueOf(shiftType));
    }

    public boolean checkIfShiftIsClose(LocalDate date, String shiftType) {
        return sc.wasSelfMake(date,ShiftType.valueOf(shiftType));
    }

    public boolean checkIfDriver(int eid) {
        try{
            Driver d = DriverMapper.getMapper().select(eid);
            return d != null;
        }catch (Exception e){
            return false;
        }
    }

    public int getCurrConnectedEID() {
        return currConnectedEmp.getEID();
    }
}

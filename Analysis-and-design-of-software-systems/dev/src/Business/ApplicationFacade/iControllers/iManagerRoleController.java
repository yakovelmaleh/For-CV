package Business.ApplicationFacade.iControllers;

import Business.ApplicationFacade.ResponseData;
import Business.ApplicationFacade.outObjects.Employee;
import Business.ApplicationFacade.outObjects.Shift;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * This is the Employee module Service interface - done by Dor Elad & Niv Dan
 * This interface includes only functions that are needed for part 1 before uniting with other module
 * ShiftType , DayOfWeek , RoleType are Strings that will be converted into Business.Type.enums and passed on
 * RoleType = {PersonnelManager, BranchManager, ShiftManager, Driver, Cashier,...etc.}
 * DayOfWeek = {SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THIRSTY, FRIDAY, SATURDAY }
 * ShiftType = {Morning, Night}
 */

public interface iManagerRoleController {




    /**
     * Add a new employee to the system with all his details
     *
     * @param newEID        Identification number of the new employee
     * @param name          Name of the new employee
     * @param bankDetails   array of bank details :
     *                      bankDetails[0] -> account number
     *                      bankDetails[1] -> bank branch
     *                      bankDetails[2] -> bank id
     * @param salary        THe salary of the new employee
     * @param role          The role of the new employee
     * @param startWorkDate THe starting date of the new employee
     * @param terms         Terms of employments :
     *                      terms[0] -> education fund
     *                      terms[1] -> days off
     *                      terms[2] -> sick days
     * @return A response object. The response should contain a error message in case of an error
     */
     Business.Employees.EmployeePKG.Employee addEmployee(int newEID, String name, int[] bankDetails, int salary, String role, LocalDate startWorkDate, int[] terms);
    Business.Employees.EmployeePKG.Driver addDriver(int newEID, String name, int[] bankDetails, int salary, LocalDate startWorkDate, int[] terms, int license);
    /**
     * fire an employee with fireID
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param fireEID Identifier of the employee to fire
     * @return A response object. The response should contain a error message in case of an error
     */
    void fireEmployee(int fireEID);

    /**
     * Update an existing employee's name in the system
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param updateEID The identifier of the employee
     * @param newName   The new name
     * @return A response object. The response should contain a error message in case of an error
     */
    void updateEmployeeName(int updateEID, String newName);

    /**
     * Update an existing employee's salary in the system
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param updateEID The identifier of the employee
     * @param newSalary The new salary of the employee
     * @return A response object. The response should contain a error message in case of an error
     */
    void updateEmployeeSalary(int updateEID, int newSalary);

    /**
     * Update an existing employee's bank account number in the system
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param updateEID        The identifier of the employee
     * @param newAccountNumber The new account number of the employee
     * @return A response object. The response should contain a error message in case of an error
     */
    void updateEmployeeBANum(int updateEID, int newAccountNumber);

    /**
     * Update an existing employee's bank branch  in the system
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param updateEID The identifier of the employee
     * @param newBranch The new bank branch number of the employee
     * @return A response object. The response should contain a error message in case of an error
     */
    void updateEmployeeBABranch(int updateEID, int newBranch);

    /**
     * Update an existing employee's bank id number in the system
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param updateEID The identifier of the employee
     * @param newBankID The new bank id number of the employee
     * @return A response object. The response should contain a error message in case of an error
     */
    void updateEmployeeBAID(int updateEID, int newBankID);

    /**
     * Update an existing employee's education fund in the system
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param updateEID        The identifier of the employee
     * @param newEducationFund The education fund of the employee
     * @return A response object. The response should contain a error message in case of an error
     */
    void updateEmployeeEducationFund(int updateEID, int newEducationFund);

    /**
     * Update an existing employee's days off in the system
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param updateEID The identifier of the employee
     * @param newAmount The new amount of days off of the employee
     * @return A response object. The response should contain a error message in case of an error
     */
    void updateEmployeeDaysOff(int updateEID, int newAmount);

    /**
     * Update an existing employee's sick days in the system
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param updateEID The identifier of the employee
     * @param newAmount The new amount of sick days of the employee
     * @return A response object. The response should contain a error message in case of an error
     */
    void updateEmployeeSickDays(int updateEID, int newAmount);

    /**
     * Create a new shift with given roles and amount of each role at a specific date {@link LocalDate} and shift-type
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param rolesAmount Map with key: role and value: the amount
     * @param date        The date of the shift to take place
     * @param shiftType   type of the shift
     * @returnA response object with a value set to the employee
     * *         otherwise the response should contain a error message in case of an error
     */
    void createShift(Map<String, Integer> rolesAmount, LocalDate date, String shiftType);

    /**
     * set the default Shifts with roles and amount of each role
     * Note: Only the personnel manager is allowed to use this functionality
     *
     * @param defaultRolesAmount Map<ShiftType, Map<RoleType,amount>>
     * @return A response object. The response should contain a error message in case of an error
     */
    void defaultShifts(Map<String, Map<String, Integer>> defaultRolesAmount);

    /**
     * creates shifts of week from SUNDAY to FRIDAY(FRIDAY only morning) default shifts
     * Note: Only the personnel manager is allowed to use this functionality
     *
     * @return A response object. The response should contain a error message in case of an error
     */
    ResponseData<List<Shift>> createWeekShifts();

    /**
     * makes shifts of week from SUNDAY to FRIDAY(FRIDAY only morning) default shifts with all constraints and puts the employees in them
     * Note: Only the personnel manager is allowed to use this functionality
     *
     * @return A response object. The response should contain a error message in case of an error
     */
    void selfMakeWeekShifts();



    /**
     * Gets the details all the shifts and the employees in the shifts including optionals
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @return A response object with a value set to shift containing the employees in it,
     * otherwise the response should contain a error message in case of an error
     */
    ResponseData<List<Shift>> getShifts(LocalDate until);

    /**
     * Removes employee with removeEID id from an existing shift with SID
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param SID       Identifier of the shift
     * @param removeEID Identifier of the employee
     * @return A response object. The response should contain a error message in case of an error
     */
    void removeEmpFromShift(int SID, int removeEID);

    /**
     * Adds an employee with  id(addEID) and  his/her role to shift(SID)
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param SID    Identifier of the shift
     * @param addEID Identifier of the employee
     * @param role   The role of the employee will be in the shift
     * @return A response object. The response should contain a error message in case of an error
     */
    void addEmpToShift(int SID, int addEID, String role);

    /**
     * Update the amount of a specific role in shift with SID id with new amount
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param SID       Identifier of the shift
     * @param role      The role that will be updated in the shift
     * @param newAmount new amount of the role
     * @return A response object. The response should contain a error message in case of an error
     */
    void updateAmountRole(int SID, String role, int newAmount);


    /**
     * Adds to a specific employee a role to his list
     * Note : Only the personnel manager is allowed to use this functionality
     *
     * @param EID  the identifier of the employee to add the role
     * @param role the role
     * @return
     */
    void addRoleToEmployee(int EID, String role);




    /**
     * get all employees in current branch
     *  Note : Only the personnel manager is allowed to use this functionality
     * @return  A response List. The response should contain a error message in case of an error
     */
    ResponseData<List<Employee>> getAllEmployees();

     boolean checkIfSIDExist(int sid);
     boolean optionalIsEmpty(int SID);
     boolean EIDIsOptionForSID(int sid, int eid);
     boolean canWork(int sid,int eid, String role);
     boolean shiftIsEmpty(int sid);
     boolean EIDWorkInSID(int sid,int eid);
     boolean hasShiftManager(LocalDate date, String shiftType);
     boolean driverOrStoreKeeper(int sid, int eid);

     void EnterBranch(int BID);
     boolean ShiftExist(LocalDate date,String shiftType);

     boolean StoreKeeperAvailable(LocalDate date, String shiftType);

     List<Integer> getAllAvailableDrivers(LocalDate date, String shiftType);

     void addDriverAndStoreKeeperToShift(int driverID, LocalDate date, String shiftType);

    boolean DriverAvailable(LocalDate date, String shiftType);

    void removeDriverFromShiftAndStorekeeper(int driverID, LocalDate date, String shiftType);


    Shift getShift(int sid);

    List<String> getMessagesOfManager(int BID, int EID);

    void removeDriverFromShift(int oldDriverID, LocalDate date, String shiftType);

    void addDriverToShift(int newDriverID, LocalDate date, String shiftType);
}

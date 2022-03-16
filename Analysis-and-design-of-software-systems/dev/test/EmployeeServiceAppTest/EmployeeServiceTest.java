//package EmployeeServiceAppTest;
//
//import Business.ApplicationFacade.*;
//import Business.ApplicationFacade.outObjects.*;
//import org.junit.*;
//
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class EmployeeServiceTest {
//    private iManagerRoleController service;
//    private final int[] bank = {1, 2, 3};
//    private final int[] terms = {4, 5, 6};
//
//    /**
//     * ******************************************************************
//     * Assume that database has in init:
//     * Branch : 1
//     * Employees in branch 1 : PersonnelManager with EID = 1
//     * Shifts : 1 ->Morning (2021,8,2) with SID 1
//     *
//     * @throws Exception
//     */
//    @Before
//    public void setUp() {
//        service = new ManagerRoleController();
//        service.createBranch("00000",1, "Niv", bank, 1000, terms);
//    }
//
//    @After
//    public void tearDown() {
//
//    }
//
//    @Test
//    public void loginFail1() {
//        Response response = service.Login(1, "Manager");
//        Assert.assertTrue("Exception expected - there is no type of 'Manager'", response.isError());
//    }
//
//    @Test
//    public void loginFail2() {
//        Response response = service.Login(30000, "Driver");
//        Assert.assertTrue("Exception expected - there is no id of 30000 in system", response.isError());
//    }
//
//    @Test
//    public void loginNewBranchPass() {
//        Response response = service.Login(2, "PersonnelManager");
//        Assert.assertTrue("Exception expected - there is no id of 2 in branch 1", response.isError());
//        Response s1 = service.createBranch("00000", 2, "Niv", bank, 30000, terms);
//        Assert.assertFalse(s1.isError());
//        /*
//        Response l = service.loadData(2);
//        Assert.assertFalse(l.isError());
//        Response response1 = service.Login(2, "PersonnelManager");
//        Assert.assertFalse("Not Exception expected", response1.isError());
//         */
//    }
//
//    @Test
//    public void addConstConstraint() {
//        Response personnelManagerLog = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(personnelManagerLog.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response login = service.Login(2, "Driver");
//        Assert.assertFalse(login.isError());
//        ResponseData<Constraint> res = service.addConstConstraint(DayOfWeek.MONDAY, "Morning", "i'm tired");
//        Assert.assertFalse(res.isError());
//        ResponseData<List<Constraint>> cons = service.getMyConstraints();
//        Assert.assertFalse(cons.isError());
//        boolean contains = cons.getData().isEmpty();
//        Assert.assertFalse("The constraint wasn't added properly", contains);
//        /*
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response login2 = service.Login(2,"Driver");
//        Assert.assertFalse(login2.isError());
//        ResponseData<Employee> cons2 = service.getOnlyEmployeeShiftsAndConstraints();
//        Assert.assertFalse(cons2.isError());
//        boolean contains2 = cons2.getData().containsConst(res.getData().CID);
//        Assert.assertTrue("The constraint wasn't loaded back(saved) properly",contains2);
//        */
//    }
//
//    @Test
//    public void addConstraintCorrectTypo() {
//        Response personnelManagerLog = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(personnelManagerLog.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response login = service.Login(2, "Driver");
//        Assert.assertFalse(login.isError());
//        ResponseData<Constraint> res1 = service.addConstraint(LocalDate.of(2021, 8, 2), "Morningg", "i'm tired");
//        Assert.assertTrue("Exception expected - shift type does not match the date", res1.isError());
//        Response personnelManagerLog2 = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(personnelManagerLog2.isError());
//        Map<String,Integer> a = new HashMap<>();
//        a.put("ShiftManager",1);
//        ResponseData<Shift> s = service.createShift(a,LocalDate.of(2021, 8, 2),"Morning");
//        Assert.assertTrue(s.isError());
//        Response logout_p = service.Logout();
//        Assert.assertFalse(logout_p.isError());
//        Response loginD = service.Login(2,"Driver");
//        Assert.assertFalse(loginD.isError());
//        ResponseData<Constraint> res2 = service.addConstraint(LocalDate.of(2021, 8, 2), "Morning", "i'm tired");
//        Assert.assertFalse(res2.isError());
//    }
//
//    @Test
//    public void addConstraintNonExistsShift() {
//        Response personnelManagerLog = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(personnelManagerLog.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response s6 = service.Login(2, "Driver");
//        Assert.assertFalse(s6.isError());
//        ResponseData<Constraint> res = service.addConstraint(LocalDate.of(2222, 8, 2), "Morning", "i'm tired");
//        Assert.assertTrue("Exception expected - there is no such shift", res.isError());
//    }
//
//    @Test
//    public void addConstraintPassProperly() {
//        Response personnelManagerLog = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(personnelManagerLog.isError());
//        Map<String,Integer> a = new HashMap<>();
//        a.put("ShiftManager",1);
//        ResponseData<Shift> s = service.createShift(a,LocalDate.of(2021, 8, 2),"Morning");
//        Assert.assertTrue(s.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response s6 = service.Login(2, "Driver");
//        Assert.assertFalse(s6.isError());
//        ResponseData<Constraint> res = service.addConstraint(LocalDate.of(2021, 8, 2), "Morning", "i'm tired");
//        Assert.assertFalse(res.isError());
//        ResponseData<List<Constraint>> cons = service.getMyConstraints();
//        Assert.assertFalse(cons.isError());
//        boolean contains = cons.getData().isEmpty();
//        Assert.assertFalse("The constraint wasn't added properly", contains);
//        /*
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response reload = service.loadData(1);
//        Assert.assertFalse(reload.isError());
//        Response s9 = service.Login(2,"Driver");
//        Assert.assertFalse(s9.isError());
//        ResponseData<Employee> cons1 = service.getOnlyEmployeeShiftsAndConstraints();
//        boolean contains1 = cons1.getData().containsConst(res.getData().CID);
//        Assert.assertTrue("The constraint wasn't added properly",contains1);
//         */
//    }
//
//    @Test
//    public void removeConstraintPassProperly() {
//        Response personnelManagerLog = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(personnelManagerLog.isError());
//        Map<String,Integer> a = new HashMap<>();
//        a.put("ShiftManager",1);
//        a.put("Driver",1);
//        ResponseData<Shift> s = service.createShift(a,LocalDate.of(2021, 8, 2),"Morning");
//        Assert.assertTrue(s.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response s6 = service.Login(2, "Driver");
//        Assert.assertFalse(s6.isError());
//        ResponseData<Constraint> res = service.addConstraint(LocalDate.of(2021, 8, 2), "Morning", "i'm tired");
//        Assert.assertFalse(res.isError());
//        ResponseData<Constraint> rem = service.removeConstraint(res.getData().CID);
//        Assert.assertFalse(rem.isError());
//        ResponseData<List<Constraint>> cons = service.getMyConstraints();
//        Assert.assertFalse(cons.isError());
//        boolean contains = cons.getData().isEmpty();
//        Assert.assertTrue("The constraint wasn't removed properly", contains);
//        /*
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response reload = service.loadData(1);
//        Assert.assertFalse(reload.isError());
//        Response s9 = service.Login(2,"Driver");
//        Assert.assertFalse(s9.isError());
//        ResponseData<Employee> cons1 = service.getOnlyEmployeeShiftsAndConstraints();
//        boolean contains1 = cons1.getData().containsConst(res.getData().CID);
//        Assert.assertFalse("The constraint wasn't removed properly",contains1);
//         */
//    }
//
//    @Test
//    public void removeConstraintByOtherUser() {
//        Response personnelManagerLog = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(personnelManagerLog.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response s6 = service.Login(2, "Driver");
//        Assert.assertFalse(s6.isError());
//        ResponseData<Constraint> res = service.addConstConstraint(DayOfWeek.MONDAY, "Morning", "i'm tired");
//        Assert.assertFalse(res.isError());
//        Response D_logout = service.Logout();
//        Assert.assertFalse(D_logout.isError());
//        Response personnelManagerLog1 = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(personnelManagerLog1.isError());
//        ResponseData<Employee> driver3 = service.addEmployee(3, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver3.isError());
//        Response p_logout2 = service.Logout();
//        Assert.assertFalse(p_logout2.isError());
//        Response s9 = service.Login(3, "Driver");
//        Assert.assertFalse(s9.isError());
//        ResponseData<Constraint> removedC = service.removeConstraint(res.getData().CID);
//        Assert.assertTrue("Exception expected - a user can remove its own constraints only", removedC.isError());
//    }
//
//    @Test
//    public void updateReasonConstraintPassProperly() {
//        Response personnelManagerLog = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(personnelManagerLog.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response s6 = service.Login(2, "Driver");
//        Assert.assertFalse(s6.isError());
//        ResponseData<Constraint> s7 = service.addConstConstraint(DayOfWeek.MONDAY, "Morning", "i'm tired");
//        Assert.assertFalse(s7.isError());
//        Response s8 = service.updateReasonConstraint(s7.getData().CID, "xxx");
//        Assert.assertFalse(s8.isError());
//        ResponseData<List<Constraint>> constList = service.getMyConstraints();
//        Assert.assertFalse(constList.isError());
//        Constraint c = constList.getData().get(0);
//        Assert.assertNotEquals("Update Reason in constraint didnt work properly", "i'm tired", c.reason);
//        /*
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response reload = service.loadData(1);
//        Assert.assertFalse(reload.isError());
//        Response s9 = service.Login(2,"Driver");
//        Assert.assertFalse(s9.isError());
//        ResponseData<Employee> constList2 = service.getOnlyEmployeeShiftsAndConstraints();
//        Assert.assertFalse(constList2.isError());
//        Constraint c1 = constList2.getData().constraints.get(0);
//        Assert.assertNotEquals("Update Reason in constraint didnt work properly after reload", "i'm tired", c1.reason);
//         */
//    }
//
//    @Test
//    public void updateShiftTypeConstraintPassProperly() {
//        Response personnelManagerLog = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(personnelManagerLog.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response s6 = service.Login(2, "Driver");
//        Assert.assertFalse(s6.isError());
//        ResponseData<Constraint> s7 = service.addConstConstraint(DayOfWeek.MONDAY, "Morning", "i'm tired");
//        Assert.assertFalse(s7.isError());
//        Response s8 = service.updateShiftTypeConstraint(s7.getData().CID, "Night");
//        Assert.assertFalse(s8.isError());
//        ResponseData<List<Constraint>> constList = service.getMyConstraints();
//        Assert.assertFalse(constList.isError());
//        Constraint c = constList.getData().get(0);
//        Assert.assertNotEquals("Update shift type in constraint didnt work properly", "Morning", c.shiftType);
//        /*
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response reload = service.loadData(1);
//        Assert.assertFalse(reload.isError());
//        Response s9 = service.Login(2,"Driver");
//        Assert.assertFalse(s9.isError());
//        ResponseData<Employee> constList2 = service.getOnlyEmployeeShiftsAndConstraints();
//        Assert.assertFalse(constList2.isError());
//        Constraint c1 = constList2.getData().constraints.get(0);
//        Assert.assertNotEquals("Update shift type in constraint didnt work properly after reload", "Morning", c1.shiftType);
//    */
//    }
//
//    @Test
//    public void addEmployeePassProperly() {
//        Response s2 = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(s2.isError());
//        Response added = service.addEmployee(5, "xxx", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse("Not expected exception adding an employee by personnel manager", added.isError());
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//       /* Response reload = service.loadData(1);
//        Assert.assertFalse(reload.isError());
//        Response s9 = service.Login(5,"Driver");
//        Assert.assertFalse("Not expected exception driver was not loaded after reload",s9.isError());
//        */
//    }
//
//    @Test
//    public void addEmployeeAddExistingEmployeeAndIllegalAdd() {
//        Response s2 = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(s2.isError());
//        Response driver = service.addEmployee(6, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response s4 = service.addEmployee(6, "vv", bank, 10023400, "Cashier", LocalDate.now(), terms);
//        Assert.assertTrue("Exception expected - employee already exists", s4.isError());
//        Response s5 = service.addEmployee(4, "vv", bank, 10000, "Cashier", LocalDate.now(), terms);
//        Assert.assertFalse(s5.isError());
//        Response s6 = service.Logout();
//        Assert.assertFalse(s6.isError());
//        Response s7 = service.Login(6, "Driver");
//        Assert.assertFalse(s7.isError());
//        Response s8 = service.addEmployee(4, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertTrue("expected exception adding an employee by driver", s8.isError());
//        /*Response s10 = service.Logout();
//        Assert.assertFalse(s10.isError());
//        Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response login = service.Login(4,"Cashier");
//        Assert.assertFalse("Not Expected exception - reload employee failed",login.isError());*/
//    }
//
//    @Test
//    public void fireEmployeeFailFireByDriver() {
//        Response personnelManagerLog = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(personnelManagerLog.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response s6 = service.Login(2, "Driver");
//        Assert.assertFalse(s6.isError());
//        Response s7 = service.addEmployee(4, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertTrue("expected exception firing an employee by driver", s7.isError());
//    }
//
//    @Test
//    public void fireEmployeeFireProperly() {
//        Response personnelManagerLog = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(personnelManagerLog.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response s5 = service.fireEmployee(2);
//        Assert.assertFalse("Not expected exception adding an employee by personnel manager", s5.isError());
//        /*Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response log = service.Login(2, "Driver");
//        Assert.assertTrue("After reload didnt save in database properly", log.isError());*/
//    }
//
//    @Test
//    public void updateEmployeeNamePassProperly() {
//        Response s2 = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(s2.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response s5 = service.updateEmployeeName(2, "xxx");
//        Assert.assertFalse(s5.isError());
//        Response s6 = service.Logout();
//        Assert.assertFalse(s6.isError());
//        Response s7 = service.Login(2, "Driver");
//        Assert.assertFalse(s7.isError());
//        ResponseData<Employee> s8 = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee name didnt update", "moshe", s8.getData().name);
//       /* Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response log = service.Login(2, "Driver");
//        Assert.assertNotEquals("updating employee name didnt update after reload", "moshe", s8.getData().name);*/
//    }
//
//    @Test
//    public void updateEmployeeNameFailByDriver() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response s6 = service.Logout();
//        Assert.assertFalse(s6.isError());
//        Response s2 = service.Login(2, "Driver");
//        Assert.assertFalse(s2.isError());
//        Response s5 = service.updateEmployeeName(2, "xxx");
//        Assert.assertTrue("Expected exception - driver cannot update details", s5.isError());
//    }
//
//    @Test
//    public void updateEmployeeSalaryPassProperly() {
//        Response s2 = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(s2.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response s5 = service.updateEmployeeSalary(2, 10);
//        Assert.assertFalse(s5.isError());
//        Response s6 = service.Logout();
//        Assert.assertFalse(s6.isError());
//        Response s7 = service.Login(2, "Driver");
//        Assert.assertFalse(s7.isError());
//        ResponseData<Employee> s8 = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee salary didnt update", 10000, s8.getData().salary);
//       /* Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response log = service.Login(2, "Driver");
//        Assert.assertFalse(log.isError());
//        ResponseData<Employee> ss = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee salary didnt update", 10000, ss.getData().salary);*/
//    }
//
//    @Test
//    public void updateEmployeeSalaryFailByDriver() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response s2 = service.Login(2, "Driver");
//        Assert.assertFalse(s2.isError());
//        Response s5 = service.updateEmployeeSalary(2, 9999999);
//        Assert.assertTrue("Expected exception - driver cannot update details", s5.isError());
//    }
//
//    @Test
//    public void updateEmployeeBANumPassProperly() {
//        Response s2 = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(s2.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response s5 = service.updateEmployeeBANum(2, 10);
//        Assert.assertFalse(s5.isError());
//        Response s6 = service.Logout();
//        Assert.assertFalse(s6.isError());
//        Response s7 = service.Login(2, "Driver");
//        Assert.assertFalse(s7.isError());
//        ResponseData<Employee> s8 = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee BA number didnt update", 1, s8.getData().bankAccount[0]);
//        /*Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response log = service.Login(2, "Driver");
//        Assert.assertFalse(log.isError());
//        ResponseData<Employee> ss = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee BA number didnt update after reload", 1, ss.getData().bankAccount[0]);*/
//    }
//
//    @Test
//    public void updateEmployeeBANumByDriver() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response s2 = service.Login(2, "Driver");
//        Assert.assertFalse(s2.isError());
//        Response s5 = service.updateEmployeeBANum(2, 9999999);
//        Assert.assertTrue("Expected exception - driver cannot update details", s5.isError());
//    }
//
//    @Test
//    public void updateEmployeeBABranchPassProperly() {
//        Response s2 = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(s2.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response s5 = service.updateEmployeeBABranch(2, 10);
//        Assert.assertFalse(s5.isError());
//        Response s6 = service.Logout();
//        Assert.assertFalse(s6.isError());
//        Response s7 = service.Login(2, "Driver");
//        Assert.assertFalse(s7.isError());
//        ResponseData<Employee> s8 = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee BA branch didnt update", 2, s8.getData().bankAccount[1]);
//        /* Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//       Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response log = service.Login(2, "Driver");
//        Assert.assertFalse(log.isError());
//        ResponseData<Employee> ss = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee BA branch didnt update after reload", 2, ss.getData().bankAccount[1]);*/
//    }
//
//    @Test
//    public void updateEmployeeBABranchByDriver() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response s2 = service.Login(2, "Driver");
//        Assert.assertFalse(s2.isError());
//        Response s5 = service.updateEmployeeBABranch(2, 9999999);
//        Assert.assertTrue("Expected exception - driver cannot update details", s5.isError());
//    }
//
//    @Test
//    public void updateEmployeeBAIDPassProperly() {
//        Response s2 = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(s2.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response s5 = service.updateEmployeeBAID(2, 10);
//        Assert.assertFalse(s5.isError());
//        Response s6 = service.Logout();
//        Assert.assertFalse(s6.isError());
//        Response s7 = service.Login(2, "Driver");
//        Assert.assertFalse(s7.isError());
//        ResponseData<Employee> s8 = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee BA ID didnt update", 3, s8.getData().bankAccount[2]);
//        /*Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response log = service.Login(2, "Driver");
//        Assert.assertFalse(log.isError());
//        ResponseData<Employee> ss = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee BA ID didnt update after reload", 3, ss.getData().bankAccount[2]);*/
//    }
//
//    @Test
//    public void updateEmployeeBAIDByDriver() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response s2 = service.Login(2, "Driver");
//        Assert.assertFalse(s2.isError());
//        Response s5 = service.updateEmployeeBAID(2, 9999999);
//        Assert.assertTrue("Expected exception - driver cannot update details", s5.isError());
//    }
//
//    @Test
//    public void updateEmployeeEducationFundPassProperly() {
//        Response s2 = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(s2.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response s5 = service.updateEmployeeEducationFund(2, 10);
//        Assert.assertFalse(s5.isError());
//        Response s6 = service.Logout();
//        Assert.assertFalse(s6.isError());
//        Response s7 = service.Login(2, "Driver");
//        Assert.assertFalse(s7.isError());
//        ResponseData<Employee> s8 = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee education fund didnt update", 4, s8.getData().terms[0]);
//        /*Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response log = service.Login(2, "Driver");
//        Assert.assertFalse(log.isError());
//        ResponseData<Employee> ss = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee education fund didnt update after reload", 4, ss.getData().terms[0]);*/
//    }
//
//    @Test
//    public void updateEmployeeEducationFundByDriver() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response s2 = service.Login(2, "Driver");
//        Assert.assertFalse(s2.isError());
//        Response s5 = service.updateEmployeeEducationFund(2, 9999999);
//        Assert.assertTrue("Expected exception - driver cannot update details", s5.isError());
//    }
//
//    @Test
//    public void updateEmployeeDaysOffPassProperly() {
//        Response s2 = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(s2.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response s5 = service.updateEmployeeDaysOff(2, 10);
//        Assert.assertFalse(s5.isError());
//        Response s6 = service.Logout();
//        Assert.assertFalse(s6.isError());
//        Response s7 = service.Login(2, "Driver");
//        Assert.assertFalse(s7.isError());
//        ResponseData<Employee> s8 = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee days off didnt update", 5, s8.getData().terms[1]);
//        /*Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response log = service.Login(2, "Driver");
//        Assert.assertFalse(log.isError());
//        ResponseData<Employee> ss = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee days off didnt update after reload", 5, ss.getData().terms[1]);*/
//    }
//
//    @Test
//    public void updateEmployeeDaysOffByDriver() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response s2 = service.Login(2, "Driver");
//        Assert.assertFalse(s2.isError());
//        Response s5 = service.updateEmployeeDaysOff(2, 9999999);
//        Assert.assertTrue("Expected exception - driver cannot update details", s5.isError());
//    }
//
//    @Test
//    public void updateEmployeeSickDaysPassProperly() {
//        Response s2 = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(s2.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response s5 = service.updateEmployeeSickDays(2, 10);
//        Assert.assertFalse(s5.isError());
//        Response s6 = service.Logout();
//        Assert.assertFalse(s6.isError());
//        Response s7 = service.Login(2, "Driver");
//        Assert.assertFalse(s7.isError());
//        ResponseData<Employee> s8 = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee sick days didnt update", 6, s8.getData().terms[2]);
//        /*Response p_logout = service.Logout();
//        Assert.assertFalse(p_logout.isError());
//        Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response log = service.Login(2, "Driver");
//        Assert.assertFalse(log.isError());
//        ResponseData<Employee> ss = service.getEmployeeDetails();
//        Assert.assertNotEquals("updating employee sick days didnt update after reload", 6, ss.getData().terms[2]);*/
//    }
//
//    @Test
//    public void updateEmployeeSickDaysByDriver() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response s2 = service.Login(2, "Driver");
//        Assert.assertFalse(s2.isError());
//        Response s5 = service.updateEmployeeSickDays(2, 9999999);
//        Assert.assertTrue("Expected exception - driver cannot update details", s5.isError());
//    }
//
//    @Test
//    public void createShiftFailTypo() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        Map<String, Integer> rolesAmount = new HashMap<>();
//        rolesAmount.put("ShiftManager", 1);
//        rolesAmount.put("Driver", 1);
//        ResponseData<Shift> created1 = service.createShift(rolesAmount, LocalDate.of(2021, 8, 8), "Mornining");
//        Assert.assertTrue("Expected exception - shift typo", created1.isError());
//        Map<String, Integer> rolesAmountFail = new HashMap<>();
//        rolesAmountFail.put("ShiftManager", 1);
//        rolesAmountFail.put("Driverr", 1);
//        ResponseData<Shift> created2 = service.createShift(rolesAmountFail, LocalDate.of(2021, 8, 8), "Mornining");
//        Assert.assertTrue("Expected exception - shift typo", created2.isError());
//        ResponseData<Shift> created3 = service.createShift(rolesAmount, LocalDate.of(2021, 8, 8), "Morning");
//        Assert.assertTrue("Exception error", created3.isError());
//    }
//
//    @Test
//    public void createShiftPassProperly1() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        Map<String, Integer> rolesAmount = new HashMap<>();
//        rolesAmount.put("ShiftManager", 1);
//        rolesAmount.put("Driver", 1);
//        ResponseData<Shift> created = service.createShift(rolesAmount, LocalDate.of(2021, 8, 8), "Morning");
//        Assert.assertTrue("Exception error", created.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response fail = service.addEmpToShift(0, 2, "Driverr");
//        Assert.assertTrue("Expected typo exception", fail.isError());
//        Response suc = service.addEmpToShift(0, 2, "Driver");
//        Assert.assertFalse(suc.isError());
//    }
//
//    @Test
//    public void createShiftPassProperly2() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        Map<String, Integer> rolesAmount = new HashMap<>();
//        rolesAmount.put("ShiftManager", 1);
//        rolesAmount.put("Driver", 1);
//        ResponseData<Shift> created = service.createShift(rolesAmount, LocalDate.now().plusDays(5), "Morning");
//        Assert.assertTrue("Unexpected Exception error", created.isError());
//        ResponseData<Employee> driver = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver.isError());
//        Response suc = service.addEmpToShift(0, 2, "Driver");
//        Assert.assertFalse(suc.isError());
//        ResponseData<List<Shift>> shiftList = service.getShifts(LocalDate.now().plusWeeks(2));
//        Assert.assertFalse(shiftList.isError());
//        boolean contains = shiftList.getData().get(0).SID == 0;
//        Assert.assertTrue(contains);
//        /*Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response p_login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(p_login.isError());
//        ResponseData<List<Shift>> shiftList = service.getShiftsAndEmployees();
//        Assert.assertFalse(shiftList.isError());
//        boolean contains = shiftList.getData().get(0).SID == created.getData().SID;
//        Assert.assertTrue("reload of shifts or save in database failed", contains);*/
//    }
//
//    @Test
//    public void createShiftFailAmount() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        Map<String, Integer> rolesAmount = new HashMap<>();
//        rolesAmount.put("ShiftManager", 1);
//        rolesAmount.put("Driver", 1);
//        ResponseData<Shift> created = service.createShift(rolesAmount, LocalDate.of(2021, 8, 8), "Morning");
//        Assert.assertTrue(" Exception error", created.isError());
//        ResponseData<Employee> driver1 = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver1.isError());
//        ResponseData<Employee> driver2 = service.addEmployee(6, "dorrr", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver2.isError());
//        Response suc = service.addEmpToShift(0, 2, "Driver");
//        Assert.assertFalse(suc.isError());
//        Response fail = service.addEmpToShift(0, 6, "Driver");
//        Assert.assertTrue("Expected to fail because amount of driver is 1 ", fail.isError());
//        ResponseData<Employee> cashier = service.addEmployee(8, "blabla", bank, 10000, "Cashier", LocalDate.now(), terms);
//        Assert.assertFalse(cashier.isError());
//        Response fail2 = service.addEmpToShift(0, 8, "Cashier");
//        Assert.assertTrue("Expected to fail because amount of cashier is 0 ", fail.isError());
//    }
//
//    @Test
//    public void removeEmpFromShift() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        Map<String, Integer> rolesAmount = new HashMap<>();
//        rolesAmount.put("ShiftManager", 1);
//        rolesAmount.put("Driver", 1);
//        ResponseData<Shift> created = service.createShift(rolesAmount, LocalDate.of(2021, 8, 8), "Morning");
//        Assert.assertTrue("Unexpected Exception error", created.isError());
//        ResponseData<Employee> driver1 = service.addEmployee(2, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver1.isError());
//        Response addTo = service.addEmpToShift(0,driver1.getData().EID,driver1.getData().role.get(0));
//        Assert.assertFalse(addTo.isError());
//        Response logout_p = service.Logout();
//        Assert.assertFalse(logout_p.isError());
//        Response login_d = service.Login(2,"Driver");
//        Assert.assertFalse(login_d.isError());
//        ResponseData<List<Shift>> d1Shifts = service.getMyShifts();
//        Response logout_d = service.Logout();
//        Assert.assertFalse(logout_d.isError());
//        Response login_p = service.Login(1,"PersonnelManager");
//        Assert.assertFalse(login_p.isError());
//        Response remove = service.removeEmpFromShift(6, driver1.getData().EID);
//        Assert.assertTrue("Exception expected - shift does not exist", remove.isError());
//        Response remove2 = service.removeEmpFromShift(0, 2);
//        Assert.assertFalse(remove2.isError());
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response login_d1 = service.Login(2, "Driver");
//        Assert.assertFalse(login_d1.isError());
//        Response remove3 = service.removeEmpFromShift(0, driver1.getData().EID);
//        Assert.assertTrue("Exception expcected - driver cannot remove", remove3.isError());
//       /* Response logout2 = service.Logout();
//        Assert.assertFalse(logout2.isError());
//        Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response login_p = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login_p.isError());
//        Response remove4 = service.removeEmpFromShift(created.getData().SID, driver1.getData().EID);
//        Assert.assertTrue("Expected exception- emmployee no removed from database after reload", remove4.isError());
//
//        */
//    }
//
//
//    @Test
//    public void updateAmountRole() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        Map<String, Integer> rolesAmount = new HashMap<>();
//        rolesAmount.put("ShiftManager", 1);
//        rolesAmount.put("Driver", 1);
//        ResponseData<Shift> created = service.createShift(rolesAmount, LocalDate.of(2021, 8, 8), "Morning");
//        Assert.assertTrue("Exception error", created.isError());
//        Response fail1 = service.updateAmountRole(0, "Driver", -1);
//        Assert.assertTrue("Expected failure - amount is negative", fail1.isError());
//        Response fail2 = service.updateAmountRole(0, "Driverr", 5);
//        Assert.assertTrue("Expected failure - typo is incorrect", fail2.isError());
//        Response fail3 = service.updateAmountRole(7, "Driver", 5);
//        Assert.assertTrue("Expected failure - not shift with SID like this", fail3.isError());
//        Response pass = service.updateAmountRole(0, "Driver", 5);
//        Assert.assertFalse("Unexpected Exception error", pass.isError());
//        ResponseData<Employee> driver1 = service.addEmployee(7, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver1.isError());
//        ResponseData<Employee> driver2 = service.addEmployee(8, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse("Unexpected Exception error because amount was updated from 1 to 5 ", driver2.isError());
//    }
//
//    @Test
//    public void addRoleToEmployee() {
//        Response login = service.Login(1, "PersonnelManager");
//        Assert.assertFalse(login.isError());
//        ResponseData<Employee> driver1 = service.addEmployee(7, "moshe", bank, 10000, "Driver", LocalDate.now(), terms);
//        Assert.assertFalse(driver1.isError());
//        Response fail1 = service.addRoleToEmployee(7, "Chashier");
//        Assert.assertTrue("Exception expected - typo of role", fail1.isError());
//        Response fail2 = service.addRoleToEmployee(8, "Cashier");
//        Assert.assertTrue("Exception expected - eid not exists", fail2.isError());
//        Response pass1 = service.addRoleToEmployee(7, "Cashier");
//        Assert.assertFalse("Unexpected Exception error", pass1.isError());
//        /*Response logout_p = service.Logout();
//        Assert.assertFalse(logout_p.isError());
//        Response login_d = service.Login(7, "Cashier");
//        Assert.assertFalse("Unexpected Exception error", login_d.isError());
//        Response logout = service.Logout();
//        Assert.assertFalse(logout.isError());
//        Response load = service.loadData(1);
//        Assert.assertFalse(load.isError());
//        Response login_d1 = service.Login(7, "Cashier");
//        Assert.assertFalse("Unexpected Exception error", login_d1.isError());
//        Response logout2 = service.Logout();
//        Assert.assertFalse(logout2.isError());
//        Response login_d2 = service.Login(7, "Driver");
//        Assert.assertFalse("Unexpected Exception error", login_d2.isError());
//
//         */
//    }
//}
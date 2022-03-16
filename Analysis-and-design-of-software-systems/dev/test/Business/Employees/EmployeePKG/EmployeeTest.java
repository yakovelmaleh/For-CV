//package Business.Employees.EmployeePKG;
//
//import Business.Employees.ShiftPKG.*;
//import Business.Type.RoleType;
//import Business.Type.ShiftType;
//import org.junit.*;
//import org.junit.Test;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.mockito.Mock;
//
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.util.*;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class EmployeeTest {
//    private int bank[] = {1, 2, 3};
//    private int terms[] = {4, 5, 6};
//    private Employee personnelManager;
//    private Employee driver;
//    private Map<Integer, Employee> employees;
//
//    @Mock
//    private ShiftController shiftController = mock(ShiftController.class);
//
//    @Before
//    public void setUp() throws Exception {
//        personnelManager = new Employee(1, "Niv", bank, 1000, RoleType.PersonnelManager, LocalDate.now(), terms);
//        driver = personnelManager.addEmployee(2,"dor",bank,3000,RoleType.Driver,LocalDate.now(),terms,new HashMap<>());
//        employees = new HashMap<>();
//        employees.put(personnelManager.getEID(),personnelManager);
//        employees.put(driver.getEID(),driver);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//
//    }
//
//    @Test
//    public void addConstConstraint() {
//        Constraint constraintMock = mock(Constraint.class);
//        when(shiftController.addConstConstraint(driver.getEID(),DayOfWeek.MONDAY,ShiftType.Morning,"cant")).thenReturn(constraintMock);
//        when(shiftController.getOnlyEmployeeConstraints(driver.getEID())).thenReturn(new ArrayList<>());
//        when(constraintMock.getCID()).thenReturn(1);
//        Constraint constraint = driver.addConstConstraint(DayOfWeek.MONDAY, ShiftType.Morning, "cant", shiftController);
//        List<Constraint> list = driver.getOnlyEmployeeConstraints(shiftController);
//        list.add(constraintMock);
//        Assert.assertEquals(list.get(0).getCID(), constraint.getCID());
//    }
//
//    @Test
//    public void addConstraint() throws Exception {
//        Constraint constraintMock = mock(Constraint.class);
//        when(shiftController.addConstraint(driver.getEID(),LocalDate.now(),ShiftType.Morning,"cant")).thenReturn(constraintMock);
//        when(shiftController.getOnlyEmployeeConstraints(driver.getEID())).thenReturn(new ArrayList<>());
//        when(constraintMock.getCID()).thenReturn(1);
//        Business.Employees.ShiftPKG.Constraint constraint = driver.addConstraint(LocalDate.now(), ShiftType.Morning, "cant", shiftController);
//        List<Business.Employees.ShiftPKG.Constraint> list = driver.getOnlyEmployeeConstraints(shiftController);
//        list.add(constraintMock);
//        Assert.assertEquals(list.get(0).getCID(), constraint.getCID());
//    }
//
//    @Test
//    public void removeConstraint() throws Exception {
//        Constraint constraintMock = mock(Constraint.class);
//        when(shiftController.addConstraint(driver.getEID(),LocalDate.now(),ShiftType.Morning,"cant")).thenReturn(constraintMock);
//        Constraint constraint = driver.addConstraint(LocalDate.now(), ShiftType.Morning, "cant", shiftController);
//        when(shiftController.removeConstraint(constraintMock.getCID(),driver.getEID())).thenReturn(constraintMock);
//        when(shiftController.removeConstraint(constraintMock.getCID(),driver.getEID())).thenReturn(constraintMock);
//        when(constraintMock.getCID()).thenReturn(1);
//        driver.removeConstraint(constraintMock.getCID(), shiftController);
//        when(shiftController.getOnlyEmployeeConstraints(driver.getEID())).thenReturn(new ArrayList<>());
//        List<Constraint> list = driver.getOnlyEmployeeConstraints(shiftController);
//        Assert.assertFalse(list.contains(constraint));
//    }
//
//    @Test
//    public void updateReasonConstraint() throws Exception {
//        Constraint constraintMock = mock(Constraint.class);
//        when(constraintMock.getReason()).thenReturn("newReason");
//        when(constraintMock.getCID()).thenReturn(1);
//        driver.updateReasonConstraint(constraintMock.getCID(), "newReason", shiftController);
//        Assert.assertNotEquals("didnt update reason", "cant", constraintMock.getReason());
//    }
//
//    @Test
//    public void updateShiftTypeConstraint() throws Exception {
//        Constraint constraintMock = mock(Constraint.class);
//        when(constraintMock.getShiftType()).thenReturn(ShiftType.Night);
//        driver.updateShiftTypeConstraint(constraintMock.getCID(), ShiftType.Night, shiftController);
//        Assert.assertNotEquals("didnt update reason", ShiftType.Morning, constraintMock.getShiftType());
//    }
//
//    @Test
//    public void addEmployeePersonnelPass() {
//        try {
//            Map<Integer, Employee> employees1 = new HashMap<>();
//            personnelManager.addEmployee(5, "bla", bank, 222222, RoleType.Cashier, LocalDate.now(), terms, employees1);
//            Assert.assertTrue(employees1.containsKey(5));
//        } catch (Exception e) {
//            Assert.fail();
//        }
//    }
//
//
//
//    @ValueSource(strings = {"","ass3ASDA","aa3vsv"})
//    @ParameterizedTest
//    public void addEmployeePersonnelFail1(String name) {
//        try {
//            setUp();
//            Map<Integer, Employee> employees1 = new HashMap<>();
//            personnelManager.addEmployee(5, name, bank, 22, RoleType.Cashier, LocalDate.now(), terms, employees1);
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @ValueSource(ints = {0,-5})
//    @ParameterizedTest
//    public void addEmployeePersonnelFail2(int s) {
//        try {
//            setUp();
//            Map<Integer, Employee> employees1 = new HashMap<>();
//            personnelManager.addEmployee(5, "name", bank, s, RoleType.Cashier, LocalDate.now(), terms, employees1);
//            Assertions.fail();
//        } catch (Exception e) {
//        }
//    }
//    @Test
//    public void addEmployeePersonnelFail3() {
//        try {
//            Map<Integer, Employee> employees1 = new HashMap<>();
//            employees1.put(5,driver);
//            personnelManager.addEmployee(5, "name", bank, 22222, RoleType.Cashier, LocalDate.now(), terms, employees1);
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//
//    @Test
//    public void addEmployeeRegularFail() {
//        try {
//            Map<Integer, Employee> employees1 = new HashMap<>();
//            driver.addEmployee(5, "bla", bank, 222222, RoleType.Cashier, LocalDate.now(), terms, employees1);
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void fireEmployeePersonnelPass() {
//        try {
//            Map<Integer, Employee> employees1 = new HashMap<>();
//            personnelManager.addEmployee(5, "bla", bank, 222222, RoleType.Cashier, LocalDate.now(), terms, employees1);
//            personnelManager.fireEmployee(5, employees1);
//            Assert.assertFalse(employees1.containsKey(5));
//        } catch (Exception e) {
//            Assert.fail();
//        }
//    }
//    @Test
//    public void fireEmployeePersonnelFail1() {
//        try {
//            Map<Integer, Employee> employees1 = new HashMap<>();
//            personnelManager.fireEmployee(5, employees1);
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//    @Test
//    public void fireEmployeePersonnelFail2() {
//        try {
//            Map<Integer, Employee> employees1 = new HashMap<>();
//            personnelManager.addEmployee(5, "bla", bank, 222222, RoleType.Cashier, LocalDate.now(), terms, employees1);
//            personnelManager.fireEmployee(1, employees1);
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void fireEmployeeRegular() {
//        try {
//            Map<Integer, Employee> employees1 = new HashMap<>();
//            driver.fireEmployee(5, employees1);
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeNamePersonnelPass() {
//        try {
//            personnelManager.updateEmployeeName(driver.getEID(),"xxx", employees);
//            Assert.assertNotEquals("didnt update name", "dor", driver.getName());
//        } catch (Exception e) {
//            Assert.fail();
//        }
//    }
//    @Test
//    public void updateEmployeeNamePersonnelFail() {
//        try {
//            personnelManager.updateEmployeeName(driver.getEID(),"xx2x", employees);
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeNameRegular() {
//        try {
//            driver.updateEmployeeName(driver.getEID(), "bla",new HashMap<>());
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeSalaryPersonnelPass() {
//        try {
//            personnelManager.updateEmployeeSalary(driver.getEID(), 60,employees);
//            Assert.assertNotEquals("didnt update name", 3000, driver.getSalary());
//        } catch (Exception e) {
//            Assert.fail();
//        }
//    }
//    @Test
//    public void updateEmployeeSalaryPersonnelFail() {
//        try {
//            personnelManager.updateEmployeeSalary(driver.getEID(), -60,new HashMap<>());
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeSalaryRegular() {
//        try {
//            driver.updateEmployeeSalary(driver.getEID(), 60,new HashMap<>());
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeBANumPersonnelPass() {
//        try {
//            personnelManager.updateEmployeeBANum(driver.getEID(), 8,employees);
//            Assert.assertNotEquals("didnt update bankA num", 1, driver.getBankAccount().getAccountNum());
//        } catch (Exception e) {
//            Assert.fail();
//        }
//    }
//    @ParameterizedTest
//    @ValueSource(ints={0,-1})
//    public void updateEmployeeBANumPersonnelFail(int v) {
//        try {
//            setUp();
//            personnelManager.updateEmployeeBANum(driver.getEID(), v,employees);
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeBANumRegular() {
//        try {
//            driver.updateEmployeeBANum(driver.getEID(), 60,new HashMap<>());
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeBABranchPersonnelPass() {
//        try {
//            personnelManager.updateEmployeeBABranch(driver.getEID(), 8,employees);
//            Assert.assertNotEquals("didnt update bank branch", 2, driver.getBankAccount().getBankBranch());
//        } catch (Exception e) {
//            Assert.fail();
//        }
//    }
//    @ParameterizedTest
//    @ValueSource(ints = {0,-1})
//    public void updateEmployeeBABranchPersonnelFail(int v) {
//        try {
//            setUp();
//            personnelManager.updateEmployeeBABranch(driver.getEID(), v,employees);
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeBABranchRegular() {
//        try {
//            driver.updateEmployeeBABranch(driver.getEID(), 60,new HashMap<>());
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeBAIDPersonnelPass() {
//        try {
//            personnelManager.updateEmployeeBAID(driver.getEID(), 8,employees);
//            Assert.assertNotEquals("didnt update bank id", 3, driver.getBankAccount().getBankID());
//        } catch (Exception e) {
//            Assert.fail();
//        }
//    }
//    @ParameterizedTest
//    @ValueSource(ints = {-5,0})
//    public void updateEmployeeBAIDPersonnelFail(int v) {
//        try {
//            setUp();
//            personnelManager.updateEmployeeBAID(driver.getEID(), v,employees);
//            Assert.fail();
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Test
//    public void updateEmployeeBAIDRegular() {
//        try {
//            driver.updateEmployeeBAID(driver.getEID(), 60,new HashMap<>());
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeEducationFundPersonnelPass() {
//        try {
//            personnelManager.updateEmployeeEducationFund(driver.getEID(), 8,employees);
//            int x = driver.getTermsOfEmployment().getEducationFun();
//            Assert.assertNotEquals("didnt update education fund", 4, driver.getTermsOfEmployment().getEducationFun());
//        } catch (Exception e) {
//            Assert.fail();
//        }
//    }
//    @ParameterizedTest
//    @ValueSource(ints = {-5,-777,0})
//    public void updateEmployeeEducationFundPersonnelFail(int v) {
//        try {
//            setUp();
//            personnelManager.updateEmployeeEducationFund(driver.getEID(), v,employees);
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeEducationFundRegular() {
//        try {
//            driver.updateEmployeeEducationFund(driver.getEID(), 60,new HashMap<>());
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeDaysOffPersonnelPass() {
//        try {
//            personnelManager.updateEmployeeDaysOff(driver.getEID(), 8,employees);
//            Assert.assertNotEquals("didnt update days off", 5, driver.getTermsOfEmployment().getDaysOff());
//        } catch (Exception e) {
//            Assert.fail();
//        }
//    }
//
//    @Test
//    public void updateEmployeeDaysOffPersonnelFail1() {
//        try {
//            personnelManager.updateEmployeeDaysOff(driver.getEID(), -5,employees);
//            Assert.fail();
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Test
//    public void updateEmployeeDaysOffRegular() {
//        try {
//            driver.updateEmployeeDaysOff(driver.getEID(), 60,employees);
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeSickDaysPersonnelPass() {
//        try {
//            personnelManager.updateEmployeeSickDays(driver.getEID(), 8,employees);
//            Assert.assertNotEquals("didnt update sick days", 6, driver.getTermsOfEmployment().getSickDays());
//        } catch (Exception e) {
//            Assert.fail();
//        }
//    }
//    @Test
//    public void updateEmployeeSickDaysPersonnelFail() {
//        try {
//            personnelManager.updateEmployeeSickDays(driver.getEID(), -6,employees);
//            Assert.assertNotEquals("didnt update sick days", 6, driver.getTermsOfEmployment().getSickDays());
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void updateEmployeeSickDaysRegular() {
//        try {
//            driver.updateEmployeeSickDays(driver.getEID(), 60,employees);
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void createShift1() {
//        try {
//            Shift shiftMock = mock(Shift.class);
//            when(shiftController.createShift(new HashMap<>(), LocalDate.of(2021, 8, 20), ShiftType.Morning, new HashMap<>())).thenReturn(shiftMock);
//            Shift shift = personnelManager.createShift(new HashMap<>(), LocalDate.of(2021, 8, 20), ShiftType.Morning, new HashMap<>(), shiftController);
//            when(shiftController.getShifts(LocalDate.now().plusWeeks(2))).thenReturn(new ArrayList<>());
//            List<Shift> list = personnelManager.getShifts(LocalDate.now().plusWeeks(2),shiftController);
//            list.add(shiftMock);
//            when(list.get(0).getDate()).thenReturn(LocalDate.of(2021,8,20));
//            when(list.get(0).getShiftType()).thenReturn(ShiftType.Morning);
//            Assert.assertTrue(list.get(0).getDate().equals(LocalDate.of(2021,8,20)) && list.get(0).getShiftType() == ShiftType.Morning);
//        } catch (Exception e) {
//            Assert.fail();
//        }
//    }
//
//    @Test
//    public void createShift2() {
//        try {
//            driver.createShift(new HashMap<>(), LocalDate.of(2021, 8, 20), ShiftType.Morning, new HashMap<>(), shiftController);
//            Assert.fail();
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void removeEmpFromShift1() {
//        try {
//            Shift s = mock(Shift.class);
//            Map<Integer, String[]> map_mock = mock(s.getEmployees().getClass());
//            when(map_mock.containsKey(driver.getEID())).thenReturn(false);
//            personnelManager.removeEmpFromShift(s.getSID(), driver.getEID(), employees.get(driver.getEID()).getRole(), shiftController);
//            Assert.assertFalse(s.getEmployees().containsKey(driver.getEID()));
//        } catch (Exception e) {
//            Assert.fail();
//        }
//    }
//    @Test
//    public void removeEmpFromShift2() {
//        try {
//            Shift s = mock(Shift.class);
//            driver.removeEmpFromShift(s.getSID(), driver.getEID(), employees.get(driver.getEID()).getRole(), shiftController);
//            Assert.fail();
//        } catch (Exception e) {}
//    }
//
//    @Test
//    public void addEmpToShift1() {
//        try {
//            Shift s = mock(Shift.class);
//            personnelManager.addEmpToShift(s.getSID(), driver.getEID(), driver.getRole().get(0), driver.getName(), shiftController);
//            Map<Integer, String[]> m = s.getEmployees();
//            Map<Integer, String[]> mock = mock(m.getClass());
//            when(mock.containsKey(driver.getEID())).thenReturn(true);
//            boolean a =mock.containsKey(driver.getEID());
//            Assert.assertTrue(a);
//        }
//        catch (Exception e){
//            Assert.fail();}
//    }
//    @Test
//    public void addEmpToShift2() {
//        try {
//            Shift s = mock(Shift.class);
//            driver.addEmpToShift(s.getSID(), driver.getEID(), driver.getRole().get(0), driver.getName(), shiftController);
//            Assert.fail();
//        }catch (Exception e){}
//    }
//
//
//    @Test
//    public void updateAmountRole1(){
//        try{
//            Shift s = mock(Shift.class);
//            personnelManager.updateAmountRole(1,RoleType.Driver,5,shiftController);
//            Map<RoleType, Integer> rolesMock = mock(s.getRolesAmount().getClass());
//            when(rolesMock.get(RoleType.Driver)).thenReturn(5);
//            Assert.assertTrue( rolesMock.get(RoleType.Driver)== 5);
//        }catch (Exception e){Assert.fail();}
//    }
//
//    @Test
//    public void updateAmountRole2(){
//        try{
//            driver.updateAmountRole(1,RoleType.Driver,5,shiftController);
//            Assert.fail();
//        }catch (Exception e){}
//    }
//}
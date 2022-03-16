//package Business.Employees.ShiftPKG;
//
//import Business.Employees.EmployeePKG.Employee;
//import Business.Type.RoleType;
//import Business.Type.ShiftType;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.mock;
//
//public class ShiftControllerTest {
//    ShiftController sc;
//
//    @Mock
//    private Employee emp = mock(Employee.class);
//
//
//    @Before
//    public void setUp()  {
//        sc = new ShiftController();
//        sc.createShift(new HashMap<>(),LocalDate.now().plusDays(3),ShiftType.Morning,new HashMap<>());
//    }
//
//    @After
//    public void tearDown()  {
//    }
//
//    @Test
//    public void addConstConstraint() {
//        int size  = sc.getConstraints().size();
//        sc.addConstConstraint(2, DayOfWeek.MONDAY, ShiftType.Morning, "sick");
//        Map<Integer, Constraint> constraints = sc.getConstraints();
//        assertEquals(constraints.size() - 1, size);
//    }
//
//    @Test
//    public void addTempConstraint1() {
//            when(emp.getEID()).thenReturn(2);
//            int size  = sc.getConstraints().size();
//            sc.addTempConstraint(emp, LocalDate.now().plusDays(3), ShiftType.Morning, "sick");
//            Map<Integer, Constraint> constraints = sc.getConstraints();
//            assertEquals(constraints.size() - 1, size);
//    }
//
//    @Test
//    public void addTempConstraint2() {
//            when(emp.getEID()).thenReturn(2);
//            String res = sc.addTempConstraint(emp, LocalDate.of(1990, 10, 19), ShiftType.Morning, "sick");
//            assertFalse(res.isEmpty());
//    }
//
//    @Test
//    public void addTempConstraint3() {
//            when(emp.getEID()).thenReturn(2);
//            sc.createShift(new HashMap<>(),LocalDate.now().plusDays(8),ShiftType.Morning,new HashMap<>());
//            sc.selfMakeWeekShifts();
//            String res = sc.addTempConstraint(emp, LocalDate.now().plusDays(8), ShiftType.Morning, "sick");
//            assertFalse(res.isEmpty());  //cant add constraint for shift that was self
//    }
//
//    @Test
//    public void removeConstraint() {
//            when(emp.getEID()).thenReturn(2);
//            String res = sc.addTempConstraint(emp, LocalDate.now().plusDays(3), ShiftType.Morning, "sick");
//            assertEquals(sc.getMyConstraints(2).size(), 1);
//            sc.removeConstraint(0,2);
//            assertEquals(0, sc.getMyConstraints(2).size());
//    }
//
//
//    @Test
//    public void updateReasonConstraint() {
//            when(emp.getEID()).thenReturn(2);
//            String res = sc.addTempConstraint(emp, LocalDate.now().plusDays(3), ShiftType.Morning, "sick");
//            String str = "tired";
//            sc.updateReasonConstraint(0, str,2);
//            Map<Integer, Constraint> constraints = sc.getConstraints();
//            Constraint c = constraints.get(0);
//            assertEquals(str, c.getReason());
//    }
//
//    @Test
//    public void updateShiftTypeConstraint() {
//            when(emp.getEID()).thenReturn(2);
//            String res = sc.addTempConstraint(emp, LocalDate.now().plusDays(3), ShiftType.Morning, "sick");
//            sc.updateShiftTypeConstraint(0, ShiftType.Night,2);
//            Map<Integer, Constraint> constraints = sc.getConstraints();
//            Constraint c = constraints.get(0);
//            assertEquals(ShiftType.Night, c.getShiftType());
//    }
//
//
//    @Test
//    public void defaultShifts1() {
//            Map<ShiftType, Map<RoleType, Integer>> defaultShifts = new HashMap<>();
//            HashMap<RoleType, Integer> rolesAmount = new HashMap<>();
//            rolesAmount.put(RoleType.Cashier, 2);
//            rolesAmount.put(RoleType.Driver, 0);
//            defaultShifts.put(ShiftType.Morning, rolesAmount);
//            sc.defaultShifts(defaultShifts);
//            assertEquals(2, (int) sc.getDefaultShifts().get(ShiftType.Morning).get(RoleType.Cashier));
//            assertEquals(0, (int) sc.getDefaultShifts().get(ShiftType.Morning).get(RoleType.Driver));
//    }
//
//    @Test
//    public void defaultShifts2() {
//            Map<ShiftType, Map<RoleType, Integer>> defaultShifts = new HashMap<>();
//            HashMap<RoleType, Integer> rolesAmount = new HashMap<>();
//            rolesAmount.put(RoleType.Cashier, -2);
//            defaultShifts.put(ShiftType.Morning, rolesAmount);
//            String res = sc.defaultShifts(defaultShifts);
//            assertFalse(res.isEmpty()); // role amount is negative
//    }
//
//    @Test
//    public void getOnlyEmployeeConstraints() {
//        when(emp.getEID()).thenReturn(2);
//        sc.addConstConstraint(2, DayOfWeek.THURSDAY, ShiftType.Morning, "sick");
//        String res = sc.addTempConstraint(emp, LocalDate.now().plusDays(3), ShiftType.Morning, "feel bad");
//        List<Constraint> list = sc.getMyConstraints(2);
//        boolean ok = list.size() == 2;
//        for (Constraint c : list) {
//            if (c.getEID() != 2 || !(c.getReason().equals("sick") | c.getReason().equals("feel bad"))) {
//                ok = false;
//                break;
//            }
//        }
//        assertTrue(ok);
//    }
//
//}
package Presentation;

import Business.ApplicationFacade.DriverRoleController;
import Business.ApplicationFacade.ManagerRoleController;
import Business.ApplicationFacade.RegularRoleController;
import Business.ApplicationFacade.iControllers.iDriverRoleController;
import Business.ApplicationFacade.iControllers.iManagerRoleController;
import Business.ApplicationFacade.iControllers.iRegularRoleController;
import Business.StockBusiness.Fcade.StorageService;
import Business.SupplierBusiness.facade.SupplierService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class Controllers {
    private final iRegularRoleController rc;
    private final iManagerRoleController mc;
    private final iDriverRoleController dc;
    private final TransportationController tc;
    private final SupplierService sc;
    private final StorageService st;
    private int currBID;

    public Controllers() {
        rc = new RegularRoleController();
        mc = new ManagerRoleController(rc.getUtils());
        dc = new DriverRoleController(mc);
        tc = new TransportationController(mc);
        sc = new SupplierService(tc);
        st = new StorageService(sc);
        sc.setStockService(st);
        currBID = -1;
    }

    public iManagerRoleController getMc() {
        return mc;
    }

    public iRegularRoleController getRc() {
        return rc;
    }

    public iDriverRoleController getDc() {
        return dc;
    }

    public TransportationController getTc() {
        return tc;
    }

    public SupplierService getSc() {
        return sc;
    }

    public StorageService getSt() {
        return st;
    }

    public void init() {
        initializeEmpsModule();
        initializeStorage();
        initializeTransportationModule();
        initializeSuppModule();
    }

    void initializeStorage(){
        StorageService s= st;
        s.addStore(1);
        s.addStore(2);
        s.addStore(3);
        s.useStore(1, sc);
        s.addCategory("a");
        s.addCategory("b");
        s.addCategory("c");
        s.addCategory("d");
        s.useStore(2, sc);
        s.addCategory("a");
        s.addCategory("b");
        s.addCategory("c");
        s.addCategory("d");
        s.useStore(3, sc);
        s.addCategory("a");
        s.addCategory("b");
        s.addCategory("c");
        s.addCategory("d");

    }

    private void initializeTransportationModule() {


        tc.addTruck(1,3500,"Subaro",3000,3500);
        tc.addTruck(2,4500,"Volvo",4000,5000);
        tc.addTruck(3,11000,"Mercedese",7000,12000);
        tc.addTruck(4,13500,"Dodge",10000,15000);


        int[] bankDetails = {123, 456, 789};
        int[] terms = {1000, 5, 10};
        dc.addNewDriver(100, "Driver1", bankDetails, 40000,LocalDate.now(),terms,13000);
        dc.addNewDriver(101, "Driver2", bankDetails, 40000,LocalDate.now(),terms,13000);
        }


    private void initializeEmpsModule(){
        int[] bankDetails = {123, 456, 789};
        int[] terms = {1000, 5, 10};
        rc.createBranch(1, "PersonnelManager", bankDetails, 150000, terms,"sivan","Tel Aviv",12,2,"South","rom","507350111");
        rc.createBranch(2, "PersonnelManager", bankDetails, 120000, terms,"Zalman","Haifa",12,2,"Center","dor","512156465");
        rc.createBranch(3, "PersonnelManager", bankDetails, 100000, terms,"Alenbi","Beer-Sheva",12,2,"North","bar","507350111");
        rc.EnterBranch(1);
        rc.Login(1);
        mc.addEmployee(40, "LogisticManager", bankDetails, 10000, "LogisticManager", LocalDate.now(), terms);
        mc.addEmployee(4, "SorterA", bankDetails, 10000, "Sorter", LocalDate.now(), terms);
        mc.addEmployee(5, "SorterB", bankDetails, 10000, "Sorter", LocalDate.now(), terms);
        mc.addEmployee(6, "StoreKeeperA", bankDetails, 10000, "StoreKeeper", LocalDate.now(), terms);
        mc.addEmployee(7, "CashierA", bankDetails, 10000, "Cashier", LocalDate.now(), terms);
        mc.addEmployee(8, "CashierB", bankDetails, 10000, "Cashier", LocalDate.now(), terms);
        mc.addEmployee(9, "StoreKeeperB", bankDetails, 10000, "StoreKeeper", LocalDate.now(), terms);
        mc.addEmployee(10, "CashierC", bankDetails, 10000, "Cashier", LocalDate.now(), terms);
        mc.addEmployee(11, "ShiftManagerA", bankDetails, 40000, "ShiftManager", LocalDate.now(), terms);
        mc.addEmployee(12, "ShiftManagerB", bankDetails, 40000, "ShiftManager", LocalDate.now(), terms);
        mc.addEmployee(200, "BranchManagerA", bankDetails, 40000, "BranchManager", LocalDate.now(), terms);
        Map<String, Integer> morning = new HashMap<>();
        morning.put("Driver", 0);
        morning.put("Cashier", 1);
        morning.put("Sorter", 1);
        morning.put("ShiftManager", 1);
        morning.put("StoreKeeper", 0);
        Map<String, Integer> night = new HashMap<>();
        night.put("Cashier", 1);
        night.put("ShiftManager", 1);
        night.put("Driver", 0);
        night.put("Sorter", 1);
        night.put("StoreKeeper", 0);
        SortedMap<String, Map<String, Integer>> defaultRolesAmount = new TreeMap<>();
        defaultRolesAmount.put("Night", night);
        defaultRolesAmount.put("Morning", morning);
        mc.defaultShifts(defaultRolesAmount);
        rc.Logout();

        rc.EnterBranch(2);
        rc.Login(2);
        mc.addEmployee(41, "LogisticManager", bankDetails, 10000, "LogisticManager", LocalDate.now(), terms);
        mc.addEmployee(27, "SorterA", bankDetails, 10000, "Sorter", LocalDate.now(), terms);
        mc.addEmployee(28, "SorterB", bankDetails, 10000, "Sorter", LocalDate.now(), terms);
        mc.addEmployee(13, "StoreKeeperA", bankDetails, 10000, "StoreKeeper", LocalDate.now(), terms);
        mc.addEmployee(14, "CashierA", bankDetails, 10000, "Cashier", LocalDate.now(), terms);
        mc.addEmployee(15, "CashierB", bankDetails, 10000, "Cashier", LocalDate.now(), terms);
        mc.addEmployee(16, "StoreKeeperB", bankDetails, 10000, "StoreKeeper", LocalDate.now(), terms);
        mc.addEmployee(17, "CashierC", bankDetails, 10000, "Cashier", LocalDate.now(), terms);
        mc.addEmployee(18, "ShiftManagerA", bankDetails, 40000, "ShiftManager", LocalDate.now(), terms);
        mc.addEmployee(19, "ShiftManagerB", bankDetails, 40000, "ShiftManager", LocalDate.now(), terms);
        mc.addEmployee(201, "BranchManagerB", bankDetails, 40000, "BranchManager", LocalDate.now(), terms);
        mc.defaultShifts(defaultRolesAmount);
        rc.Logout();

        rc.EnterBranch(3);
        rc.Login(3);
        mc.addEmployee(42, "LogisticManager", bankDetails, 10000, "LogisticManager", LocalDate.now(), terms);
        mc.addEmployee(29, "SorterA", bankDetails, 10000, "Sorter", LocalDate.now(), terms);
        mc.addEmployee(30, "SorterB", bankDetails, 10000, "Sorter", LocalDate.now(), terms);
        mc.addEmployee(20, "StoreKeeperA", bankDetails, 10000, "StoreKeeper", LocalDate.now(), terms);
        mc.addEmployee(21, "CashierA", bankDetails, 10000, "Cashier", LocalDate.now(), terms);
        mc.addEmployee(22, "CashierB", bankDetails, 10000, "Cashier", LocalDate.now(), terms);
        mc.addEmployee(23, "StoreKeeperB", bankDetails, 10000, "StoreKeeper", LocalDate.now(), terms);
        mc.addEmployee(24, "CashierC", bankDetails, 10000, "Cashier", LocalDate.now(), terms);
        mc.addEmployee(25, "ShiftManagerA", bankDetails, 40000, "ShiftManager", LocalDate.now(), terms);
        mc.addEmployee(26, "ShiftManagerB", bankDetails, 40000, "ShiftManager", LocalDate.now(), terms);
        mc.addEmployee(202, "BranchManagerC", bankDetails, 40000, "BranchManager", LocalDate.now(), terms);
        mc.defaultShifts(defaultRolesAmount);
        rc.Logout();


        rc.EnterBranch(1);
        rc.Login(6);
        rc.addConstConstraint(DayOfWeek.SUNDAY, "Night", "tired");
        rc.Logout();
        rc.Login(1);
        rc.EnterBranch(1);
        mc.createWeekShifts();
        mc.createShift(morning,LocalDate.now(),"Morning");
        mc.createShift(night,LocalDate.now(),"Night");
        rc.Logout();


        rc.EnterBranch(2);
        rc.Login(2);
        mc.createWeekShifts();
        mc.createShift(morning,LocalDate.now(),"Morning");
        mc.createShift(night,LocalDate.now(),"Night");
        rc.Logout();

        rc.EnterBranch(3);
        rc.Login(3);
        mc.createWeekShifts();
        mc.createShift(morning,LocalDate.now(),"Morning");
        mc.createShift(night,LocalDate.now(),"Night");
        rc.Logout();


    }

    private void initializeSuppModule() {
        sc.addSupplier("Supplier1", 0, 0, 0, "cash");
        sc.addSupplier("Supplier2", 1, 1, 1, "bank transfer");
        sc.addSupplier("Supplier3", 2, 2, 2, "bank transfer");
        sc.addSupplier("Supplier4", 3, 3, 3, "check");
        sc.addSupplier("Supplier5", 4, 4, 4, "cash");
        st.useStore(1, sc);
        sc.addItem( 0, "meat", 21.99, 19.99, 2,
                "Tnuva", 1, LocalDate.now().plusYears(1), 1);
        sc.addQuantityDocument(0, 1, 2, 1);
        sc.addItem( 0, "minced meat", 39.99, 34.99, 100,
                "Dabah", 1, LocalDate.now().plusYears(1), 2);
        sc.addQuantityDocument(0, 2, 2, 2);
        sc.addItem( 1, "stake", 70.5, 65, 2,
                "Dabah", 1, LocalDate.now().plusMonths(1), 1.5);
        sc.addQuantityDocument(1, 3, 2, 3);
        st.useStore(2, sc);
        sc.addItem( 2, "yogurt", 9.99, 8.99, 2,
                "Tara", 2, LocalDate.now().plusMonths(1), 0.1);
        sc.addQuantityDocument(2, 4, 3, 1);
        sc.addItem( 3, "cheese cake", 35, 34, 2,
                "Cheese Cake Factory", 2, LocalDate.now().plusDays(14), 1);
        sc.addQuantityDocument(3, 5, 3 , 2);
        sc.addItem( 0, "gum", 1.99, 1.5, 2,
                "Hubba", 3, LocalDate.now().plusYears(1000), 0.01);
        sc.addQuantityDocument(0, 6, 3 , 3);
        st.useStore(3, sc);
        sc.addItem( 1, "lollipop", 0.5, 0.35, 2,
                "Hubba", 3, LocalDate.now().plusYears(1000), 0.01);
        sc.addQuantityDocument(1, 7, 2, 1);
        sc.addItem( 2, "windshield wiper", 15.99, 14, 2,
                "Sano", 3, LocalDate.now().plusYears(1), 0.5);
        sc.addQuantityDocument(2, 8, 2, 2);
        sc.addItem( 3, "broom", 19.99, 17, 2,
                "Sano", 3, LocalDate.now().plusYears(50), 1);
        sc.addQuantityDocument(3, 9, 3, 3);
        st.useStore(2, sc);
        sc.addItem( 0, "soda", 10.99, 9, 2,
                "Cola-Cola", 3, LocalDate.now().plusMonths(6), 3);
        sc.addQuantityDocument(0, 10, 2, 1);
        sc.addItem( 0, "sprite ", 10.99, 8, 700,
                "Cola-Cola", 3, LocalDate.now().plusMonths(3), 1.5);
        sc.addQuantityDocument(0, 11, 2, 2);
        sc.addItem( 1, "fanta", 10.99, 10, 2,
                "Cola-Cola", 3, LocalDate.now().plusMonths(3), 1.5);
        sc.addQuantityDocument(1, 12, 2, 3);
        st.useStore(3, sc);
        sc.addItem( 1, "grape juice", 10.99, 9.99, 2,
                "PriGat", 3, LocalDate.now().plusMonths(9), 1);
        sc.addQuantityDocument(1, 13, 3, 1);
        sc.addItem( 1, "orange juice", 10.99, 9.99, 2,
                "PriGat", 3, LocalDate.now().plusMonths(9), 1);
        sc.addQuantityDocument(1, 14, 3, 1);
        sc.addItem( 2, "water", 5.99, 4.5, 2,
                "Neviot", 3, LocalDate.now().plusYears(3), 2);
        sc.addQuantityDocument(2,15, 3, 2);
        st.useStore(1, sc);
        sc.addItem( 3, "apple", 1.99, 1.5, 2,
                "Sami", 3, LocalDate.now().plusMonths(1), 0.05);
        sc.addQuantityDocument(3, 16, 3, 1);
        sc.addItem( 3, "orange", 1.99, 1.5, 2,
                "Sami", 3, LocalDate.now().plusMonths(1), 0.05);
        sc.addQuantityDocument(3, 17, 2, 3);
        sc.addItem( 0, "tomato", 0.99, 0.5, 2,
                "Orna", 3, LocalDate.now().plusMonths(1), 0.05);
        sc.addQuantityDocument(0, 18, 2, 2);
        sc.addItem( 0, "onion", 1.5, 1, 2,
                "Orna", 3, LocalDate.now().plusMonths(1), 0.05);
        sc.addQuantityDocument(0, 19, 2, 3);

        sc.addSupplierAgreement(0, 200, 1, true, true);
        sc.addSupplierAgreement(1, 100, 0, false, true);
        sc.addSupplierAgreement(2, 150, 2, true, false);
        sc.addSupplierAgreement(3, 100, 1, false, false);
        sc.addContactEmail(0, "BGU@bgu.ac.il", "Rami");
        sc.addContactEmail(0, "Lucture@bgu.ac.il", "Rami");
        sc.addContactEmail(1, "Intel@gamil.com", "Lidor");
        sc.addContactEmail(3, "PleaseGiveUs100@bgu.ac.il", "Kfir");
        sc.addContactEmail(3, "PleaseGiveUs100@gamil.com", "Ori");
        sc.addContactEmail(4, "AmirTheKing@bgu.ac.il", "Amir");
        sc.addContactEmail(4, "Cotel@GOD.com", "Gabriel");
        sc.addContactEmail(4, "SneBoher@GOD.com", "Moshes");
        sc.addContactPhone(0, "050-0000000", "Tali");
        sc.addContactPhone(1, "000-0000000", "Jesus");
        sc.addContactPhone(2, "054-1234567", "Hani");
        sc.addContactPhone(3, "054-9849521", "Kfir");
        sc.addContactPhone(3, "052-4737283", "Ori");
        Hashtable<Integer, Integer> firstHash = new Hashtable<>();
        Hashtable<Integer, Integer> secondHash = new Hashtable<>();
        Hashtable<Integer, Integer> thirdHash = new Hashtable<>();
        Hashtable<Integer, Integer> fourthHash = new Hashtable<>();
        firstHash.put(1, 3);
        firstHash.put(6, 4);
        firstHash.put(10, 5);
        secondHash.put(3, 1);
        secondHash.put(7, 2);
        secondHash.put(12, 4);
        thirdHash.put(4, 1);
        thirdHash.put(8, 2);
        thirdHash.put(15, 1);
        fourthHash.put(5, 2);
        fourthHash.put(9, 4);
        fourthHash.put(16,1);
        st.useStore(1, sc);
        sc.addRegularOrder(0, 1, firstHash);
        st.useStore(2, sc);
        sc.addRegularOrder(1, 2, secondHash);
        st.useStore(2, sc);
        sc.addRegularOrder(2, 2, thirdHash);
        sc.addRegularOrder(3, 2, fourthHash);
        sc.addItemToOrder(0, 1, 1, 2);
        sc.addItemToOrder(0, 1, 2, 5);
        sc.addItemToOrder(2, 3, 4, 10);
        sc.addItemToOrder(3, 4, 5, 4);
        sc.addItemToOrder(3, 4, 9, 5);
        sc.addItemToOrder(1, 2, 3, 1);
    }


    public int getCurrBID() {
        return currBID;
    }

    public void setCurrBID(int currBID) {
        this.currBID = currBID;
    }


}

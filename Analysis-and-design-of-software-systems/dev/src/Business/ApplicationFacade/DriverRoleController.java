package Business.ApplicationFacade;

import Business.ApplicationFacade.iControllers.iDriverRoleController;
import Business.ApplicationFacade.iControllers.iManagerRoleController;
import DataAccess.DriverMapper;
import Business.Employees.EmployeePKG.Driver;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

public class DriverRoleController implements iDriverRoleController {
    private iManagerRoleController mc;
    private final DriverMapper dm;

    public DriverRoleController(iManagerRoleController m) {
        mc = m;
        dm=DriverMapper.getMapper();
    }


    public void addNewDriver(int newEID, String name, int[] bankDetails, int salary, LocalDate startWorkDate, int[] terms, int license) {
        Driver driver = mc.addDriver(newEID,name,bankDetails,salary,startWorkDate,terms,license);
        dm.insert(driver);
    }

    public boolean checkAvailableStoreKeeperAndShifts(int BID, LocalDate date, LocalTime leavingTime) {
        mc.EnterBranch(BID);
        String shiftType = leavingTime.getHour() < 14 ? "Morning" : "Night";
        boolean res = mc.ShiftExist(date, shiftType);
        if(res)
            res = mc.StoreKeeperAvailable(date, shiftType);
        return  res;
    }

    public boolean checkAvailableDriver(int BID, LocalDate date, LocalTime leavingTime) {
        mc.EnterBranch(BID);
        String shiftType = leavingTime.getHour() < 14 ? "Morning" : "Night";
        boolean res = mc.ShiftExist(date, shiftType);
        if(res)
            res = mc.DriverAvailable(date, shiftType);
        return  res;
    }

    public void removeDriverFromShiftAndStorekeeper(int BID, int driverID, LocalDate date, LocalTime leavingTime) {
        mc.EnterBranch(BID);
        String shiftType = leavingTime.getHour() < 14 ? "Morning" : "Night";
        mc.removeDriverFromShiftAndStorekeeper(driverID,date,shiftType);
    }
    // INSERT INTO ManagerAlerts (MID, BID, EID, Date, Message) VALUES(?,?,?,?,?);
    public List<Integer> allPersonnelManager(int BID){
        return dm.allPersonnelManager(BID);
    }

    public void changeDriver(List<Integer> BIDS, int oldDriverID, int newDriverID,  LocalDate date, LocalTime leavingTime){
        String shiftType = leavingTime.getHour() < 14 ? "Morning" : "Night";
        for (Integer bid: BIDS){
            mc.EnterBranch(bid);
            mc.removeDriverFromShift(oldDriverID,date,shiftType);
            mc.addDriverToShift(newDriverID,date,shiftType);
        }
    }
    public List<Driver> chooseDriver(LocalDate date, LocalTime leavingTime) throws Exception {
        String shiftType = leavingTime.getHour() < 14 ? "Morning" : "Night";
        List<Integer> driversID =  mc.getAllAvailableDrivers(date,shiftType);
        List<Driver> drivers = new LinkedList<>();
        for (int id:driversID ) {
            drivers.add(getDriver(id));
        }
        return (drivers.isEmpty())?(null):drivers;
    }
    public Driver getDriver(int id) throws Exception {
       return dm.select(id);
    }
    public void addDriverToShiftAndStoreKeeper(int BID,int driverID, LocalDate date, LocalTime leavingTime) {
        mc.EnterBranch(BID);
        String shiftType = leavingTime.getHour() < 14 ? "Morning" : "Night";
        mc.addDriverAndStoreKeeperToShift(driverID,date,shiftType);
    }

    public boolean isDriver(int eid) {
       try{
           Driver d= dm.select(eid);
           return  d !=null;
       }
       catch (Exception e){
           return false;
       }
    }
}

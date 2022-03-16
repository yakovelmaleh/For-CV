package Business.Transportation;

import Business.ApplicationFacade.DriverRoleController;
import Business.ApplicationFacade.Response;
import Business.ApplicationFacade.ResponseData;
import Business.ApplicationFacade.iControllers.iManagerRoleController;
import Business.ApplicationFacade.outObjects.DriverServiceDTO;
import Business.ApplicationFacade.outObjects.TransportationServiceDTO;
import Business.ApplicationFacade.outObjects.TruckServiceDTO;
import Business.Employees.EmployeePKG.Driver;
import Business.SupplierBusiness.Order;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class TransportationService {

    private final DataControl dataControl;
    private final DriverRoleController drivers;
    private final TruckService tc;

    public TransportationService(iManagerRoleController mc) {

        dataControl=new DataControl();
        drivers = new DriverRoleController(mc);
        tc=new TruckService();
    }

    private long getId() {
        try {
            return dataControl.getCurrID()+1;
        }
        catch (Exception e){
            return -1;
        }
    }

    /**
     * Method to return all transportations that has been made.
     * @return : list of all transportations.
     */
    public List<Transportation> getTransportationsList() throws Exception {
        return dataControl.getTransportationsList();
    }


    public Branch getBranchById(int id) throws Exception {
       return dataControl.getBranch(id);
    }

    public boolean cancelTran(long tranId) throws Exception {
        Transportation toDelete = dataControl.getTransportation(tranId);
        if(toDelete==null)
        {
            System.out.println("Transportation with this id is not exists.");
            return false;
        }
        List<Order> orders = toDelete.getOrderList();
        for (Order order:orders){
            order.removeOrder();
            drivers.removeDriverFromShiftAndStorekeeper(order.getBranchID(),toDelete.getDriver().getEID(),toDelete.getDate(),toDelete.getLeavingTime());
        }
       return dataControl.remove(tranId);
    }


    public long addOrderToTransportation(Order order, double weight) throws Exception {
        Branch bran = getBranchById(order.getBranchID());
        List<Transportation> trans = dataControl.getTransportationsByArea(bran.getArea());
        for (Transportation tran : trans) {
            if(tran.getOrders().containsKey(order.getOrderId())){
                dataControl.updateOrder(tran.getId(),weight,order);
                return tran.getId();
            }
            else{
                boolean shift=(tran.getLeavingTime().isBefore(LocalTime.of(14,0)));
                boolean shiftNow=(LocalTime.now().isBefore(LocalTime.of(14,0)));
                if (tran.canAdd(order)&&((tran.getDate().isAfter(LocalDate.now())||!(!shift&&shiftNow)))) {
                    dataControl.updateTransWeight(tran.getId(), tran.getWeight()+order.getTotalWeight(), order);
                return tran.getId();
            }}
        }
        if(weight!=0){
            return -1;
        }
        LocalDate days = LocalDate.now().plusDays(7);
        LocalTime noon = LocalTime.parse("15:00");
        LocalTime morning = LocalTime.parse("09:00");
        List<Truck> trucks = dataControl.getTrucksByWeight(order.getTotalWeight());
        if (trucks.isEmpty()) {
            announceManagers(order.getBranchID(),days
                    ,"no compatible truck was found for new Order: \nOrder id: " +order.getOrderId() + "\tDate: "
                            + LocalDate.now().toString() + "\tTime: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
            throw new IllegalArgumentException("No truck compatible for this order's weight. ");
        }
        Driver chosenDriver = null;
        LocalDate date = null;
        LocalTime leavingTime= null;
        boolean found = false;
        Truck  chooseTruck=null;
        for (LocalDate i = LocalDate.now(); i.compareTo(days) <= 0 & !found; i = i.plusDays(1)) {
            List<Driver> driverList = new LinkedList<>();
            if (drivers.checkAvailableStoreKeeperAndShifts(bran.getId(), i, morning) & drivers.checkAvailableDriver(bran.getId(), i, morning)) {
                if(i.compareTo(LocalDate.now())!=0 | !(LocalTime.now().compareTo(LocalTime.of(14,0))>=0)) {
                    driverList = drivers.chooseDriver(i, morning);
                    leavingTime = morning;
                }
                else {
                    if (drivers.checkAvailableStoreKeeperAndShifts(bran.getId(), i, noon) & drivers.checkAvailableDriver(bran.getId(), i, noon)) {
                        driverList = drivers.chooseDriver(i, noon);
                        leavingTime = noon;
                    }
                }
            } else if (drivers.checkAvailableStoreKeeperAndShifts(bran.getId(), i, noon) & drivers.checkAvailableDriver(bran.getId(), i, noon)) {
                driverList = drivers.chooseDriver(i, noon);
                leavingTime = noon;
            }
            else
                continue;
            List<Long> unavailableTrucks= dataControl.getTransportations(i,  leavingTime).stream().map(t->t.getTruck().getId()).collect(Collectors.toList());
            List<Truck> newTrucks=new ArrayList<>();
            for(Truck t: trucks){
                if(!unavailableTrucks.contains(t.getId()))
                    newTrucks.add(t);
            }
            chooseTruck = newTrucks.get(0);
            List<Integer> unavailableDriver= dataControl.getTransportations(i,leavingTime).stream().map(t->t.getDriver().getEID()).collect(Collectors.toList());
           List<Driver> newDrivers=new ArrayList<>();
            for(Driver d: driverList){
               if(!unavailableDriver.contains(d.getEID()))
                   newDrivers.add(d);
           }
            for (Driver d:newDrivers) {
                if(d.getLicense()>=chooseTruck.getLicense()){
                    date = i;
                    chosenDriver = d;
                    found = true;
                    break;
                }
            }
        }
        if(chosenDriver == null){
            announceManagers(order.getBranchID(),days,"no compatible driver was found for new Order: " +order.getOrderId() + "\n at Date: " + LocalDate.now().toString() + ",time: " + LocalTime.now().toString());
            throw new IllegalArgumentException("no driver compatible for this order");
        }
        if(chooseTruck == null){
            announceManagers(order.getBranchID(),days,"no compatible truck was found for new Order: " +order.getOrderId() + "\n at Date: " + LocalDate.now().toString() + ",time: " + LocalTime.now().toString());
            throw new IllegalArgumentException("no truck compatible for this order");
        }
        HashMap <Integer,Order> newOrdersList = new HashMap<>();
        newOrdersList.put(order.getOrderId(),order);
        Transportation transportation = new Transportation(getId(),date,leavingTime,bran.getArea(),chosenDriver,chooseTruck,order.getTotalWeight(),newOrdersList);
        dataControl.addTransportation(transportation);
        drivers.addDriverToShiftAndStoreKeeper(order.getBranchID(),chosenDriver.getEID(),date,leavingTime);
        return transportation.getId();
    }

    public ResponseData<List<TransportationServiceDTO>> getDTOTransportations() {
        List<TransportationServiceDTO> returnT = new LinkedList<>();
        try {
            List<Transportation> transportations = getTransportationsList();
            for (Transportation t: transportations){
                returnT.add(toTransportationServiceDTO(t));
            }
            return new ResponseData<>(returnT);
        }catch (Exception e){
            return new ResponseData<>(e.getMessage());
        }
    }
    private void announceManagers(int branchId,LocalDate days,String msg) throws Exception {
        List<Integer> MIDs = drivers.allPersonnelManager(branchId);
        for (int manager:MIDs){
            dataControl.insertAlerts(branchId,manager,days,msg);
        }
    }
    private TransportationServiceDTO toTransportationServiceDTO(Transportation t){

        HashMap<Integer,Business.SupplierBusiness.facade.outObjects.Order> ordersService=new HashMap<>();
        for(Order order : t.getOrderList())
            ordersService.put(order.getOrderId(),new Business.SupplierBusiness.facade.outObjects.Order(order));
        return new TransportationServiceDTO(t.getId(),t.getDate(),t.getLeavingTime(),t.getArea(),new DriverServiceDTO(t.getDriver()),new TruckServiceDTO(t.getTruck()),t.getWeight(),ordersService);
    }
    public void addTruck(long id, int maxWeight, String model, int netWeight, int license){
        dataControl.addTruck(id, maxWeight,model,netWeight,license);
    }
    public List<TransportationServiceDTO> getTransportations( LocalDate date, LocalTime time){
        List<TransportationServiceDTO> returnT = new LinkedList<>();
        try {
            List<Transportation> transportations = dataControl.getTransportations(date,time);
            for (Transportation t: transportations){
                returnT.add(toTransportationServiceDTO(t));
            }
            return returnT;
        }catch (Exception e){
            throw  new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * @return All drivers who can replace the driver in the transportation.
     * @throws Exception
     */


    public ResponseData<List<Integer>> checkAvailableDriverSubs(int driverID, String time, LocalDate date,List<Integer> optionalDrivers) throws Exception{
        try {
            List<Integer> used=dataControl.getTransportations(date,time.equals("Morning")?LocalTime.of(9,0):LocalTime.of(15,0)).stream().map(t->t.getDriver().getEID()).collect(Collectors.toList());
            Transportation t = dataControl.selectTransWithDriverShift(driverID, time, date);
            List<Integer> ret = new LinkedList<>();
            Driver d = drivers.getDriver(driverID);
            for (int dId: optionalDrivers){
                if(drivers.getDriver(dId).getLicense() >= d.getLicense()& !used.contains(dId)){
                    ret.add(dId);
                }
            }
            return new ResponseData<>(ret);
        }catch (Exception ex) {
            return new ResponseData<>(ex.getMessage());
        }
    }
    //2
    public Response swapDrivers(int newDriverID, int oldDriverID, String time, LocalDate date){
        try{
            Transportation t = dataControl.selectTransWithDriverShift(oldDriverID, time, date);
            dataControl.replaceDrivers(t.getId(),newDriverID);
            Driver d = drivers.getDriver(newDriverID);
            t.setDriver(d);
            drivers.changeDriver(t.getTransBranches(),oldDriverID,newDriverID,date,t.getLeavingTime());
            return new Response();
        }catch (Exception ex) {
            return new Response(ex.getMessage());
        }
    }

    public void removeOrderFromTransportation(long transID, int orderId) {
        try {
            Transportation t=dataControl.getTransportation(transID);
            int branId= t.getOrders().get(orderId).getBranchID();
            if(t.getDate().compareTo(LocalDate.now())>0)
                throw new IllegalArgumentException("Cannot remove order from transportation who already left.");
            dataControl.removeOrderFromTransportation(transID, orderId);
            if(t.getOrderList().isEmpty()){
                dataControl.remove(transID);
                drivers.removeDriverFromShiftAndStorekeeper(branId,t.getDriver().getEID(),t.getDate(),t.getLeavingTime());
            }
        }
        catch(Exception e){
            //System.out.println("Could not remove order number: "+orderId+" from transportation number: "+transID+" .Error: "+e.getMessage());
        }

    }
}

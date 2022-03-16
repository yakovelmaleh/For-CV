package Business.Transportation;

import Business.SupplierBusiness.Order;
import Business.Type.Area;
import DataAccess.BranchMapper;
import DataAccess.DriverMapper;
import DataAccess.TransportationMapper;
import DataAccess.TruckMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

public class DataControl {

    private BranchMapper branchMapper;
    private TruckMapper truckMapper;
    private DriverMapper driverMapper;
    private TransportationMapper transportationMapper;
    static  private String dbName;

    public DataControl(){
        dbName="databaseDemo.db";
        branchMapper=BranchMapper.getMapper();
        truckMapper=TruckMapper.getMapper();
        transportationMapper=TransportationMapper.getMapper();
        driverMapper=DriverMapper.getMapper();

    }

    public List<Truck> getTrucks() throws Exception {
        return truckMapper.getTrucks();
    }
    public List<Truck> getTrucksByWeight(double weight) throws Exception {
        return truckMapper.getTrucksByWeight(weight);
    }

    public Branch getBranch(int id) throws Exception {
        return branchMapper.getBranch(id);
    }

    public Truck getTruck(long id) throws Exception {
        return truckMapper.getTruck(id);
    }
    public Transportation getTransportation(long id) throws Exception {
        Transportation t= transportationMapper.getTransportation(id,truckMapper,driverMapper);
        t.setOrders(getOrdersByTran(t.getId()));
        return t;
    }

    public void insertAlerts (int bid, int eid, LocalDate date, String message) throws Exception {
        transportationMapper.insertAlerts(bid,eid,date,message);
    }
    public void addTransportation( Transportation tra) throws Exception {
        transportationMapper.addTransportation(tra);
    }

    public List<Transportation> getTransportationsList() throws Exception {
        List<Transportation> trans= transportationMapper.getAllTransportations(truckMapper,driverMapper);
        for(Transportation t: trans){
            t.setOrders(getOrdersByTran(t.getId()));
        }
        return trans;
    }

    public boolean remove(long idCounter) throws IOException {
        return transportationMapper.deleteTrans(idCounter);
    }


    public long getCurrID() throws Exception {
        return transportationMapper.getCurrId();
    }

    public void addTruck(long id, int maxweight,String model, int netWeight, int license){truckMapper.addTruck(id,maxweight,model,netWeight,license);}

    public List<Transportation> getTransportationsByArea(Area area) throws IOException {
       List<Transportation> trans= transportationMapper.getTransportationsByArea(truckMapper,driverMapper,area);
       for(Transportation t: trans){
           HashMap<Integer,Order> orders=getOrdersByTran(t.getId());
           t.setOrders(orders);
       }
       return trans;
    }

    public void updateTransWeight(long id, double weight,Order order) throws IOException {
        transportationMapper.updateTransWeight(id,weight);
        transportationMapper.changeWeight(id,weight,order);
    }

    public List<Transportation> getTransportations( LocalDate date, LocalTime time) throws IOException {
        List<Transportation> trans= transportationMapper.getTransportationsByDate(date,time,truckMapper,driverMapper);
        for(Transportation tran: trans){
            tran.setOrders(getOrdersByTran(tran.getId()));
        }
        return trans;
    }
    public Transportation selectTransWithDriverShift(int driverID, String time,LocalDate date) throws Exception {
        Transportation t= transportationMapper.selectTransWithDriverShift(driverID,date,time,truckMapper,driverMapper);
        t.setOrders(getOrdersByTran(t.getId()));
        return t;
    }
    private HashMap<Integer,Order> getOrdersByTran(Long tranId) {
        try {
            return transportationMapper.getOrdersByTranId(tranId.intValue());
        }
        catch (Exception e){
            System.out.println("Fail. getOrdersByTran()->DataControl -> "+e.getMessage());
            return new HashMap<>();
        }
    }

    public void replaceDrivers(long id, int newDriverID) throws Exception {
        transportationMapper.replaceDrivers(id,driverMapper.select(newDriverID));
    }

    public boolean removeOrderFromTransportation(long transID, int orderId) throws IOException {
        return transportationMapper.removeOrderFromTransportation(transID,orderId);
    }

    public void updateOrder(long id, double oldWeight, Order order) {
        transportationMapper.updateOrder(id,oldWeight,order);
    }
}

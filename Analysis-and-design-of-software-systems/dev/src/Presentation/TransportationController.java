package Presentation;

import Business.ApplicationFacade.Response;
import Business.ApplicationFacade.ResponseData;
import Business.ApplicationFacade.iControllers.iManagerRoleController;
import Business.ApplicationFacade.outObjects.TransportationServiceDTO;
import Business.SupplierBusiness.Order;
import Business.Transportation.TransportationService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TransportationController {
    private final TransportationService serviceControl;


    public List<Integer> checkAvailableDriverSubs(int driverID, String time, LocalDate date,List<Integer> optionalDrivers) {
        try {


            ResponseData<List<Integer>> res = serviceControl.checkAvailableDriverSubs(driverID, time, date, optionalDrivers);
            if (res.isError()) {
                //System.out.println("Could not check available drivers for replacement. Error: " + res.getError());
                return new ArrayList<>();
            } else {
                return res.getData();
            }
        } catch (Exception e) {
            //System.out.println("Could not check available drivers for replacement. Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void swapDrivers(int newDriverID, int oldDriverID, String time, LocalDate date){
        Response res = serviceControl.swapDrivers(newDriverID,oldDriverID,time,date);
        if(res.isError()) {
            //System.out.println("Could not swap drivers. Error: "+res.getError());
        }
    }
    public TransportationController(iManagerRoleController mc){
        serviceControl = new TransportationService(mc);
    }


    public List<TransportationServiceDTO> getAllTransportations(){

        ResponseData<List<TransportationServiceDTO>> res = serviceControl.getDTOTransportations();
        if(res.isError()){
            //System.out.println("Could not get all transportation. Error: "+res.getError());
            return new ArrayList<>();
        }
        return res.getData();
    }
    public boolean delete(long id) {
        try{
            return serviceControl.cancelTran(id);
        }
        catch (Exception e){
            //System.out.println("Could not delete transportation. Error: "+e.getMessage());
            return false;
        }
    }

    public void addTruck(long id, int maxWeight,String model, int netWeight, int license) {
        try {
            serviceControl.addTruck(id, maxWeight, model, netWeight, license);
        }
        catch (Exception e){
            //System.out.println("Could not add truck. Error: "+e.getMessage());
        }
    }
    public List<TransportationServiceDTO> getTransportations(int currBID, LocalDate date, LocalTime time) {
        try {
            return serviceControl.getTransportations(date, time);
        }
        catch (Exception e){
            //System.out.println("Could not get transportations by date and bid. Error: "+e.getMessage());
            return new ArrayList<>();
        }
    }
    public long addOrderToTransportation(Order order, double weight) {
        try {
            return serviceControl.addOrderToTransportation(order, weight);
        }
        catch (Exception e){
            //System.out.println("Could not add order to trans. Error: "+e.getMessage());
            return -1;
        }
    }
    public void removeOrderFromTransportation(long transID, int orderId) {
        try {
             serviceControl.removeOrderFromTransportation(transID,orderId);
        }
        catch (Exception e){
            //System.out.println("Could not remover order from trans. Error: "+e.getMessage());
        }
    }

}

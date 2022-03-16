package Business.Transportation;
import java.util.*;


public class TruckService {


    private DataControl dataControl;

    public TruckService(){
        dataControl=new DataControl();
    }
    public List<Truck> getTrucksList() throws Exception {

        return dataControl.getTrucks();
    }


    public Truck getTruck(Long truckId) throws Exception {
        return dataControl.getTruck(truckId);
    }

}

package Business.ApplicationFacade.outObjects;
import Business.SupplierBusiness.facade.outObjects.Order;
import Business.Type.Area;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

public class TransportationServiceDTO {

    private long id;
    private LocalDate date;
    private LocalTime leavingTime;
    private DriverServiceDTO driver;
    private TruckServiceDTO truck;
    private double weight;
    private Area shippingArea;
    private HashMap <Integer, Order> orders;

    @Override
    public String toString() {
        String output="Transportation : \tid = " + id ;
        if(date==null)
            output+="\tDate: ";
        else
            output+="\tDate = " + date;
        if(leavingTime==null)
            output+="\tLeavingTime : ";
        else
            output+="\tLeavingTime : " + leavingTime;
        if(driver==null)
            output+="\n\tDriver : ";
        else
            output+=  "\n\tDriver : " + driver;
        if(truck==null)
            output+="\n\tTruck : ";
        else
            output+=   "\n\tTruck : " + truck;
        if(orders==null)
            output+="\n\tOrders : ";
        else
            output+=   "\n\tOrders : " + orders  ;
        output+=   "\n\tweight : " + weight +"\n\n";


        return output;

    }

    public TransportationServiceDTO(long id, LocalDate date, LocalTime leavingTime,Area area ,DriverServiceDTO driver, TruckServiceDTO truck, double weight, HashMap<Integer, Order> orderS) {
        this.date = date;
        this.id = id;
        this.driver = driver;
        this.truck = truck;
        this.weight = weight;
        this.leavingTime = leavingTime;
        orders=orderS;
        this.shippingArea=area;
    }

    public void setId(int id) { this.id = id; }

    public DriverServiceDTO getDriver() { return driver; }

    public double getWeight() { return weight; }

    public LocalDate getDate() { return date; }

    public long getId() { return id; }

    public void setDate(LocalDate date) { this.date = date; }

    public void setDriver(DriverServiceDTO driver) { this.driver = driver; }

    public void setWeight(int weight) { this.weight = weight; }

    public Area getArea() { return shippingArea; }

    public void setArea(Area area) { this.shippingArea = area; }
    public HashMap<Integer,Order> getOrders(){return orders;}

}

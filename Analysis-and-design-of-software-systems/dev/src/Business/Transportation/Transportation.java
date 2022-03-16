package Business.Transportation;
import Business.Employees.EmployeePKG.Driver;
import Business.Type.Area;
import Business.SupplierBusiness.Order;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


public class Transportation {
    private long id;
    private LocalDate date;
    private LocalTime leavingTime;
    private Driver driver;
    private Truck truck;
    private double weight;
    private Area shippingArea;
    private HashMap <Integer, Order> orders;

    public Transportation(long id) {
        this.id = id;
        date = null;
        leavingTime = null;
        driver = null;
        truck = null;
        weight = -1;
        shippingArea = null;
        orders=new HashMap<>();
    }

    public Transportation(long id, LocalDate date, LocalTime leavingTime,Area area, Driver driver ,Truck truck, double weight, HashMap<Integer, Order> orderS) {
        this.date = date;
        this.id = id;
        this.driver = driver;
        this.truck = truck;
        this.shippingArea=area;
        this.weight = weight;
        this.leavingTime = leavingTime;
        orders=orderS;
    }

    public Order removeOrder(int orderId){
        if(!orders.containsKey(orderId)){
            throw new IllegalArgumentException("order id: "+ orderId+" not found on transportation is: "+ id);
        }
        Order retOrder = orders.get(orderId);
        weight=weight-retOrder.getTotalWeight();
        orders.remove(orderId);
        return  retOrder;
    }
    /**
     * Sets new data.
     * Check if the date is later than the date it was typed..
     * @param date: the date to set to.
     */
    public void setDate(LocalDate date) {
        if (LocalDate.now().compareTo(date)>0) {
            throw new IllegalArgumentException("the date is: " + LocalDate.now() + " but u set: " + date + "to be the date.");
        }
        this.date = date;
    }




    /**
     * Set driver to the transportation.
     * Will not allow to set driver before truck.
     * Check that driver's license is compatible.
     * @param driver: The driver to set to.
     */
    public void setDriver(Driver driver) {
        if (truck == null) {
            throw new IllegalArgumentException("Please choose a truck before u choose a Driver");
        } else if ((driver.getLicense())<(truck.getLicense())) {
            throw new IllegalArgumentException("ur driver license is:" + driver.getLicense() + "but ur truck license is: " + truck.getNetWeight());
        } else {
            this.driver = driver;
        }
    }
    public List<Integer> getTransBranches(){
        return orders.values().stream().map(o-> o.getBranchID()).collect(Collectors.toList());
    }

    public void setId(int id) {
        this.id = id;
    }


    /**
     * sets the transportation weight.
     * Checks if the weight is legal.
     * @param weight : the weight to set to.
     */
    public void setWeight(double weight){
       if(truck!=null && weight > truck.getMaxWeight()){
            throw new IllegalArgumentException("Warning!!! the curr weight is mismatch to max truck wight.\n");
        }
        this.weight = weight;
    }

    public double getWeight() { return weight; }
    public Truck getTruck() { return truck; }
    public long getId() {
        return id;
    }
    public Area getArea() {
        return shippingArea;
    }
    public LocalDate getDate() {
        return date;
    }
    public Driver getDriver() {
        return driver;
    }
    public LocalTime getLeavingTime() {
        return leavingTime;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transportation that = (Transportation) o;
        return id == that.id && weight == that.weight && Objects.equals(date, that.date) && Objects.equals(leavingTime, that.leavingTime) && Objects.equals(driver, that.driver) && Objects.equals(truck, that.truck) ;
    }

    @Override
    public String toString() {
        return "Transportation{" +
                "id=" + id +
                ", date=" + date +
                ", leavingTime=" + leavingTime +
                ", driver=" + driver +
                ", truck=" + truck +
                ", weight=" + weight +
                ", shippingArea=" + shippingArea +
                ", orders=" + orders +
                '}';
    }

    public boolean canAdd(Order order){
        if(weight+order.getTotalWeight()<=truck.getMaxWeight()){
            return true;
        }
        return false;
    }

    public boolean isEmpty(){return orders.isEmpty();}
    public  Map<Integer,Order> getOrders() {return  orders;}
    public List<Order> getOrderList(){return new ArrayList<>(orders.values());}

    public void addOrder(Order order) {
        orders.put(order.getOrderId(),order);
    }

    public void replaceOrder(Order order) {
        orders.replace(order.getOrderId(),order);
    }

    public void setOrders(HashMap<Integer, Order> ret) { this.orders=ret; }
}

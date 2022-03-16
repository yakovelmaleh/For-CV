package Business.Transportation;

import java.util.Objects;

//bar
public class Truck {

    private long id;
    private  int license;
    private String model;
    private int netWeight;
    private int maxWeight;

    public  Truck(long id,int license, int maxWeight, int netWeight, String model){
        this.license=license;
        this.maxWeight=maxWeight;
        this.model=model;
        this.netWeight=netWeight;
        this.id=id;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public int getLicense() { return license; }

    public int getMaxWeight() { return maxWeight; }

    public int getNetWeight() { return netWeight; }

    public String getModel() { return model; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Truck truck = (Truck) o;
        return license == truck.license && netWeight == truck.netWeight && maxWeight == truck.maxWeight && Objects.equals(model, truck.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(license, model, netWeight, maxWeight);
    }

    @Override
    public String toString() {
        return "Truck{" +
                "licenseNumber=" + license +
                ", model='" + model + '\'' +
                ", netWeight=" + netWeight +
                ", maxWeight=" + maxWeight +
                '}';
    }

}


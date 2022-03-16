package Business.Transportation;

import Business.Type.Area;

public class Branch {

    String phone;
    String contactName;
    int id;
    Address address;
    Area area;


    public Branch(String phone, String contactName, int id, Address address, Area shippingArea) {
        this.address=address;
        this.contactName=contactName;
        this.id=id;
        this.area=shippingArea;
        this.phone=phone;
    }

    public String getPhone () {return phone;}

    public Address getAddress() { return address; }

    public Area getArea() { return area; }

    public String getContactName() { return contactName; }

    public int getId() { return id;}
}

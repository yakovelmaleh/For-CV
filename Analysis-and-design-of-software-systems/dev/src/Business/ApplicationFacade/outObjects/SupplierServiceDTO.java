package Business.ApplicationFacade.outObjects;

import java.util.Objects;

public class SupplierServiceDTO {

    private String phone;
    private String contactName;
    private int id;
    private  String Area;

    @Override
    public String toString() {
        return " - Supplier:  " +
                "\t\tId = " + id +
                "\t\tArea = " + Area+
                "\t\tContactName = " + contactName+
                "\t\tPhone = " + phone+"\n";
    }

    public SupplierServiceDTO(String phone, String contactName, int id, String area ){

        this.phone=phone;
        this.contactName=contactName;
        this.id=id;
        this.Area=area;
    }

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    public String getArea() { return Area; }

    public String getContactName() { return contactName; }

    public String getPhone() { return phone; }

    public void setArea(String area) { Area = area; }

    public void setContactName(String contactName) { this.contactName = contactName; }

    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupplierServiceDTO that = (SupplierServiceDTO) o;
        return id == that.id &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(contactName, that.contactName) &&
                Objects.equals(Area, that.Area);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, contactName, id, Area);
    }
}

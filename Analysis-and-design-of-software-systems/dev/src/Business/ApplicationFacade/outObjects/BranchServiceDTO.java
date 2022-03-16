package Business.ApplicationFacade.outObjects;

import Business.Transportation.Branch;

import java.util.Objects;

public class BranchServiceDTO {

    private String phone;
    private String contactName;
    private int id;
    private  String Area;

    public BranchServiceDTO(String phone, String contactName, int id, String area ){
        this.phone=phone;
        this.contactName=contactName;
        this.id=id;
        this.Area=area;
    }
    public BranchServiceDTO(Branch b){
        this.phone=b.getPhone();
        this.contactName=b.getContactName();
        this.id=b.getId();
        this.Area=b.getArea().toString();
    }

    @Override
    public String toString() {
        return "- Branch " +
                "\tid=" + id +
                "\tArea='" + Area +
                "\tcontactName='" + contactName +
                "\tphone='" + phone+"\n";
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
        BranchServiceDTO that = (BranchServiceDTO) o;
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

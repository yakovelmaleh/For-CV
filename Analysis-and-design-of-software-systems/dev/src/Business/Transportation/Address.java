package Business.Transportation;

import java.util.Objects;

//bar
public class Address {

    private int number;
    private String street;
    private String city;

    public Address(int number, String street, String city){

        this.city=city;
        this.number=number;
        this.street=street;
    }
    public Address(){ }
    public int getNumber() {return this.number;}
    public String getStreet(){ return this.street;}
    public String getCity(){return this.city;}
    public void setNumber(int num){this.number=num;}
    public void setStreet(String street){this.street=street;}
    public void setCity(String city){this.city=city;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return number == address.number && street.equals(address.street) && city.equals(address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, street, city);
    }

    @Override
    public String toString() {
        return "Address: " + '\n' +
                "number=" + number + '\n' +
                ", street='" + street + '\n' +
                ", city='" + city + '\n';
    }
}

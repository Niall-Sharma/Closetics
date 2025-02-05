package org.springframework.samples.petclinic.pets;

public class owner {
    private String name;
    private String telephoneNumber;
    private String address;

    public owner(){
        this.name = "John Doe";
        this.address = "SECRET";
        this.telephoneNumber = "555-555-5555";
    }

    public owner(String name, String telephoneNumber, String address) {
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "owner{" +
                "name='" + name + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

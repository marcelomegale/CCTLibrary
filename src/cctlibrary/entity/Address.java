package cctlibrary.entity;

/**
 * This class represents the Address entity.
 */
public class Address {
    
    private String streetAddress;
    
    private String city;
    
    private String state;
    
    private String zipCode;

    public Address(String streetAddress, String city, String state, String zipCode) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "Address{" + "streetAddress=" + streetAddress + ", city=" + city + 
                ", state=" + state + ", zipCode=" + zipCode + '}';
    }
    
}

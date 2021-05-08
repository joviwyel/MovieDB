
package models;

import java.util.Map;

public class Customer {
    
    private Integer id;
    private String firstName;
    private String lastName;
    private String ccId;
    private String address;
    private String email;
    private String password;

    public Customer() {

    }
    
    public Customer(Integer id, String firstName, String lastName, String ccId, String address, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ccId = ccId;
        this.address = address;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCcId() {
        return ccId;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCcId(String ccId) {
        this.ccId = ccId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public static Customer buildCustomerFromRowResult(Map<String, Object> rowMap) {
        
        Integer id = (Integer) rowMap.get("id");
        String firstName = (String) rowMap.get("first_name");
        String lastName = (String) rowMap.get("last_name");
        String ccId = (String) rowMap.get("cc_id");
        String email = (String) rowMap.get("email");
        String address = (String) rowMap.get("address");
        String password = (String) rowMap.get("password");
        
        return new Customer(id, firstName, lastName, ccId, address, email, password);
    }
    
}
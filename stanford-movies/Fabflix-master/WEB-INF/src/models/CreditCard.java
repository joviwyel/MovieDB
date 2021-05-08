
package models;

import java.sql.Date;

public class CreditCard {
    
    private String id;
    private String firstName;
    private String lastName;
    private Date expiration;

    public CreditCard() {

    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}
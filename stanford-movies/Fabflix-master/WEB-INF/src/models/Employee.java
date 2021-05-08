package models;

import java.util.Map;

/**
 * Created by kbendick on 5/4/16.
 */
public class Employee {

    public String email;
    public String password;
    public String fullName;

    public Employee() { }

    public Employee(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public static Employee buildEmployeeFromRowResult(Map<String, Object> rowMap) {

        String fullName = (String) rowMap.get("fullname");
        String email = (String) rowMap.get("email");
        String password = (String) rowMap.get("password");

        return new Employee(email, password, fullName);
    }

}

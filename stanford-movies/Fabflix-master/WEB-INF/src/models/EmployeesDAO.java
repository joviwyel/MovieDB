package models;

import java.util.List;
import java.util.Map;

/**
 * Created by kbendick on 5/4/16.
 */
public class EmployeesDAO {


    public static Employee verifyAccount(String email, String password) {
        try {
            String sql = String.format("SELECT * FROM employees WHERE email='%s' AND password='%s' LIMIT 1;", email, password);
            List<Map<String, Object>> matchingEmployees = QueryProcessor.processReadOp(sql);
            if (!matchingEmployees.isEmpty()) {
                return Employee.buildEmployeeFromRowResult(matchingEmployees.get(0));
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}

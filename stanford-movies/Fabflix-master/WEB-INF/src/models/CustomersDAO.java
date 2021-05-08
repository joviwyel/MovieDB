
package models;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class CustomersDAO {

    public static int insert(Customer customer) throws Exception {
        
        // Verify the CC is on file.
        String ccVerificationSql = "SELECT COUNT(*) AS count FROM creditcards WHERE id = " + customer.getCcId() + ";";
        
        List<Map<String, Object>> matchingCreditCards;
        try {
            matchingCreditCards = QueryProcessor.processReadOp(ccVerificationSql);
            if (matchingCreditCards.isEmpty()) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception("ERROR - There was a problem connecting to the CC database. The cc id provided could not be verified. Insertion aborted.");
        }
        
        long matchingCards = (Long) matchingCreditCards.get(0).get("count");
        
        if (matchingCards == 0) {
            throw new Exception("ERROR - The credit card id provided does not match one in the database. Insert not processed.");
        }
        
        
        // Attempt the insertion.
        String custInsertSql = "INSERT INTO customers " +
                               "(first_name, last_name, cc_id, address, email, password) " +
                               "VALUES(" + "'" + customer.getFirstName() + "'," + 
                                           "'" + customer.getLastName() + "'," + 
                                           "'" + customer.getCcId() + "'," + 
                                           "'" + customer.getAddress() + "'," +
                                           "'" + customer.getEmail() + "'," +
                                           "'" + customer.getPassword() + "'" +
                               ");";
        
        try {
            return QueryProcessor.processWriteOp(custInsertSql);
        } catch (SQLException e) {
            throw new Exception("ERROR - There was a problem connecting to the customer database. Insertion aborted.");
        }
    }
    
    
    public static Customer verifyAccount(String email, String password) {
        try {       
            String sql = String.format("SELECT * FROM customers WHERE email='%s' AND password='%s' LIMIT 1;", email, password);
            List<Map<String, Object>> matchingCustomers = QueryProcessor.processReadOp(sql);
            if (!matchingCustomers.isEmpty()) {
                return Customer.buildCustomerFromRowResult(matchingCustomers.get(0));
            } else {
                return null;
            }   
        } catch (Exception e) {
            return null;
        }
    }
    
    
//  public static void promptAndInsertNewCustomer() {
//      try {
//          Customer customer = Prompt.forCustomer();
//          System.out.println("\nAttempting to add to the database...");
//          int numUpdates = insert(customer);
//          System.out.println("The update was successful.");
//          System.out.printf("%d rows in the database were affected.\n", numUpdates);
//      } catch (Exception e) {
//              System.out.println(e.getMessage());
//      }   
//  }
    
    
//  public static int delete(String ccId) throws Exception {
//      
//      String sql = "DELETE FROM customers WHERE cc_id = ?";
//          
//      try (PreparedStatement stmt = DBConnectionManager.getInstance().getConnection().prepareStatement(sql)) {
//              stmt.setString(1, ccId);
//              return stmt.executeUpdate();
//      } catch (SQLException e) {
//              throw new Exception("ERROR - There was a problem connecting to the customer database. Delete request not processed.");
//      }
//
//  }
//  
//  public static void promptAndDeleteCustomerByCcId() {
//      try {
//          String ccId = Prompt.forString("\nEnter the credit card id of the customer to delete");
//          int numCustomersDeleted = delete(ccId);
//          System.out.printf("%d customers with the corresponding CC id were deleted.\n", numCustomersDeleted);
//      } catch (Exception e) {
//          System.out.println(e.getMessage());
//      }
//  }

}

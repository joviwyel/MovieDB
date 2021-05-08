package models;


import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SalesDAO {

    public static boolean insert(Customer customer, SessionCart cart) {

        java.util.Date todaysDate = new java.util.Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(todaysDate).toString();

        for (CartItem item : cart.getCartItems()) {
            try {
                String sqlFmtString = "INSERT INTO sales(customer_id, movie_id, sale_date) VALUES('%s', '%s', '%s')";
                String sql = String.format(sqlFmtString, customer.getId(), item.getMovieId(), today);

                for (int i = 0; i < item.getQuantity(); i++) {
                    QueryProcessor.processWriteOp(sql);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        return true;
    }
}

package models;

/**
 * Created by kbendick on 4/20/16.
 */
public class CreditCardsDAO {

    /*
    Checkout: The transaction succeeds only if the user can provide the first name,
    a last name,a credit card with an expiration date, which match a record in the
    creditcards table (not those in the customers table). If succeeded, the
    transaction should be recorded in the system (in the "sales" table) and a
    confirmation page should be displayed.
    */
    /**
     * Returns true if a cc matching the given info is on file.
     * @param
     */
    public static Boolean ccIsOnFile(String id, String firstName, String lastName, String expiration) {

        String sqlFmtString =  "SELECT * "
                             + "FROM creditcards "
                             + "WHERE id='%s' AND first_name='%s' AND last_name='%s' AND expiration='%s' ;";
        String sql = String.format(sqlFmtString, id, firstName, lastName, expiration);

        try {
            return !QueryProcessor.processReadOp(sql).isEmpty();
        } catch (Exception e) {
            return null;
        }
    }
}



package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.annotation.Resource;

public class DBConnectionManager {

    private static final String DB_HOST = "localhost";
    private static final String DB_CONNECTION_STRING = "jdbc:mysql://";
    private static final String DB_WRITE_HOST = "52.71.243.62:3306";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";

    private static Map<String, String> validUserPasswordsMap;

    private static String userName = "testuser";
    private static String userPassword = "testpass";
    private static String dbName = "moviedb";
    private static DBConnectionManager instance; // Singleton

    private static Context initContext = null;
    private static Context envContext = null;

    private static DataSource dataSource = null;

    private static Connection connection = null;
    private static boolean didLoadDriver = false;

    /*
     * This class is not to be instantiated directly. It is a singleton.
     */
    private DBConnectionManager() {
        // Build the map of valid usernames and passwords.
        validUserPasswordsMap = new HashMap<String, String>();
        validUserPasswordsMap.put("root", "");
        initDataSource();
    }


    private static void initDataSource() {

        try {
            initContext = new InitialContext();
            envContext = (Context) initContext.lookup("java:comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/moviedb");
        } catch (final NamingException e) {
            System.out.println("DBConnectionManager#initDataSource: Failed to initialize the data source");
            e.printStackTrace();
        }
    }

    /**
     * Accessor for the singleton DBConnectionManager object
     * 
     * @return the DBConnectionManager object, or null if the driver cannot be
     *         loaded.
     */
    public static DBConnectionManager getInstance() {
        try {
            if (instance == null) {
                if (!didLoadDriver) {
                    Class.forName(DB_DRIVER).newInstance();
                    didLoadDriver = true;
                }
                instance = new DBConnectionManager();
            }

            return instance;
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Attempts to establish connection to the database.
     */
    private void openConnection() throws SQLException {
        connection = DriverManager.getConnection(DB_CONNECTION_STRING + DB_HOST + "/" + dbName, userName, userPassword);
    }

    public Connection getWriteConnection() throws SQLException {

        return DriverManager.getConnection(DB_CONNECTION_STRING + DB_WRITE_HOST + "/" + dbName, userName, userPassword);
    }
    public Connection getConnection() throws SQLException {

        if (dataSource == null || envContext == null || initContext == null) {
            initDataSource();
        }

        return dataSource.getConnection();
    }

    /**
     * Closes the connection to the database.
     */
    public void close() {

        try {
            envContext.close();
        } catch (final Exception e) {
        } // No Big Deal!

        try {
            envContext.close();
        } catch (final Exception e) {
        } // No Big Deal!

        try {
            connection.close();
        } catch (Exception e) {
        } // No big deal!
    }

    /**
     * Gets and verifies login credentials from the user and uses them to
     * establish connection to the database.
     */
    public static void login() {
        // Attempt to establish connection with database.
        DBConnectionManager.getInstance().setUserName(userName);
        DBConnectionManager.getInstance().setUserPassword(userPassword);
        try {
            if (DBConnectionManager.getInstance().getConnection() != null) {
                System.out.println("Login successful..");
                return;
            }
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
            System.out.println("Unable to connect to the database.");
            System.exit(-1);
        }
    }

    /* Getters */
    public Map<String, String> getValidUserPasswordsMap() {
        return this.validUserPasswordsMap;
    }

    /* Setters */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setDBName(String dbName) {
        this.dbName = dbName;
    }

}

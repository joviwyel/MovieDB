
package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryProcessor {

    private static Connection dbConnection;

    static {
        try {
            dbConnection = DBConnectionManager.getInstance().getConnection();
        } catch (Exception e) {
            System.err.println("Failed to establish a connection to the database.");
            System.err.println("Exception reported: " + e.getMessage());
            System.exit(-1);
        }
    }

    public static List<Map<String, Object>> processReadOp(String sqlQuery) throws Exception {

        if (dbConnection == null || dbConnection.isClosed()) {
            dbConnection = DBConnectionManager.getInstance().getConnection();
        }

        // Compile and run the query.
        sqlQuery = preprocess(sqlQuery);
        Statement selectStatement = dbConnection.createStatement();
        ResultSet results = selectStatement.executeQuery(sqlQuery);
        ResultSetMetaData metadata = results.getMetaData();

        // Store the results in a list mapping the column name to the returned
        // objects.
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        while (results.next()) {
            Map<String, Object> row = new HashMap<String, Object>();
            for (int i = 1; i <= metadata.getColumnCount(); i++) {
                row.put(metadata.getColumnName(i), results.getObject(i));
            }
            rows.add(row);
        }

        // Close the database connection etc.
        results.close();
        selectStatement.close();

        return rows;
    }

    public static List<Map<String, Object>> processReadOp(PreparedStatement preparedStatement) throws Exception {

        // Run the query.
        ResultSet results = preparedStatement.executeQuery();
        ResultSetMetaData metadata = results.getMetaData();

        // Store the results in a list mapping the column name to the returned
        // objects.
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        while (results.next()) {
            Map<String, Object> row = new HashMap<String, Object>();
            for (int i = 1; i <= metadata.getColumnCount(); i++) {
                row.put(metadata.getColumnName(i), results.getObject(i));
            }
            rows.add(row);
        }

        // Close the database connection etc.
        results.close();
        return rows;
    }


    public static int processWriteOp(String sqlString) throws SQLException {
        Statement statement = null;
        sqlString = preprocess(sqlString);
        try {
            statement = dbConnection.createStatement();
            return statement.executeUpdate(sqlString);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public static void execute(String sqlQuery) {

        // Preprocessing input SQL
        sqlQuery = sqlQuery.toUpperCase();
        if (!sqlQuery.endsWith(";")) {
            sqlQuery += ";";
        }

        try {
            if (sqlQuery.startsWith("SELECT")) {

                List<Map<String, Object>> queryRows = QueryProcessor.processReadOp(sqlQuery);
                System.out.printf("%d results returned.\n", queryRows.size());
                for (int i = 0; i < queryRows.size(); i++) {
                    System.out.printf("Item %d:\n", i + 1);
                    for (Map.Entry<String, Object> rowEntry : queryRows.get(i).entrySet())
                        System.out.println("\t" + rowEntry.getKey() + ": " + rowEntry.getValue().toString());
                }

            } else if (sqlQuery.startsWith("UPDATE") || sqlQuery.startsWith("DELETE")
                    || sqlQuery.startsWith("INSERT")) {
                int rowsMutated = QueryProcessor.processWriteOp(sqlQuery);
                System.out.printf("%d rows affected.\n");
            } else {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            System.out.println("Please enter a valid SQL query starting with SELECT, UPDATE, DELETE, or INSERT.");
        }
    }

    public static String preprocess(String sql) {
        // Preprocessing input SQL
        if (!sql.trim().endsWith(";")) {
            return sql + ";";
        } else {
            return sql;
        }
    }

    // public static List<Map<String, Object>> select(String outputCols, String
    // tableName, String rowCondition) throws Exception {
    // // Load the driver
    //
    //
    // // Establish the connection
    // Connection connection = null;
    // ResultSet results = null;
    // ResultSetMetaData metadata = null;
    // Statement selectStatement = null;
    //
    // // Build the SQL query
    // String query = "SELECT "+outputCols+" FROM "+tableName;
    // if (!"".equals(rowCondition)) {
    // query = query + " WHERE "+rowCondition;
    // }
    // query = query + ";";
    //
    // try {
    // // Compile and run the query.
    // connection = DBConnectionManager.getInstance().getConnection();
    // selectStatement = connection.createStatement();
    // results = selectStatement.executeQuery(query);
    // metadata = results.getMetaData();
    //
    // // Store the results in a list mapping the column name to the returned
    // objects.
    // List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
    // while (results.next()) {
    // Map<String, Object> row = new HashMap<String, Object>();
    // for (int i = 1; i <= metadata.getColumnCount(); i++) {
    // row.put(metadata.getColumnName(i), results.getObject(i));
    // }
    // rows.add(row);
    // }
    // return rows;
    //
    // } finally {
    // results.close();
    // selectStatement.close();
    // }
    // }

}
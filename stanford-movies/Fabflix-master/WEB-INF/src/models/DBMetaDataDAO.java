package models;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * MetaDataDAO - MetaData Data Access Object
 */
public class DBMetaDataDAO {

    public static final int TABLE_NAME_COL = 3;
    public static final int COLUMN_NAME_COL = 4;


    /**
     * Maps a table name to a mapping of the column names in that table to their respective types.
     */
    public static Map<String, Map<String, String>> getSchema() {

        Connection connection = null;
        ResultSet tables = null;
        ResultSet columns = null;
        Map<String, Map<String, String>> schemaMap = new HashMap<String, Map<String, String>>();

        try {

            connection = DBConnectionManager.getInstance().getConnection();

            // Get all the tables in the database.
            tables = connection.getMetaData().getTables(null, null, null, null);
            while (tables.next()) {
                schemaMap.put(tables.getString("TABLE_NAME"), new HashMap<String, String>>());
            }

            // Get all of the columns for each found table.
            for (String table : schemaMap.keySet()) {
                Map<String, String> tableColumns = schemaMap.get(table);
                columnMetaData = connection.getMetaData().getColumns(null, null, tableToColumns.getKey(), null);
                while (columnMetaData.next()) {
                    tableColumns.put(columnMetaData.getString("COLUMN_NAME"), columnMetaData.getString("TYPE_NAME"));
                }
            }

            return schemaMap;

        } catch (SQLException e) {
            return Collections.emptyMap();
        } finally {
            try {
                tables.close();
            } catch (SQLException e) { } // No worries.
            try {
                columns.close();
            } catch (SQLException e) { } // No worries.
        }
    }

    public static void printDBSchema() {
        System.out.println();
        Map<String, Map<String, String>> dbSchema = DBMetaDataDAO.getSchema();
        for (Map.Entry<String, Map<String, String>> tableSchema : dbSchema.entrySet()) {
            System.out.println("Table: " + tableSchema.getKey());
            System.out.println("Columns: " + tableSchema.getValue().toString());
            System.out.println();
        }
    }
}

package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLQueryExtender implements SQLQueryBuilder {

    @Autowired
    private DataSource dataSource;

    private List<String> tablesList;

    @Override
    public String queryForTable(String tableName) throws SQLException {
//        List<String> tablesList = getTables();
        if (tablesList == null) {                                       // if tablesList not initialized
            this.tablesList = getTables();                              // updates tablesList ONCE
        }
        if (tablesList == null || !tablesList.contains(tableName)) {    // if tablesList is empty, or wrong tableName query
            return null;
        }

        try (Connection connection = dataSource.getConnection()) {
            java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
            try(ResultSet columns = databaseMetaData.getColumns(null,null, tableName, null)){
                StringBuilder stringBuilder = new StringBuilder("SELECT");
                while(columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    stringBuilder.append(" " + columnName + ",");
//                    String columnSize = columns.getString("COLUMN_SIZE");
//                    String datatype = columns.getString("DATA_TYPE");
//                    String isNullable = columns.getString("IS_NULLABLE");
//                    String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
                }
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
                stringBuilder.append(" FROM " + tableName + ";");

                columns.close();
                connection.close();
                return stringBuilder.toString();
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }

    @Override
    public List<String> getTables() throws SQLException {
//        List<String> tablesList = new ArrayList<>();

        tablesList = new ArrayList<>();                                 // update tablesList at every method call
        try (Connection connection = dataSource.getConnection()) {
            java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
            try(ResultSet tables = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"})){

//                while(tables.next()) {
//                    String tableName = tables.getString("TABLE_NAME");
////                    System.out.printf(" === %s \n", tableName);
//                    tablesList.add(tableName);
//                }

                if (tables.next()) {
                    do {
                        String tableName = tables.getString("TABLE_NAME");
                        tablesList.add(tableName);
                    } while (tables.next());
                } else {
                    tablesList = null;
                    System.err.println("Warning DB: no Tables found.");
                }

                tables.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tablesList;
    }
}

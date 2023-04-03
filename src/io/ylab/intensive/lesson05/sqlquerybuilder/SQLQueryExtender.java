package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLQueryExtender implements SQLQueryBuilder {

    private List<String> tablesList;
    static DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String queryForTable(String tableName) throws SQLException {
//        List<String> tablesList = getTables();
        if (tablesList == null) {                                       // if tablesList not initialized
            this.tablesList = getTables();                              // updates tablesList ONCE
        }
        if (tablesList == null || !tablesList.contains(tableName)) {    // if tablesList is empty, or wrong tableName query
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        try (Connection connection = dataSource.getConnection()) {
            java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
            try(ResultSet columns = databaseMetaData.getColumns(null,null, tableName, null)){

                if (columns.next()) {
                    stringBuilder.append("SELECT");
                    do {
                        String columnName = columns.getString("COLUMN_NAME");
                        stringBuilder.append(" " + columnName + ",");
//                    String columnSize = columns.getString("COLUMN_SIZE");
//                    String datatype = columns.getString("DATA_TYPE");
//                    String isNullable = columns.getString("IS_NULLABLE");
//                    String isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
                    } while (columns.next());

                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    stringBuilder.append(" FROM " + tableName + ";");

                    columns.close();
                    connection.close();
                } else {
                    return "(Sorry, table \"" + tableName + "\" is empty.)";
                }
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return stringBuilder.toString();
    }

    @Override
    public List<String> getTables() throws SQLException {
        tablesList = new ArrayList<>();                                 // update tablesList at every method call
        try (Connection connection = dataSource.getConnection()) {
            java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
            try(ResultSet tables = databaseMetaData.getTables(null, null, null, null)){

                if (tables.next()) {
                    do {
                        String tableName = tables.getString("TABLE_NAME");
                        tablesList.add(tableName);
                    }
                    while (tables.next());
                } else {
                    tablesList = null;
                    System.err.println("(ServiceMessage: No Tables found in DB.)");
                }

                tables.close();
                connection.close();
            }
        }
        return tablesList;
    }


    // *************************************************
    // Additional TEST-method: get only User's tables
    public static List<String> getUserTablesOnly() {
        List<String> tablesList = new ArrayList<>();
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
                    }
                    while (tables.next());
                } else {
                    tablesList = null;
                    System.err.println("(ServiceMessage: No Tables found in DB.)");
                }

                tables.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tablesList;
    }

    /*
    @Override
    public List<String> getTables() throws SQLException {
//        List<String> tablesList = new ArrayList<>();

        tablesList = new ArrayList<>();                     // updates at every method call
        try (Connection connection = dataSource.getConnection()) {
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery("select * from orders");
//            ResultSetMetaData rsmd = rs.getMetaData();
//
//            int cols = rsmd.getColumnCount();
//            int rownum = 1;
//
//            while(rs.next()) {
//                System.out.println("Cтpокa : " + rownum++);
//                for (int i = 0; i < cols; i++) {
//                    System.out.println("\tColumnLabel : '" + rsmd.getColumnLabel (i + 1) + "', " +
//                            "\tDisplaySize : " + rsmd.getColumnDisplaySize (i + 1) + ", " +
//                            "\tColumnType  : '" + rsmd.getColumnType (i + 1) + "', " +
//                            rs.getObject(i + 1) + ", ");
//                }
//            }
//            rs.close();



            java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
            try(ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"})){
                while(resultSet.next()) {
                    String tableName = resultSet.getString("TABLE_NAME");
                    System.out.printf(" === %s \n", tableName);
                    tablesList.add(tableName);



//                    ResultSetMetaData resultSetMD = resultSet.getMetaData();
//                    int cols = resultSetMD.getColumnCount();
//                    System.out.println("getColumnCount()=" + cols);
//                    int rownum = 1;
//                    for (int i = 0; i < cols; i++) {
//                        System.out.println("\tColumnLabel : '" + resultSetMD.getColumnLabel (i + 1) + "', " +
//                                "\tDisplaySize : " + resultSetMD.getColumnDisplaySize (i + 1) + ", " +
//                                "\tColumnType  : '" + resultSetMD.getColumnType (i + 1) + "', Object=" +
//                                resultSet.getObject(i + 1) + ", ");
//                    }
                }
                resultSet.close();
            }

////            OK
//            java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
//            try(ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"})){
//                while(resultSet.next()) {
//                    String tableName = resultSet.getString("TABLE_NAME");
//                    String remarks = resultSet.getString("REMARKS");
//                    System.out.printf(" === %s, %s \n", tableName, remarks);
//
//
//
//                    ResultSetMetaData resultSetMD = resultSet.getMetaData();
//                    int cols = resultSetMD.getColumnCount();
//                    int rownum = 1;
//                    for (int i = 0; i < cols; i++) {
//                        System.out.println("\tColumnLabel : '" + resultSetMD.getColumnLabel (i + 1) + "', " +
//                                "\tDisplaySize : " + resultSetMD.getColumnDisplaySize (i + 1) + ", " +
//                                "\tColumnType  : '" + resultSetMD.getColumnType (i + 1) + "', Object=" +
//                                resultSet.getObject(i + 1) + ", ");
//                    }
//                }
//                resultSet.close();
//            }


////            OK
//            java.sql.DatabaseMetaData databaseMetaData = connection.getMetaData();
//            try(ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"})){
//                while(resultSet.next()) {
//                    String tableName = resultSet.getString("TABLE_NAME");
//                    String remarks = resultSet.getString("REMARKS");
//                    System.out.printf(" === %s, %s \n", tableName, remarks);
//                }
//            }


//            // Набор данных поддерживаемых типов
//            ResultSet rs = databaseMetaData.getTypeInfo();
////            ResultSet rset = databaseMetaData.getColumns (null, "schemaName", "tableName", "%");
//            System.out.println("Набор примитивных типов, поддерживаемых данным типом приложения\n");
//            while (rs.next()) {
//                System.out.println(rs.getString(1));
//            }
//            rs.close();


//            ResultSet resultSet = databaseMetaData.getTables("postgres", "information_schema", "*", null);
//            ResultSet resultSet = databaseMetaData.getSuperTables("*", "*", "*");
//            ResultSet resultSet = databaseMetaData.getTables("postgres", null, null, new String[]{"FOREIGN TABLE"});
//            ResultSet resultSet = databaseMetaData.getTables("postgres", null, null, new String[]{"%"});
//            ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"Shop"});
//            ResultSet resultSet = databaseMetaData.getCatalogs();   // postgres
//            ResultSet resultSet = databaseMetaData.getSchemas();   // information_schema, null
//            ResultSet resultSet = databaseMetaData.getTableTypes();   // FOREIGN TABLE
//            System.out.println(databaseMetaData.getCatalogTerm());  // database


//            if (resultSet.next()) {
//                tablesList.add(resultSet.getString(1));
//                System.out.println(" === " + resultSet.getString(1));
////                System.out.println(resultSet.getString(1) + " / " + resultSet.getString(2) + " : " + resultSet.getString(3));
////                Person person = new Person();
////                person.setId(personId);
////                person.setName(resultSet.getString(2));
////                person.setLastName(resultSet.getString(3));
////                person.setMiddleName(resultSet.getString(4));
////                return person;
//            } else {
//                System.out.println("Check null=" + null + " : records not found");
//            }

            return tablesList;
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }
     */
}

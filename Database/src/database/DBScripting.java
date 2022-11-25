package database;

import java.sql.*;

public class DBScripting {
    Connection connection = DriverManager.getConnection("jdbc:mysql://projpara.cxk0cha7pnxm.us-east-2.rds.amazonaws.com:3306/ProjectParadise", "admin", "rootdb3432");

    Statement statement = connection.createStatement();
    ResultSet rs;
    int columns;
    int rows;

    public DBScripting() throws SQLException {
    }

    public int getNumRows(String table) throws SQLException {
        rs = statement.executeQuery("select count(*) from " + table);
        rs.next();
        rows = rs.getInt(1);
        return rows;
    }

    public int getNumColumns(String table) throws SQLException {
        rs = statement.executeQuery("select * from " + table);
        rs.next();
        ResultSetMetaData rsMetaData = rs.getMetaData();
        columns = rsMetaData.getColumnCount();
        return columns;
    }


    public String[] getColumns(String table) throws SQLException {
        rs = statement.executeQuery("select * from " + table);
        rs.next();
        ResultSetMetaData rsMetaData = rs.getMetaData();
        columns = rsMetaData.getColumnCount();

        String[] columnNames = new String[columns];
        for(int i = 1; i <= columns; i++) {
            columnNames[i-1] = rsMetaData.getColumnName(i); //"id", "geography", "name"
        }

        return columnNames;
    }

    public Object[][] getRowData(String table) throws SQLException {
        rs = statement.executeQuery("select * from " + table);
        rs.next();

        String[][] data = new String[rows][columns];

        for(int i = 0; i < rows; i++) {
            for (int r = 0; r<columns; r++) {
                data[i][r] = rs.getString(r+1);
            }
            rs.next();
        }

        return data;
    }
}

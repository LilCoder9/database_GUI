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

    public void addRow(Object[] adder, String[] columns, String table) throws SQLException {
        String insert = ("INSERT INTO "+ table + "(");
        String values = "VALUES(";

        for (int i = 0; i < columns.length; i++) {
            insert += columns[i];
            if (i != columns.length-1)
                insert += ", ";
            else
                insert += ") ";
        }

        for (int i = 0; i < columns.length; i++) {
            values += "?";
            if (i != columns.length-1)
                values += ", ";
            else
                values += ")";
        }

        PreparedStatement pStmt = connection.prepareStatement(insert + values);

        for (int i = 0; i < adder.length; i++) {
            pStmt.setObject(i+1, adder[i]);
        }

        pStmt.executeUpdate();
    }

    public void editRow(Object[] edit, String[] columns, String table) throws SQLException {
        //UPDATE table SET values WHERE pk
        String query = "UPDATE " + table + " SET ";
        for (int i = 1; i < edit.length; i++) {
            query += columns[i] + "=\"" + edit[i] + "\"";
            if (i != columns.length-1)
                query += ", ";
        }

        query += " WHERE " + columns[0] + "=" + edit[0];
        statement.executeUpdate(query);
    }

    public void deleteRow(Object deleter, String[] columns, String table) throws SQLException {
        String query = "DELETE FROM " + table + " WHERE " + columns[0] + "=" + deleter;
        statement.executeUpdate(query);
    }
}

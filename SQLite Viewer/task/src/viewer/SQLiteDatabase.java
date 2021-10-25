package viewer;

import org.sqlite.SQLiteDataSource;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabase {

    private String jdbcURL;
    private SQLiteDataSource dataSource;
    private Connection con;
    private JFrame parentFrame;

    SQLiteDatabase(String path, JFrame parentFrame) {
        jdbcURL = "jdbc:sqlite:" + path;
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(jdbcURL);
        this.parentFrame = parentFrame;
        try {
            con = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] getTables() {
        String query = "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs == null) {
                return null;
            } else {
                List<String> l = new ArrayList();
                while (rs.next()) {
                    l.add(rs.getString(1));
                }
                ;
                return (String[]) l.toArray(new String[0]);
            }
        } catch (SQLException exception) {

            JOptionPane.showMessageDialog(parentFrame, exception.getMessage(),
                    "SQL Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    ;

    public Object[][] executeQuery(String query) {
        // Execute a query and returns an array of objects with column names in the first row
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs == null) {
                return null;
            } else {
                int cols = rs.getMetaData().getColumnCount();
                // The list data will hold column headers as well as table data
                List<Object> data = new ArrayList<>();

                // Add column names to the list
                for (int columnIndex = 1; columnIndex <= cols; columnIndex++) {
                    data.add(rs.getMetaData().getColumnName(columnIndex));
                }
                // The rows variable is used for counting the rows in the results array
                // One row of data that consists of the column names has been added
                int rows = 1;
                // Add the column values to the data list
                while (rs.next()) {
                    rows++;
                    for (int columnIndex = 1; columnIndex <= cols; columnIndex++) {
                        data.add(rs.getObject(columnIndex));
                    }
                }
                // Return the list Converted to 2d array
                return listTo2dArray(data, rows, cols);
            }

        } catch (SQLException exception) {

            JOptionPane.showMessageDialog(parentFrame, exception.getMessage(),
                    "SQL Error", JOptionPane.ERROR_MESSAGE);

            return null;
        }
    }

    private static Object[][] listTo2dArray(List l, int rows, int cols) {
        // The method converts a list of Objects to a 2d array of objects
        // It is assumed that the elements fit into the number of rows and
        // columns specified.  If there are more elements they will be ignored
        // If there are fewer elements, there will be an error
        Object[][] result = new Object[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                result[row][col] = l.get((cols * row) + col);
            }
        }
        return result;
    }

    public boolean connected() {
        boolean valid;
        try {
            return con.isValid(10);
        } catch (SQLException exception) {
            return false;
        }


    }

    public String checkStatement(String statement) {
        try {
            PreparedStatement s = con.prepareStatement(statement);
            return null;
        } catch (SQLException e) {
            return e.getMessage();
        }
        }

}

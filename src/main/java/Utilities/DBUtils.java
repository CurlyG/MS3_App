package Utilities;

import java.sql.*;
import java.util.List;

public class DBUtils {
    public void createNewTable(String url) {

        String sql = "CREATE TABLE IF NOT EXISTS challenge (\n"
                + " A text,\n"
                + "	B text,\n"
                + "	C text,\n"
                + "	D text,\n"
                + "	E text,\n"
                + "	F text,\n"
                + "	G text,\n"
                + " H text,\n"
                + "	I text,\n"
                + "	J text\n"
                + ");";

        try (Connection conn = getConnection(url); //If DB doesn't exists this will create it
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);// create a new table defined in the sql above
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String url, List<List<String>> data) {
        try (Connection conn = getConnection(url)) {
            String sql = "INSERT INTO challenge(A,B,C,D,E,F,G,H,I,J) VALUES(?,?,?,?,?,?,?,?,?,?)";
            for (List<String> line:  data) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) { //PreparedStatement is used, since I deal with parametrized sql query
                    pstmt.setString(1, line.get(0));
                    pstmt.setString(2, line.get(1));
                    pstmt.setString(3, line.get(2));
                    pstmt.setString(4, line.get(3));
                    pstmt.setString(5, line.get(4));
                    pstmt.setString(6, line.get(5));
                    pstmt.setString(7, line.get(6));
                    pstmt.setString(8, line.get(7));
                    pstmt.setString(9, line.get(8));
                    pstmt.setString(10, line.get(9));
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection(String url) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
}

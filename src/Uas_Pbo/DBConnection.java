package Uas_Pbo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/db_parkir";
        String user = "root";
        String pass = "";
        return DriverManager.getConnection(url, user, pass);
    }
}
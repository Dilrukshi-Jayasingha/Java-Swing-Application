
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author chari
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ ship_shape"; // Database URL
    private static final String USER = "root";  // Your MySQL username
    private static final String PASSWORD = "";  // Your MySQL password

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Basically keeps the information of the database stored in one place. 
public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/se370team3?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String USER = "admin";
    private static final String PASSWORD = "team3Pass!";

    private DatabaseConnection() {
    }

    //This will open a new MySQL connection whenever a manager/staff has to run query's. 
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
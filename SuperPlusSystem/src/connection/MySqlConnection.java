package connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlConnection {
    private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                Properties props = new Properties();
                FileInputStream fis = new FileInputStream("src/config.properties");
                props.load(fis);

                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                conn = DriverManager.getConnection(url, user, password);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}
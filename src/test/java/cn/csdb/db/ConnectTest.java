package cn.csdb.db;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by pirate on 2018/8/8.
 */
public class ConnectTest {

    @Test
    public void validateConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
//            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@159.226.12.130:1521:DATAFABRIC", "DATAFABRIC", "DATAFABRIC");
            if(conn == null)
                return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

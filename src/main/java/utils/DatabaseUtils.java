package utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtils {
    private static ComboPooledDataSource ds = new ComboPooledDataSource();

    public static Connection getConnection(){
        try{
            return ds.getConnection();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}

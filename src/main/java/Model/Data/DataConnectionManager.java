package Model.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DataConnectionManager {

    private static DataConnectionManager instancia;
    private static String dbUrl=null;
    private static String dbUser=null;
    private static String dbPassword=null;
    private final static Logger logger = LoggerFactory.getLogger(DataConnectionManager.class);
    private Connection conn;

    public static DataConnectionManager getInstancia(){
        if(instancia==null){
            DataConnectionManager.loadDbProperties();
            instancia=new DataConnectionManager();
        }
        return instancia;
    }

    private static void loadDbProperties(){
        String appPropertyName = "app.properties";
        Properties prop = new Properties();
        try (InputStream inputStream = DataConnectionManager.class.getClassLoader().getResourceAsStream(appPropertyName)){
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + appPropertyName + "' not found in the classpath");
            }
            dbUrl = prop.getProperty("db.url");
            dbUser = prop.getProperty("db.username");
            dbPassword = prop.getProperty("db.password");
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    public Connection getConn(){
        try {
            if(conn==null || !conn.isValid(3)){
                conn=DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return conn;
    }
    public void CloseConn(){
        try {
            if(conn!=null && !conn.isClosed()){
                conn.close();
                conn=null;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

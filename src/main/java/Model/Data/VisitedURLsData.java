package Model.Data;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class VisitedURLsData {

    private final static Logger logger = LoggerFactory.getLogger(VisitedURLsData.class);

    public Set<String> getAll() throws SQLException{
        Set<String> visitedURLs = new HashSet<>();
        Statement statement = null;
        ResultSet rs = null;

        try {
            Connection conn = DataConnectionManager.getInstancia().getConn();
            statement = conn.createStatement();
            rs = statement.executeQuery("SELECT url FROM visitedURLs");

            while (rs.next()) {
                visitedURLs.add(rs.getString("url"));
            }
        } catch (SQLException Ex) {

            logger.error(Ex.getMessage(), Ex);

        } finally {

            if (rs != null) {
                rs.close();
            }
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
            DataConnectionManager.getInstancia().CloseConn();

        }
        return visitedURLs;
    }

    public void addAll(Set<String> URLs) throws SQLException{
        Connection conn = null;
        PreparedStatement statement = null;

        try {

            conn = DataConnectionManager.getInstancia().getConn();
            conn.setAutoCommit(false);
            statement = conn.prepareStatement("INSERT INTO visitedURLs(url) " +
                                              "VALUES(?)");

            for (String url : URLs) {
                statement.setString(1,url);
                statement.addBatch();
            }

            statement.executeBatch();
            conn.commit();

        } catch (SQLException Ex) {

            logger.error(Ex.getMessage(), Ex);
            conn.rollback();

        }finally {
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
            if(conn !=null){
                DataConnectionManager.getInstancia().CloseConn();
            }
        }
    }
}

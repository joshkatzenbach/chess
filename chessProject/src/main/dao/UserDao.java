package dao;

import dataAccess.DataAccessException;
import models.User;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDao {

    private static Connection conn;
    public UserDao() {}

    public boolean addUser(User user) throws DataAccessException  {
        try {
            var queryStatement = conn.prepareStatement("SELECT username FROM userTable WHERE username = ?");
            queryStatement.setString(1, user.getUsername());

            var rs = queryStatement.executeQuery();
            if (rs.next()) {
                return false;
            }

            try {
                var insertStatement = conn.prepareStatement("INSERT INTO userTable (username, password, email) VALUES(?,?,?)");
                insertStatement.setString(1, user.getUsername());
                insertStatement.setString(2, user.getPassword());
                insertStatement.setString(3, user.getEmail());
                insertStatement.executeUpdate();
                return true;
            }
            catch (SQLException ex) {
                throw new DataAccessException("Could not insert record");
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("Couldn't query userTable correctly");
        }

    };

    public boolean authenticate(String username, String password) throws DataAccessException{
        try {
            var queryStatement = conn.prepareStatement("SELECT username FROM userTable WHERE username = ? AND password = ?");
            queryStatement.setString(1, username);
            queryStatement.setString(2, password);
            var rs = queryStatement.executeQuery();
            return rs.next();
        }
        catch (SQLException ex) {
            System.out.println("Authenticate Exception");
            throw new DataAccessException("Error: Could not access user table correctly");
        }

    }

    public void clear() throws DataAccessException {
        try {
            var clearStatement = conn.prepareStatement("DELETE FROM userTable");
            clearStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("Could not clear userTable from database");
        }
    };

    public static void setConn(Connection conn) {
        UserDao.conn = conn;
    }
}

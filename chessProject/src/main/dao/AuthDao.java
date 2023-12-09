package dao;

import models.*;
import dataAccess.*;

import java.util.ArrayList;
import java.sql.*;

public class AuthDao {
    private static ArrayList<AuthToken> tokens = new ArrayList<AuthToken>();

    private static Connection conn;

    public AuthToken generateToken(String username) throws DataAccessException {
        try {
            String randomToken = java.util.UUID.randomUUID().toString();
            var insertStatement = conn.prepareStatement("INSERT INTO authTable (username, authToken) VALUES(?, ?)");
            insertStatement.setString(1, username);
            insertStatement.setString(2, randomToken);
            insertStatement.executeUpdate();

            AuthToken token = new AuthToken();
            token.setAuthToken(randomToken);
            token.setUsername(username);
            return token;
        }
        catch (SQLException ex) {
            System.out.println("Generate Token Exception");
            throw new DataAccessException("Error: Couldn't insert authToken into authTable");
        }
    };


    public void removeToken(String authToken) throws DataAccessException {
        try {
            var deleteStatement = conn.prepareStatement("DELETE FROM authTable WHERE authToken = ?");
            deleteStatement.setString(1, authToken);
            deleteStatement.executeUpdate();
        }
        catch(SQLException ex) {
            throw new DataAccessException("Error: Could not access authTable");
        }
    };


    public boolean authorizeToken(String authToken) throws DataAccessException{
        try {
            var queryStatement = conn.prepareStatement("SELECT authToken FROM authTable WHERE authToken = ?");
            queryStatement.setString(1, authToken);
            var rs = queryStatement.executeQuery();

            return rs.next();
        }
        catch (SQLException ex) {
            throw new DataAccessException("Error: Could not access authTable");
        }
    }

    public String getUsernameFromToken(String authToken) throws DataAccessException {
        try {
            var queryStatement = conn.prepareStatement("SELECT username FROM authTable WHERE authToken=?");
            queryStatement.setString(1, authToken);
            var rs = queryStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            } else {
                throw new DataAccessException("Error: AuthToken Doesn't exist in authTable");
            }
        }
        catch (SQLException ex) {
            System.out.println("Get Username from Token Exception");
            throw new DataAccessException("Error: Could not access authTable");
        }
    }

    public void clear() throws DataAccessException {
        try {
            var clearStatement = conn.prepareStatement("DELETE FROM authTable");
            clearStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("Could not clear authTable from database");
        }
    }


    public static void setConn(Connection conn) {
        AuthDao.conn = conn;
    }
}

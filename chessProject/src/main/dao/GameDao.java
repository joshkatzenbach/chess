package dao;

import chess.ChessGame;
import chessCode.AGame;
import com.google.gson.Gson;
import models.Game;
import dataAccess.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameDao {

    private static Connection conn;

    public Game getGameDetails(int gameID) throws Exception {
        var queryStatement = conn.prepareStatement("SELECT * FROM gameTable WHERE gameID=?");
        queryStatement.setInt(1, gameID);
        var rs = queryStatement.executeQuery();
        String gameString;

        if (rs.next()) {
            gameString = rs.getString("game");
        }
        else {
            throw new DataAccessException("ERROR: GameID was not in table");
        }

        return new Gson().fromJson(gameString, Game.class);
    }
    public int createGame(String gameName) throws DataAccessException {
        int idCounter = 0;

        try {
            var queryStatement = conn.prepareStatement("SELECT MAX(gameID) as max_value FROM gameTable");

            var rs = queryStatement.executeQuery();
            if (rs.next()) {
                idCounter = rs.getInt("max_value");
            }

            var insertStatement = conn.prepareStatement("INSERT INTO gameTable (gameName, gameID, game) VALUES(?,?,?)");
            insertStatement.setString(1, gameName);
            insertStatement.setInt(2, ++idCounter);
            insertStatement.setString(3, (String) new Gson().toJson(new AGame()));
            insertStatement.executeUpdate();
            return idCounter;
        }
        catch (SQLException ex) {
            System.out.println("Create Game Exception");
            throw new DataAccessException("Error: Could not connect to gameTable");
        }
    }

    public ArrayList<Game> returnAll() throws DataAccessException {
        try {
            ArrayList<Game> games = new ArrayList<Game>();
            var queryStatement = conn.prepareStatement("SELECT * FROM gameTable");
            var rs = queryStatement.executeQuery();
            while (rs.next()) {
                Game game = new Game();
                game.setWhiteUsername(rs.getString("whiteUsername"));
                game.setBlackUsername(rs.getString("blackUsername"));
                game.setGameID(rs.getInt("gameID"));
                game.setGameName(rs.getString("gameName"));
                games.add(game);
            }
            return games;
        }
        catch (SQLException ex) {
            throw new DataAccessException("Error: Could not access gameTable");
        }
    }

    public void clear() throws DataAccessException {
        try {
            var clearStatement = conn.prepareStatement("DELETE FROM gameTable");
            clearStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("Could not clear gameTable from database");
        }
    }

    public int joinGame(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        try {
            var queryStatement = conn.prepareStatement("SELECT * FROM gameTable WHERE gameID=?");
            queryStatement.setInt(1, gameID);
            var rs = queryStatement.executeQuery();
            if (!rs.next()) {
                return 2;
            } else if (color == ChessGame.TeamColor.WHITE) {
                if (rs.getString("whiteUsername") == null) {
                    var insertStatement = conn.prepareStatement("UPDATE gameTable SET whiteUsername=? WHERE gameID=?");
                    insertStatement.setString(1, username);
                    insertStatement.setInt(2, gameID);
                    insertStatement.executeUpdate();
                    return 1;
                } else {
                    return 0;
                }
            } else if (color == ChessGame.TeamColor.BLACK) {
                if (rs.getString("blackUsername") == null) {
                    var insertStatement = conn.prepareStatement("UPDATE gameTable SET blackUsername=? WHERE gameID=?");
                    insertStatement.setString(1, username);
                    insertStatement.setInt(2, gameID);
                    insertStatement.executeUpdate();
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return 1;
            }
        }
        catch (SQLException ex) {
            System.out.println("Join Game Exception");
            throw new DataAccessException("Error Could not access gameTable");
        }

    }

    public static void setConn(Connection conn) {
        GameDao.conn = conn;
    }
}

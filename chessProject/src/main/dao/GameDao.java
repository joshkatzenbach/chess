package dao;

import chess.ChessGame;
import chess.ChessPiece;
import chessCode.ChessBoardLayout;
import chessCode.ChessPieceAdapter;
import chessCode.GameInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Game;
import dataAccess.*;
import server.GameMembers;
import server.WSServer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class GameDao {

    private static Connection conn;

    public void updateGame(GameInstance game, int gameID) throws Exception {
        var updateStatement = conn.prepareStatement("UPDATE gameTable SET game=? WHERE gameID=?");
        GsonBuilder gsonBuilder = new GsonBuilder();

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
        Gson gson = builder.create();

        updateStatement.setString(1, gson.toJson(game));
        updateStatement.setInt(2, gameID);
        updateStatement.executeUpdate();
    }

    public boolean verifyColor(ChessGame.TeamColor color, int gameID, String username) throws Exception{
        java.sql.PreparedStatement queryStatement;
        queryStatement = conn.prepareStatement("SELECT * FROM gameTable WHERE gameID=?");
        queryStatement.setInt(1, gameID);
        var rs = queryStatement.executeQuery();
        if (rs.next()) {
            if (color == WHITE) {
                System.out.println(rs.getString("whiteUsername"));
                return Objects.equals(rs.getString("whiteUsername"), username);
            }
            else if (color == BLACK) {
                return Objects.equals(rs.getString("blackUsername"), username);
            }
            else {
                throw new Exception("No Color included in colorTaken function");
            }
        }
        else {
            throw new Exception("GameID not located in database");
        }
    }

    public void removeUser(ChessGame.TeamColor color, int gameID) throws Exception {
        try {
            java.sql.PreparedStatement updateStatement;
            if (color == WHITE) {
                updateStatement = conn.prepareStatement("UPDATE gameTable SET whiteUsername=NULL WHERE gameID=?");
            } else {
                updateStatement = conn.prepareStatement("UPDATE gameTable SET blackUsername=NULL WHERE gameID=?");
            }

            updateStatement.setInt(1, gameID);
            updateStatement.executeUpdate();
        }
        catch (Exception ex) {
            throw new DataAccessException("Could not access the database or gameID was not found");
        }
    }

    public GameInstance getGameInstance(int gameID) throws Exception {
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

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
        Gson gson = builder.create();


        return gson.fromJson(gameString, GameInstance.class);
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

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
            Gson gson = builder.create();

            GameInstance gameToAdd = new GameInstance();
            ChessBoardLayout boardToAdd = new ChessBoardLayout();
            boardToAdd.resetBoard();
            gameToAdd.setBoard(boardToAdd);
            String gameString = gson.toJson(gameToAdd);

            insertStatement.setString(3, gameString);
            insertStatement.executeUpdate();

            WSServer.gameList.add(new GameMembers(idCounter));
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
            } else if (color == WHITE) {
                if (rs.getString("whiteUsername") == null) {
                    var insertStatement = conn.prepareStatement("UPDATE gameTable SET whiteUsername=? WHERE gameID=?");
                    insertStatement.setString(1, username);
                    insertStatement.setInt(2, gameID);
                    insertStatement.executeUpdate();
                    return 1;
                } else {
                    return 0;
                }
            } else if (color == BLACK) {
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

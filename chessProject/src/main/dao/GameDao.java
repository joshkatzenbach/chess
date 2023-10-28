package dao;

import chess.ChessGame;
import chessCode.AGame;
import models.Game;

import dataAccess.*;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class reads and writes to the Games database
 */
public class GameDao {
    /**
     * This will store all games until a formal database is made.
     */
    private static ArrayList<Game> games = new ArrayList<Game>();
    private static int idCounter = 0;



    public int createGame(String gameName) throws DataAccessException {
        for (Game game : games) {
            if (Objects.equals(game.getGameName(), gameName)) {
                throw new DataAccessException("Game Name already exists");
            }
        }

        Game gameToAdd = new Game();
        gameToAdd.setGameName(gameName);
        gameToAdd.setGameID(++idCounter);
        games.add(gameToAdd);
        return idCounter;
    }

    public ArrayList<Game> returnAll() {
        return games;
    }

    public void clear() throws DataAccessException {
        games.clear();

        if (games.size() != 0) {
            throw new DataAccessException("Games not deleted");
        }
    };

    public boolean joinGame(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        for (Game game : games) {
            if (game.getGameID() == gameID) {
                if (color == ChessGame.TeamColor.WHITE) {
                    if (game.getWhiteUsername() == null) {
                        game.setWhiteUsername(username);
                        return true;
                    } else {
                        return false;
                    }
                }
                else if (color == ChessGame.TeamColor.BLACK) {
                    if (game.getBlackUsername() == null) {
                        game.setBlackUsername(username);
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                else if (color == null) {
                    return true;
                }
            }
        }
        throw new DataAccessException("Error: Bad Request");
    };

    public static ArrayList<Game> getGames() {
        return games;
    }
}

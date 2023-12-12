package models;

import chessCode.*;

/**
 * Class Contains all important information about a game between 2 players
 */
public class Game {
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private GameInstance game;

    public Game(String gameName) {
        this.gameName = gameName;
        game = new GameInstance();
    }
    public Game() {};
    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public GameInstance getGame() {
        return game;
    }

    public void setGame(GameInstance game) {
        this.game = game;
    }
}

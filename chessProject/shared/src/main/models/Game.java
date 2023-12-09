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
    private AGame game;

    public Game(String gameName) {
        this.gameName = gameName;
        game = new AGame();
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

    public AGame getGame() {
        return game;
    }

    public void setGame(AGame game) {
        this.game = game;
    }
}

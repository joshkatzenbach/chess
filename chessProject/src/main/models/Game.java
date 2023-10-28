package models;

import chessCode.*;

/**
 * Class Contains all important information about a game between 2 players
 */
public class Game {
    /**
     * Unique identifying number for the game
     */
    private int gameID;
    /**
     * Username of the player with white pieces
     */
    private String whiteUsername;
    /**
     * Username of the player with black pieces
     */
    private String blackUsername;
    /**
     * Nickname for the specific game
     */
    private String gameName;
    /**
     * Game object that contains board and mechanics for the game
     */
    private AGame game;

    /**
     * Default constructor initializes the Game
     */
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

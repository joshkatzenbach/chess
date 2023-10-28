package results;

import models.Game;

import java.util.ArrayList;

/**
 * Contains information from List Games Response
 */
public class ListGamesResult {
    /**
     * Array of games from server
     */
    private ArrayList<Game> games;
    /**
     * Response from server
     */
    private String message;

    private transient int errorCode;

    public ListGamesResult() {};

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}

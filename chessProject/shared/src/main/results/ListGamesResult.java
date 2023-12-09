package results;

import models.Game;

import java.util.ArrayList;

public class ListGamesResult {
    private ArrayList<Game> games;
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

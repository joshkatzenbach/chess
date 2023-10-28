package requests;

import chess.ChessGame.TeamColor;

/**
 * Class contains information for join game request
 */
public class JoinGameRequest {
    /**
     * Desired color of the user
     */
    private TeamColor playerColor;
    /**
     * Game ID of game to be joined
     */
    private int gameID;

    /**
     * Constructor of Game request to be inserted as a player
     * @param gameId Game ID of game to join
     * @param playerColor Desired color to play
     */
    public JoinGameRequest(int gameId,TeamColor playerColor) {
        this.playerColor = playerColor;
        this.gameID = gameId;
    }

    public JoinGameRequest() {};

    /**
     * Constructor Game request to join as an observer
     * @param gameID Game ID of game to watch
     */
    public JoinGameRequest(int gameID) {
        this.gameID = gameID;
    }

    public TeamColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}

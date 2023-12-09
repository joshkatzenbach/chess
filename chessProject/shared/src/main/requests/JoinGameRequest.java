package requests;

import chess.ChessGame.TeamColor;

public class JoinGameRequest {
    private TeamColor playerColor;
    private int gameID;

    public JoinGameRequest(int gameId,TeamColor playerColor) {
        this.playerColor = playerColor;
        this.gameID = gameId;
    }

    public JoinGameRequest() {};

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

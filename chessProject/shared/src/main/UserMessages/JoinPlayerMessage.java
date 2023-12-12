package UserMessages;
import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class JoinPlayerMessage extends UserGameCommand {
    private final int gameID;
    private final ChessGame.TeamColor playerColor;
    public JoinPlayerMessage(String authToken, int gameID, ChessGame.TeamColor color) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = color;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getColor() {
        return playerColor;
    }
}

package UserMessages;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class LeaveGameMessage extends UserGameCommand {

    private final int gameID;


    public LeaveGameMessage(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.LEAVE;
    }

    public int getGameID() {
        return gameID;
    }
}

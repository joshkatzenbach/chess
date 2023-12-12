package UserMessages;

import webSocketMessages.userCommands.UserGameCommand;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.RESIGN;

public class ResignMessage extends UserGameCommand {
    private final int gameID;

    public ResignMessage(String authToken, int gameID) {
        super(authToken);
        this.commandType = RESIGN;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}

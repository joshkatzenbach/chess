package UserMessages;

import webSocketMessages.userCommands.UserGameCommand;

public class JoinObserverMessage extends UserGameCommand {

    private int gameID;

    public JoinObserverMessage(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}

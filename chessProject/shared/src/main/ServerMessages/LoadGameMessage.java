package ServerMessages;

import chessCode.GameInstance;
import webSocketMessages.serverMessages.ServerMessage;

public class LoadGameMessage extends ServerMessage {

    GameInstance game;
    public LoadGameMessage(GameInstance game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public GameInstance getGame() {
        return game;
    }
}

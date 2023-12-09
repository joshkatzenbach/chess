package ServerMessages;

import models.Game;
import webSocketMessages.serverMessages.ServerMessage;

public class LoadGame extends ServerMessage {

    Game game;
    public LoadGame(Game game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}

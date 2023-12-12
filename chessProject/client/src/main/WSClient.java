import ServerMessages.ErrorMessage;
import ServerMessages.LoadGameMessage;
import ServerMessages.Notification;
import chess.ChessPiece;
import chessCode.ChessPieceAdapter;
import chessCode.GameInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;


public class WSClient extends Endpoint {

    Session session;

    private final Object lock = new Object();


    public WSClient() throws Exception {
        String url = "ws://" + ClientMain.getHost() + ":" + ClientMain.getPort() + "/connect";
        URI uri = new URI(url);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                synchronized (lock) {
                    ServerMessage messageObject = new Gson().fromJson(message, ServerMessage.class);
                    if (messageObject.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        loadGame(message);
                    } else if (messageObject.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                        handleError(message);
                    } else if (messageObject.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                        handleNotification(message);
                    } else {
                        System.out.print(SET_TEXT_COLOR_RED);
                        System.out.println("Message from server not recognized");
                    }
                }
            }
        });
    }



    public void send(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
    }

    public void onOpen(Session session, EndpointConfig config) {
    }

    private void loadGame(String message) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
            Gson gson = builder.create();
            LoadGameMessage loadGameMessage = gson.fromJson(message, LoadGameMessage.class);
            GameInstance updatedGame = loadGameMessage.getGame();
            Gameplay.updateGame(updatedGame);
            Gameplay.outputPrompt();
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private void handleError(String message) {
        ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
        System.out.print(SET_TEXT_COLOR_RED);
        System.out.println(errorMessage.getErrorMessage());
        Gameplay.outputPrompt();
    }

    private void handleNotification(String message) {
        Notification notification = new Gson().fromJson(message, Notification.class);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println(notification.getMessage());
        Gameplay.outputPrompt();
    }


}

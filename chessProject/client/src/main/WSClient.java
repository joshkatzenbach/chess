import ServerMessages.ErrorMessage;
import ServerMessages.LoadGame;
import ServerMessages.Notification;
import com.google.gson.Gson;
import models.Game;
import webSocketMessages.serverMessages.ServerMessage;

import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;


public class WSClient extends Endpoint {

    Session session;


    public WSClient() throws Exception {
        String url = "ws://" + ClientMain.getHost() + ":" + ClientMain.getPort() + "/connect";
        System.out.println(url);
        URI uri = new URI(url);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        session = container.connectToServer(this, uri);

        session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                ServerMessage messageObject = new Gson().fromJson(message, ServerMessage.class);
                if (messageObject.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                    loadGame(message);
                }
                else if (messageObject.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                    handleError(message);
                }
                else if (messageObject.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                    handleNotification(message);
                }
                else {
                    System.out.print(SET_TEXT_COLOR_RED);
                    System.out.println("Message from server not recognized");

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
            LoadGame loadGameMessage = new Gson().fromJson(message, LoadGame.class);
            Game updatedGame = loadGameMessage.getGame();
            Gameplay.updateBoard(updatedGame.getGame().getBoard());
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    private void handleError(String message) {
        ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
        System.out.print(SET_TEXT_COLOR_RED);
        System.out.println(errorMessage.getMessage());
    }

    private void handleNotification(String message) {
        Notification notification = new Gson().fromJson(message, Notification.class);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println(notification.getMessage());
    }


}

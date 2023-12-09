package server;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GameMembers {
    private int gameID;
    private ArrayList<String> usernames;
    private ArrayList<Session> sessions;

    public GameMembers(int gameID) {
        this.gameID = gameID;
    }
    void addNewMember(Session session, String username) {
        usernames.add(username);
        sessions.add(session);
    }

    void removeMember(String username) {
        for(int index = 0; index < usernames.size(); index++) {
            if (Objects.equals(usernames.get(index), username)) {
                usernames.remove(index);
                sessions.remove(index);
                break;
            }
        }
    }

    void sendToAll(String message) throws IOException {
        for (Session session : sessions) {
            session.getRemote().sendString(message);
        }
    }
    public int getGameID() {
        return gameID;
    }
}

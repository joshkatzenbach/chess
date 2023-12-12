package server;

import chess.ChessGame;
import dao.GameDao;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;


public class GameMembers {
    private final int gameID;
    private final ArrayList<Session> observerSessions;
    private Session whiteSession;
    private Session blackSession;
    private String whiteUsername;
    private String blackUsername;



    public GameMembers(int gameID) {
        this.gameID = gameID;
        observerSessions = new ArrayList<Session>();
    }
    void addNewMember(Session session, ChessGame.TeamColor color, String username) throws Exception{
        if (color == null) {
            observerSessions.add(session);
        }
        else if (color == WHITE) {
            if ((whiteSession == null) && (new GameDao().verifyColor(WHITE, gameID, username))){
                whiteSession = session;
                whiteUsername = username;
            }
            else {
                throw new Exception("White already has a player");
            }
        }
        else if (color == BLACK) {
            if ((blackSession == null) && (new GameDao().verifyColor(BLACK, gameID, username)))  {
                blackSession = session;
                blackUsername = username;
            }
            else {
                throw new Exception("Black Already has a player");
            }
        }
    }

    public ChessGame.TeamColor getColor(Session session) throws Exception{
        if (whiteSession == session) {
            return WHITE;
        }
        else if (blackSession == session) {
            return BLACK;
        }

        for (Session observerSession : observerSessions) {
            if (session == observerSession) {
                return null;
            }
        }

        throw new Exception("Could not find player or observer in selected game");
    }

    public void removeMember(Session session) {
        if (whiteSession == session) {
            whiteSession = null;
            whiteUsername = null;
            return;
        } else if (blackSession == session) {
            blackSession = null;
            blackUsername = null;
            return;
        }

        for (Session observerSession : observerSessions) {
            if (observerSession == session) {
                observerSessions.remove(observerSession);
                return;
            }
        }
    }

    void sendToAll(String message, Session noSendSession) throws IOException {
        for (Session session : observerSessions) {
            if (session != noSendSession) {
                session.getRemote().sendString(message);
            }
        }

        if ((whiteSession != null) && (whiteSession != noSendSession)) {
            whiteSession.getRemote().sendString(message);
        }

        if ((blackSession != null) && (blackSession != noSendSession)){
            blackSession.getRemote().sendString(message);
        }
    }
    public int getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }
}

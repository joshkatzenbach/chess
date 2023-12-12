package server;

import ServerMessages.ErrorMessage;
import ServerMessages.LoadGameMessage;
import ServerMessages.Notification;
import UserMessages.*;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chessCode.ChessPositionAdapter;
import chessCode.GameInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.UserGameCommand;
import dao.*;
import java.util.ArrayList;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static webSocketMessages.userCommands.UserGameCommand.CommandType.*;

public class WSServer {

    public static ArrayList<GameMembers> gameList;

    public WSServer() {
        gameList = new ArrayList<GameMembers>();
    }

    public void handle(String message, Session session) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        UserGameCommand.CommandType commandType = command.getCommandType();
        try {
            if (commandType == JOIN_PLAYER) {
                join(message, session);
            }
            else if (commandType == LEAVE) {
                leave(message, session);
            }
            else if (commandType == JOIN_OBSERVER) {
                join(message, session);
            }
            else if (commandType == MAKE_MOVE) {
                makeMove(message, session);
            }
            else if (commandType == RESIGN) {
                resign(message, session);
            }
        }
        catch (Exception ex) {
            ErrorMessage errorMessage = new ErrorMessage("Server Error Details: " + ex.toString());
            System.out.println(ex.toString());
            try {
                session.getRemote().sendString(new Gson().toJson(errorMessage));
                System.out.println("Encountered an error");
            }
            catch (Exception ex2) {
                System.out.println("Attempted to send error message to root client but failed.");
            }
        }
    }

    private void resign(String command, Session session) throws Exception{
        ResignMessage resignMessage = new Gson().fromJson(command, ResignMessage.class);
        String username = new AuthDao().getUsernameFromToken(resignMessage.getAuthString());
        int gameID = resignMessage.getGameID();

        GameMembers gameMembers = findGameMembers(gameID);

        //Check to see if observer made the request
        ChessGame.TeamColor color = gameMembers.getColor(session);
        if (color == null) {
            throw new Exception("Observers cannot resign");
        }

        //Check to see if game is over
        GameInstance gameInstance = new GameDao().getGameInstance(gameID);
        if (!gameInstance.gameActive()) {
            throw new Exception("Game is already over. You cannot resign");
        }

        gameInstance.endGame();
        new GameDao().updateGame(gameInstance, gameID);
        new GameDao().removeUser(color, gameID);


        Notification notification = new Notification(username + " has resigned and left the game");
        gameMembers.sendToAll(new Gson().toJson(notification), null);
        gameMembers.removeMember(session);
    }

    private void makeMove(String command, Session session) throws Exception {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessPosition.class, new ChessPositionAdapter());
        Gson gson = gsonBuilder.create();
        MakeMoveMessage moveMessage = gson.fromJson(command, MakeMoveMessage.class);

        ChessMove move = moveMessage.getMove();
        int gameID = moveMessage.getGameID();
        GameMembers gameMembers = findGameMembers(gameID);

        String username = new AuthDao().getUsernameFromToken(moveMessage.getAuthString());
        ChessGame.TeamColor color = gameMembers.getColor(session);

        //Check to see if an observer is requesting a move
        if (color == null) {
            throw new Exception("Observers cannot make moves");
        }

        //Get the game instance
        GameInstance gameInstance = new GameDao().getGameInstance(gameID);

        //Check to see if game is over
        if (!gameInstance.gameActive()) {
            throw new Exception("The game is over. No new moves can be made");
        }

        //Check to make sure correct color is playing
        if (gameInstance.getTeamTurn() != color) {
            throw new Exception("It is not your turn");
        }

        //Make a move and update database
        gameInstance.makeMove(move);
        new GameDao().updateGame(gameInstance, gameID);

        //Load message to all clients
        LoadGameMessage loadGameMessage = new LoadGameMessage(gameInstance);
        gameMembers.sendToAll(new Gson().toJson(loadGameMessage), null);

        //Notification to all except root client
        Notification moveNotification = new Notification(username + " made the move " + move.toString());
        gameMembers.sendToAll(new Gson().toJson(moveNotification), session);

        //Check and Checkmate notifications
        Notification checkNotification;
        String whiteUsername = gameMembers.getWhiteUsername();
        String blackUsername = gameMembers.getBlackUsername();
        if (gameInstance.isInCheckmate(WHITE)) {
            gameInstance.endGame();
            new GameDao().updateGame(gameInstance, gameID);
            checkNotification = new Notification(whiteUsername + " (white) is in checkmate.");
            gameMembers.sendToAll(new Gson().toJson(checkNotification), null);
        }
        else if (gameInstance.isInCheckmate(BLACK)) {
            gameInstance.endGame();
            new GameDao().updateGame(gameInstance, gameID);
            checkNotification = new Notification(blackUsername + " (black) is in checkmate.");
            gameMembers.sendToAll(new Gson().toJson(checkNotification), null);
        }
        else if (gameInstance.isInCheck(WHITE)) {
            checkNotification = new Notification(whiteUsername + " (white) is in check.");
            gameMembers.sendToAll(new Gson().toJson(checkNotification), null);
        }
        else if (gameInstance.isInCheck(BLACK)) {
            checkNotification = new Notification(blackUsername + " (black) is in checkmate.");
            gameMembers.sendToAll(new Gson().toJson(checkNotification), null);
        }
    }

    private GameMembers findGameMembers (int gameID) throws Exception {
        for (GameMembers game : gameList) {
            if (game.getGameID() == gameID) {
                return game;
            }
        }
        throw new Exception("Could not find requested gameID");
    }

    public static void eraseAllConnections() {
        gameList = new ArrayList<GameMembers>();
    }
    private void leave(String command, Session session) throws Exception {
        System.out.println("Arrived outside");
        LeaveGameMessage leaveGameMessage = new Gson().fromJson(command, LeaveGameMessage.class);
        int gameID = leaveGameMessage.getGameID();
        String authToken = leaveGameMessage.getAuthString();
        String username = new AuthDao().getUsernameFromToken(authToken);

        for (GameMembers game : gameList) {
            if (game.getGameID() == gameID) {
                String message;
                ChessGame.TeamColor color = game.getColor(session);
                System.out.println("Arrived inside");
                //Remove from database
                if (color != null) {
                    new GameDao().removeUser(color, gameID);
                }
                game.removeMember(session);
                if (color == WHITE) {
                    message = username + " (white) has left the game";
                    new GameDao().removeUser(color, gameID);
                }
                else if (color == BLACK) {
                    message = username + " (black) has left the game";
                    new GameDao().removeUser(color, gameID);
                }
                else {
                    message = username + " (observer) has left the game";
                }
                Notification notificationMessage = new Notification(message);
                game.sendToAll(new Gson().toJson(notificationMessage), session);
                break;
            }
        }
    }
    private void join (String command, Session session) throws Exception {
        int gameID;
        ChessGame.TeamColor color;

        UserGameCommand genericCommand = new Gson().fromJson(command,UserGameCommand.class);
        String authToken = genericCommand.getAuthString();

        if (genericCommand.getCommandType() == JOIN_PLAYER) {
            JoinPlayerMessage joinMessage = new Gson().fromJson(command, JoinPlayerMessage.class);
            gameID = joinMessage.getGameID();
            color = joinMessage.getColor();
        }
        else if (genericCommand.getCommandType() == JOIN_OBSERVER){
            JoinObserverMessage joinMessage = new Gson().fromJson(command, JoinObserverMessage.class);
            gameID = joinMessage.getGameID();
            color = null;
        } else {
            throw new Exception("Problem with server code");
        }

        //Authenticate User
        String username = new AuthDao().getUsernameFromToken(authToken);

        //Send out notification or create new game

        for (GameMembers gameMembers : gameList) {
            if (gameMembers.getGameID() == gameID) {
                String message;
                gameMembers.addNewMember(session, color, username);
                if (color == WHITE) {
                    message = username + " joined the game as white";
                } else if (color == BLACK) {
                    message = username + " joined the game as black";
                } else {
                    message = username + " joined game as observer";
                }
                Notification notificationMessage = new Notification(message);
                gameMembers.sendToAll(new Gson().toJson(notificationMessage), session);
                break;
            }
        }

        //Send back game to root client.
        GameInstance gameDetails = new GameDao().getGameInstance(gameID);
        LoadGameMessage loadMessage = new LoadGameMessage(gameDetails);
        session.getRemote().sendString(new Gson().toJson(loadMessage));
    }
}

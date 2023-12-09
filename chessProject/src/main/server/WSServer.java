package server;

import ServerMessages.ErrorMessage;
import ServerMessages.LoadGame;
import UserMessages.JoinPlayer;
import chess.ChessGame;
import com.google.gson.Gson;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.UserGameCommand;
import dao.*;
import java.util.ArrayList;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.JOIN_PLAYER;

public class WSServer {

    ArrayList<GameMembers> gameList;

    public WSServer() {
        gameList = new ArrayList<GameMembers>();
    }
    public void handle(String message, Session session) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        UserGameCommand.CommandType commandType = command.getCommandType();
        try {
            if (commandType == JOIN_PLAYER) {
                join(new Gson().fromJson(message, JoinPlayer.class), session);
            }
        }
        catch (Exception ex) {
            ErrorMessage errorMessage = new ErrorMessage("Error Details: " + ex.toString());
            try {
                session.getRemote().sendString(new Gson().toJson(errorMessage));
                System.out.println("Encountered an error");
            }
            catch (Exception ex2) {
                System.out.println("Attempted to send error message to root client but failed.");
            }
        }
    }

    private void join (JoinPlayer command, Session session) throws Exception {
        String authToken = command.getAuthString();
        int gameID = command.getGameID();
        ChessGame.TeamColor color = command.getColor();
        String username;

        //Authenticate User
        username = new AuthDao().getUsernameFromToken(authToken);

        //Send out notification or create new game
        for (int i = 0; i < gameList.size(); i++) {
            if (gameList.get(i).getGameID() == gameID) {
                String message;
                if (color == ChessGame.TeamColor.WHITE) {
                    message = username + " joined the game as white";
                } else {
                    message = username + " joined the game as black";
                }
                break;
            }
            else if ((i + 1) == gameList.size()){
                GameMembers newGame = new GameMembers(gameID);
                newGame.addNewMember(session, username);
                gameList.add(newGame);
            }
        }

        //Send back game to root client.
        Game gameDetails = new GameDao().getGameDetails(gameID);
        LoadGame loadMessage = new LoadGame(gameDetails);
        session.getRemote().sendString(new Gson().toJson(loadMessage));
    }
}

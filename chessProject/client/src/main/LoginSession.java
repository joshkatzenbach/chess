import chess.ChessGame;
import chessCode.AChessBoard;
import models.*;
import requests.JoinGameRequest;
import results.CreateGameResult;
import results.ListGamesResult;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.*;

import static ui.EscapeSequences.*;

public class LoginSession {
    private final AuthToken authToken;
    private final Scanner scanner;
    private Map<String,Integer> currentGames;
    private final Connector connector;

    public LoginSession(String authToken, String username, Scanner scanner, Connector connector) {
        this.scanner = scanner;
        this.connector = connector;
        this.authToken = new AuthToken();
        this.authToken.setUsername(username);
        this.authToken.setAuthToken(authToken);
    }

    public void run() {
        while (true) {
            System.out.print(SET_TEXT_COLOR_WHITE);
            System.out.printf("[LOGGED_IN as %s] >>> ", authToken.getUsername());
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            try {
                if (Objects.equals(tokens[0], "logout")) {
                    if (logout(tokens)) {
                        System.out.print(SET_TEXT_COLOR_WHITE);
                        System.out.print("Logging out...\n");
                        break;
                    }
                }
                else if (Objects.equals(tokens[0], "create")){
                    if (!createGame(tokens)){
                        System.out.print(SET_TEXT_COLOR_RED);
                        System.out.println("Command Unsuccessful");
                    }
                } else if (Objects.equals(tokens[0], "list")) {
                    if (!listGames(tokens)) {
                        System.out.print(SET_TEXT_COLOR_RED);
                        System.out.println("Command Unsuccessful");
                    }
                } else if (Objects.equals(tokens[0], "join")) {
                    if (!joinGame(tokens)) {
                        System.out.print(SET_TEXT_COLOR_RED);
                        System.out.println("Command Unsuccessful");
                    }
                } else if (Objects.equals(tokens[0], "quit")) {
                    System.exit(0);
                } else if (Objects.equals(tokens[0], "help")) {
                    help();
                } else if (Objects.equals(tokens[0], "observe")) {
                    if (tokens.length != 2) {
                        System.out.print(SET_TEXT_COLOR_RED);
                        System.out.println("Invalid Syntax");
                    }
                    else {
                        if (!joinGame(tokens)) {
                            System.out.print(SET_TEXT_COLOR_RED);
                            System.out.println("Command Unsuccessful");
                        }
                    }
                }
                else {
                    System.out.print(SET_TEXT_COLOR_RED);
                    System.out.printf("Invalid Token: %s\n", tokens[0]);
                }
            }
            catch(IOException | URISyntaxException ex) {
                System.out.print(SET_TEXT_COLOR_RED);
                System.out.println(ex.toString());
            }
        }
    }
    public boolean joinGame(String[] tokens) throws IOException, URISyntaxException {
        ChessGame.TeamColor teamColor = null;
        if (tokens.length > 3) {
            System.out.print(SET_TEXT_COLOR_RED);
            System.out.println("Invalid Input Syntax\n");
            return false;
        }
        Integer gameID = currentGames.get(tokens[1]);
        if (gameID == null) {
            System.out.print(SET_TEXT_COLOR_RED);
            System.out.print("Game not found. Have you listed the games yet?\n");
            return false;
        }
        JoinGameRequest request = new JoinGameRequest();
        request.setGameID(gameID);

        if (tokens.length == 3) {
            if ((Objects.equals(tokens[2], "black"))) {
                request.setPlayerColor(ChessGame.TeamColor.BLACK);
                teamColor = ChessGame.TeamColor.BLACK;
            }
            else if((Objects.equals(tokens[2], "white"))) {
                request.setPlayerColor(ChessGame.TeamColor.WHITE);
                teamColor = ChessGame.TeamColor.WHITE;
            }
            else {
                System.out.print(SET_TEXT_COLOR_RED);
                System.out.print("Invalid Color");
                return false;
            }
        }

        HttpURLConnection http = connector.makeRequest("/game", "PUT", request, authToken);
        if (http.getResponseCode() == 200) {

            Gameplay newGame = new Gameplay();
            try {
                newGame.play(gameID, authToken.getUsername(), teamColor, authToken.getAuthToken());
            }
            catch (Exception ex) {
                System.out.print(SET_TEXT_COLOR_RED);
                System.out.println("Error: Problem with websocket");
                System.out.println(ex.toString());
                return false;
            }
            return true;
        }
        else {
            System.out.print(SET_TEXT_COLOR_RED);
            String error = http.getResponseMessage();
            System.out.println(error);
            return false;
        }
    }
    public boolean listGames(String[] tokens) throws IOException, URISyntaxException {
        if (tokens.length != 1) {
            System.out.print(SET_TEXT_COLOR_RED);
            System.out.println("Invalid Input Syntax\n");
            return false;
        }

        HttpURLConnection http = connector.makeRequest("/game", "GET", null, authToken);
        if (http.getResponseCode() == 200) {
            ListGamesResult result =  (ListGamesResult) connector.getResponseBody(http, ListGamesResult.class);
            ArrayList<Game> games = result.getGames();
            System.out.print(SET_TEXT_COLOR_WHITE);
            System.out.println("Games:\n");
            currentGames = new TreeMap<String, Integer>();
            for (int i = 0; i < games.size(); i++) {
                Game game = games.get(i);
                System.out.print(SET_TEXT_COLOR_WHITE);
                System.out.printf("%-3d %s\n\tWhite: %s\n\tBlack: %s\n\n", i + 1, game.getGameName(), game.getWhiteUsername(), game.getBlackUsername());
                currentGames.put("" + (i + 1), game.getGameID());
            }
            return true;
        }
        else {
            String error = http.getResponseMessage();
            System.out.print(SET_TEXT_COLOR_RED);
            System.out.println(error);
            return false;
        }
    }

    public boolean logout(String [] tokens) throws IOException, URISyntaxException {
        if (tokens.length != 1) {
            System.out.print(SET_TEXT_COLOR_RED);
            System.out.println("Invalid Input Syntax\n");
            return false;
        }
        HttpURLConnection http = connector.makeRequest("/session", "DELETE", null, authToken);
        if (http.getResponseCode() == 200) {
            return true;
        }
        else {
            System.out.print(SET_TEXT_COLOR_RED);
            System.out.println("Error logging out\n");
            return false;
        }
    }

    public boolean createGame(String[] tokens) throws IOException, URISyntaxException {
        if (tokens.length != 2) {
            System.out.print(SET_TEXT_COLOR_RED);
            System.out.println("Invalid Input Syntax\n");
            return false;
        }

        Game newGame = new Game();
        newGame.setGameName(tokens[1]);

        HttpURLConnection http = connector.makeRequest("/game", "POST", newGame, authToken);
        if (http.getResponseCode() == 200) {
            try {
                CreateGameResult result = (CreateGameResult) connector.getResponseBody(http, CreateGameResult.class);
            }
            catch (IOException ex) {
                throw new IOException("Game created but could not retrieve gameID");
            }
            return true;
        }
        else {
            String error = http.getResponseMessage();
            System.out.print(SET_TEXT_COLOR_RED);
            System.out.println(error);
            return false;
        }
    }

    public Map<String, Integer> getGames() {
        return currentGames;
    }
    public void help() {
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\tcreate <NAME>");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - a game\n");
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\tlist");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - games\n");
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\tjoin <ID> [WHITE|BLACK|<empty>]");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - a game\n");
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\tobserve <ID>");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - a game\n");
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\tlogout");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - when you are done\n");
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\tquit");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - playing chess\n");
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\thelp");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - with possible commands\n");
    }
}

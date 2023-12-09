import models.AuthToken;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class PreSession {
    private final Connector connector;
    private Scanner scanner;

    private AuthToken authToken;
    public PreSession(Scanner scanner, Connector connector) {
        this.scanner = scanner;
        this.connector = connector;
    }

    public PreSession (Connector connector) {
        this.connector = connector;
    }
    public void runUI() {
        while (true) {
            System.out.print(SET_TEXT_COLOR_WHITE);
            System.out.print("[LOGGED_OUT] >>> ");
            String line = scanner.nextLine();
            String[] tokens = line.split(" ");

            try {
                if (Objects.equals(tokens[0], "quit")) {
                    break;
                } else if (Objects.equals(tokens[0], "register")) {
                    if (register(tokens)) {
                        LoginSession newLogin = new LoginSession(authToken.getAuthToken(), authToken.getUsername(), scanner, connector);
                        newLogin.run();
                    } else {
                        System.out.print(SET_TEXT_COLOR_RED);
                        System.out.print("Command Unsuccessful\n");
                    }
                } else if (Objects.equals(tokens[0], "help")) {
                    loggedOutHelp();
                } else if (Objects.equals(tokens[0], "login")) {
                    if (login(tokens)) {
                        LoginSession newLogin = new LoginSession(authToken.getAuthToken(), authToken.getUsername(), scanner, connector);
                        newLogin.run();
                    } else {
                        System.out.print(SET_TEXT_COLOR_RED);
                        System.out.print("Command Unsuccessful\n");
                    }
                } else {
                    System.out.print(SET_TEXT_COLOR_RED);
                    System.out.printf("Invalid option: %s\n", tokens[0]);
                }
            } catch (IOException | URISyntaxException ex) {
                System.out.print(SET_TEXT_COLOR_RED);
                System.out.println(ex.toString());
            }
        }
    }


    public boolean register(String[] tokens) throws java.io.IOException,  java.net.URISyntaxException  {
        if (tokens.length != 4) {
            System.out.print(SET_TEXT_COLOR_RED);
            System.out.println("Incorrect number of inputs");
            return false;
        }
        RegisterRequest request = new RegisterRequest();
        request.setUsername(tokens[1]);
        request.setPassword(tokens[2]);
        request.setEmail(tokens[3]);

        HttpURLConnection http = connector.makeRequest("/user", "POST", request, null);
        if (http.getResponseCode() == 200) {
            RegisterResult result = (RegisterResult) connector.getResponseBody(http, RegisterResult.class);
            authToken = new AuthToken();
            authToken.setAuthToken(result.getAuthToken());
            authToken.setUsername(result.getUsername());
            return true;
        }
        else {
            System.out.print(SET_TEXT_COLOR_RED);
            String error = http.getResponseMessage();
            System.out.println(error);
            return false;
        }
    }

    public boolean login(String[] tokens) throws IOException, URISyntaxException{
        if (tokens.length != 3) {
            System.out.print(SET_TEXT_COLOR_RED);
            System.out.println("Incorrect number of inputs");
            return false;
        }

        LoginRequest request = new LoginRequest();
        request.setUsername(tokens[1]);
        request.setPassword(tokens[2]);

        HttpURLConnection http = connector.makeRequest("/session", "POST", request, null);
        if (http.getResponseCode() == 200) {
            LoginResult result = (LoginResult) connector.getResponseBody(http, LoginResult.class);
            LoginSession newLogin = new LoginSession(result.getAuthToken(), result.getUsername(), scanner, connector);
            authToken = new AuthToken();
            authToken.setAuthToken(result.getAuthToken());
            authToken.setUsername(result.getUsername());
            return true;
        }
        else {
            System.out.print(SET_TEXT_COLOR_RED);
            String error = http.getResponseMessage();
            System.out.println(error);
            return false;
        }
    }

    public AuthToken getAuthToken() {
        return authToken;
    }
    public void loggedOutHelp() {
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\tregister <USERNAME> <PASSWORD> <EMAIL>");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - to create an account\n");
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\tlogin <USERNAME> <PASSWORD>");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - to play chess\n");
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\tquit");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - playing chess\n");
        System.out.print(SET_TEXT_COLOR_MAGENTA);
        System.out.print("\thelp");
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.print(" - with possible commands\n\n");
    }


}

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.net.*;
import com.google.gson.*;
import models.*;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;
import ui.EscapeSequences.*;

import static ui.EscapeSequences.*;

public class ClientMain {

    static private String host;

    static private String port;

    public static void main(String [] args) {
        ClientMain client = new ClientMain();
        client.run(args);

    }

    private void run(String [] args) {
        Scanner scanner = new Scanner(System.in);
        Connector connector;
        if (args.length > 0) {
            host = args[0];
            port = args[1];

        } else {
            host = "localhost";
            port = "8080";
        }
        connector = new Connector("http://" + host + ":" + port);

        System.out.print(RESET_BG_COLOR);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(" - Welcome to to 240 Chess - \n");
        System.out.print("Type <help> to get started\n");

        PreSession preLogin = new PreSession(scanner, connector);
        preLogin.runUI();
    }

    public static String getPort() {
        return port;
    }

    public static String getHost() {
        return host;
    }

}

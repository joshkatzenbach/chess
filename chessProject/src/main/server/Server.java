package server;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.*;
import handlers.*;

import java.io.IOException;
import java.sql.*;
import dao.*;
import org.eclipse.jetty.websocket.api.Session;

@WebSocket
public class Server {

    private WSServer webSocketHandler;

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    public Server() {
        webSocketHandler = new WSServer();
    }
    private void run() {
        initializeDatabase();

        Spark.port(8080);
        Spark.webSocket("/connect", Server.class);
        Spark.externalStaticFileLocation("web");
        Spark.delete("/db", (req, res) -> (new ClearHandler().HandleRequest(req, res)));
        Spark.post("/user", (req, res) -> (new RegisterHandler().HandleRequest(req, res)));
        Spark.post("session", (req, res) -> (new LoginHandler().HandleRequest(req, res)));
        Spark.delete("/session", (req, res) -> (new LogoutHandler().HandleRequest(req, res)));
        Spark.get("/game", (req, res) -> (new ListGamesHandler().HandleRequest(req, res)));
        Spark.post("/game", (req, res) -> (new CreateGameHandler().HandleRequest(req, res)));
        Spark.put("/game", (req, res) -> (new JoinGameHandler().HandleRequest(req, res)));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("Arrived here");
        webSocketHandler.handle(message, session);
    }


    private void initializeDatabase() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "sqlKatz1!1");

            var createDbStatement = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS chess");
            createDbStatement.executeUpdate();
            conn.setCatalog("chess");

            String createAuthTable = """
                    CREATE TABLE IF NOT EXISTS authTable (
                        username varChar(255) NOT NULL,
                        authToken varChar(255) NOT NULL,
                        PRIMARY KEY (authToken)
                    )""";

            String createGameTable = """
                    CREATE TABLE IF NOT EXISTS gameTable (
                        gameID int NOT NULL,
                        whiteUsername varChar(255),
                        blackUsername varChar(255),
                        gameName varChar(255) NOT NULL,
                        game varChar(2048) NOT NULL,
                        PRIMARY KEY (gameID)
                    )""";

            String createUserTable = """
                    CREATE TABLE IF NOT EXISTS userTable (
                        username varChar(255) NOT NULL,
                        password varChar(255) NOT NULL,
                        email varChar(255) NOT NULL,
                        PRIMARY KEY (username)
                    )""";

            var createAuthTableStatement = conn.prepareStatement(createAuthTable);
            createAuthTableStatement.executeUpdate();

            var createGameTableStatement = conn.prepareStatement(createGameTable);
            createGameTableStatement.executeUpdate();

            var createUserTableStatement = conn.prepareStatement(createUserTable);
            createUserTableStatement.executeUpdate();

            AuthDao.setConn(conn);
            GameDao.setConn(conn);
            UserDao.setConn(conn);
        }
        catch(SQLException ex) {
            System.out.println("Server couldn't initialize database");
            System.exit(1);
        }
    }
}

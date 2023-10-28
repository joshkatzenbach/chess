package server;
import spark.*;
import com.google.gson.*;
import java.util.*;
import handlers.*;
public class Server {

    public static void main(String[] args) {
        new Server().run();
    }
    private void run() {
        Spark.port(8080);
        Spark.externalStaticFileLocation("web");
        Spark.delete("/db", (req, res) -> (new ClearHandler().HandleRequest(req, res)));
        Spark.post("/user", (req, res) -> (new RegisterHandler().HandleRequest(req, res)));
        Spark.post("session", (req, res) -> (new LoginHandler().HandleRequest(req, res)));
        Spark.delete("/session", (req, res) -> (new LogoutHandler().HandleRequest(req, res)));
        Spark.get("/game", (req, res) -> (new ListGamesHandler().HandleRequest(req, res)));
        Spark.post("/game", (req, res) -> (new CreateGameHandler().HandleRequest(req, res)));
        Spark.put("/game", (req, res) -> (new JoinGameHandler().HandleRequest(req, res)));
    }

}

package handlers;

import com.google.gson.Gson;
import requests.JoinGameRequest;
import results.JoinGameResult;
import services.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    public Object HandleRequest(Request req, Response res) {
        JoinGameRequest joinGameReq = (JoinGameRequest) new Gson().fromJson(req.body(), JoinGameRequest.class);
        JoinGameResult joinGameRes = new JoinGameService().joinGame(joinGameReq, req.headers("authorization"));
        res.status(joinGameRes.getErrorCode());
        res.type("application/json");
        return new Gson().toJson(joinGameRes);
    }
}

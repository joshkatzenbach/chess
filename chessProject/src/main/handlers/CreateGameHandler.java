package handlers;

import com.google.gson.Gson;
import requests.CreateGameRequest;
import results.ClearResult;
import results.CreateGameResult;
import services.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    public Object HandleRequest(Request req, Response res) {
        CreateGameRequest createGameReq = (CreateGameRequest) new Gson().fromJson(req.body(), CreateGameRequest.class);
        CreateGameResult createGameRes = new CreateGameService().createGame(createGameReq, req.headers("authorization"));
        res.status(createGameRes.getErrorCode());
        res.type("application/json");
        if (createGameRes.getGameID() != 0) {
            return new Gson().toJson(createGameRes);
        }
        else {
            return new Gson().toJson(new ClearResult(createGameRes.getMessage()));
        }
    }
}

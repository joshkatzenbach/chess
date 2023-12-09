package handlers;

import com.google.gson.Gson;
import results.ListGamesResult;
import services.ListGamesService;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    public Object HandleRequest(Request req, Response res) {
        ListGamesResult listGamesRes = new ListGamesService().listGames(req.headers(("authorization")));
        res.type("application/json");
        res.status(listGamesRes.getErrorCode());
        return new Gson().toJson(listGamesRes);
    }
}

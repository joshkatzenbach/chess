package handlers;

import com.google.gson.Gson;
import results.LogoutResult;
import spark.Request;
import spark.Response;
import services.*;

public class LogoutHandler {
    public Object HandleRequest(Request req, Response res) {
        LogoutResult logoutRes = new LogoutService().logout(req.headers("authorization"));
        res.type("application/json");
        res.status(logoutRes.getErrorCode());
        return new Gson().toJson(logoutRes);
    }
}

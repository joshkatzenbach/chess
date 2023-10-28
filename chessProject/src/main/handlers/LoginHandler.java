package handlers;

import requests.LoginRequest;
import results.LoginResult;
import services.LoginService;
import spark.Request;
import spark.Response;
import com.google.gson.*;

public class LoginHandler {
    public Object HandleRequest(Request req, Response res) {
        LoginRequest loginReq = (LoginRequest) new Gson().fromJson(req.body(), LoginRequest.class);
        LoginResult loginRes = new LoginService().login(loginReq);
        res.type("application/json");
        res.status(loginRes.getErrorCode());
        return new Gson().toJson(loginRes);
    }
}

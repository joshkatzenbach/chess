package handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import requests.RegisterRequest;
import results.RegisterResult;
import services.RegisterService;
import spark.*;

import javax.xml.crypto.Data;

public class RegisterHandler {
    public Object HandleRequest(Request req, Response res) {
        RegisterRequest registerReq = (RegisterRequest) new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterResult registerRes = new RegisterService().register(registerReq);
        res.type("application/json");
        res.status(registerRes.getErrorCode());
        return new Gson().toJson(registerRes);
    }
}

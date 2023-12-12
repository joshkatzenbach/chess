package handlers;

import com.google.gson.*;
import dataAccess.DataAccessException;
import results.ClearResult;
import server.WSServer;
import services.ClearService;
import spark.*;

public class ClearHandler {
    public Object HandleRequest(Request req, Response res) throws DataAccessException {

        WSServer.eraseAllConnections();

        ClearResult clearRes = new ClearService().clear();
        res.type("application/json");
        res.status(clearRes.getErrorCode());
        return new Gson().toJson(clearRes);
    }
}

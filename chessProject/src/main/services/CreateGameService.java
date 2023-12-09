package services;

import dataAccess.DataAccessException;
import requests.CreateGameRequest;
import results.CreateGameResult;
import dao.*;

public class CreateGameService {
    public CreateGameResult createGame(CreateGameRequest request, String authToken) {
        CreateGameResult result = new CreateGameResult();
        if (request.getGameName() == null) {
            result.setErrorCode(400);
            result.setMessage("Error Bad Request");
            return result;
        }
        try {
            if (!new AuthDao().authorizeToken(authToken)) {
                result.setErrorCode(401);
                result.setMessage("Error: Unauthorized");
                return result;
            }
            else {
                int gameID = new GameDao().createGame(request.getGameName());
                result.setGameID(gameID);
                result.setErrorCode(200);
                return result;
            }
        }
        catch (DataAccessException ex) {
            CreateGameResult errorResult = new CreateGameResult();
            errorResult.setErrorCode(500);
            errorResult.setMessage(ex.getMessage());
            return errorResult;
        }

    }
}

package services;

import dataAccess.DataAccessException;
import requests.CreateGameRequest;
import results.CreateGameResult;
import dao.*;

/**
 * Class Creates models and uses Data Access Objects to Create new games
 */
public class CreateGameService {
    /**
     * Creates a new game and receives server response
     * @param request CreateGameRequest object with details for HTTP request
     * @return CreateGameResult object with server response details
     */
    public CreateGameResult createGame(CreateGameRequest request, String authToken) {
        CreateGameResult result = new CreateGameResult();
        if (request.getGameName() == null) {
            result.setErrorCode(400);
            result.setMessage("Error Bad Request");
            return result;
        }

        if (!new AuthDao().authorizeToken(authToken)) {
            result.setErrorCode(401);
            result.setMessage("Error: Unauthorized");
            return result;
        }
        try {
            int gameID = new GameDao().createGame(request.getGameName());
            System.out.println("Game ID:");
            System.out.println(gameID);
            result.setGameID(gameID);
            result.setErrorCode(200);
            return result;
        }
        catch (DataAccessException ex) {
            CreateGameResult errorResult = new CreateGameResult();
            errorResult.setErrorCode(400);
            errorResult.setMessage(ex.getMessage());
            return errorResult;
        }

    }
}

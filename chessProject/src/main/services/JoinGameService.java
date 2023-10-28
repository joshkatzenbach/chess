package services;

import dao.AuthDao;
import dao.GameDao;
import dataAccess.DataAccessException;
import requests.JoinGameRequest;
import results.JoinGameResult;

/**
 * Class Creates models and uses Data Access Objects to join games
 */
public class JoinGameService {
    /**
     * Joins a new game and receives server response
     * @param request JoinGameRequest object with details for HTTP request
     * @return JoinGameRequest Result with information from server
     */
    public JoinGameResult joinGame(JoinGameRequest request, String authToken) {
        JoinGameResult result = new JoinGameResult();
        String username;
        if (!new AuthDao().authorizeToken(authToken)) {
            result.setErrorCode(401);
            result.setMessage("Error Unauthorized");
            return result;
        }

        try {
            username = new AuthDao().getUsernamefromToken(authToken);
        }
        catch (DataAccessException ex) {
            JoinGameResult errorResult = new JoinGameResult();
            errorResult.setErrorCode(500);
            errorResult.setMessage(ex.getMessage());
            return errorResult;
        }

        try {
            if (new GameDao().joinGame(request.getGameID(), username, request.getPlayerColor())) {
                result.setErrorCode(200);
            } else {
                result.setErrorCode(403);
                result.setMessage("Error: Already Taken");
            }
            return result;
        }
        catch (DataAccessException ex) {
            JoinGameResult errorResult = new JoinGameResult();
            errorResult.setErrorCode(400);
            errorResult.setMessage(ex.getMessage());
            return errorResult;
        }
    }
}

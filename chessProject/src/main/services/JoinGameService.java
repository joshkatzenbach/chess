package services;

import dao.AuthDao;
import dao.GameDao;
import dataAccess.DataAccessException;
import requests.JoinGameRequest;
import results.JoinGameResult;

public class JoinGameService {
    public JoinGameResult joinGame(JoinGameRequest request, String authToken) {
        JoinGameResult result = new JoinGameResult();
        String username;
        try {
            if (!new AuthDao().authorizeToken(authToken)) {
                result.setErrorCode(401);
                result.setMessage("Error Unauthorized");
                return result;
            }

            username = new AuthDao().getUsernameFromToken(authToken);
            int joinReturnCode = new GameDao().joinGame(request.getGameID(), username, request.getPlayerColor());
            if (joinReturnCode == 1) {
                result.setErrorCode(200);
            } else if (joinReturnCode == 0) {
                result.setErrorCode(403);
                result.setMessage("Error: Color already taken");
            }
            else {
                result.setErrorCode(400);
                result.setMessage("Error: Bad Request");
            }
            return result;
        }
        catch (DataAccessException ex) {
            JoinGameResult errorResult = new JoinGameResult();
            errorResult.setErrorCode(500);
            errorResult.setMessage(ex.getMessage());
            return errorResult;
        }
    }
}

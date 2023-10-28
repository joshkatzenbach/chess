package services;

import dao.AuthDao;
import dataAccess.DataAccessException;
import results.LogoutResult;
import requests.*;

/**
 * Class Creates models and uses Data Access Objects to end login session
 */
public class LogoutService {
    /**
     * Logs user out and captures server response
     * @return LogoutResult object that contains info from server
     */
    public LogoutResult logout(String authToken) {

        if (!new AuthDao().authorizeToken(authToken)) {
            LogoutResult result = new LogoutResult();
            result.setErrorCode(401);
            result.setMessage("Error: Unauthorized");
            return result;
        }

        try {
            new AuthDao().removeToken(authToken);
            LogoutResult result = new LogoutResult();
            result.setErrorCode(200);
            return result;
        }
        catch (DataAccessException ex) {
            LogoutResult errorResult = new LogoutResult();
            errorResult.setErrorCode(500);
            errorResult.setMessage(ex.getMessage());
            return errorResult;
        }
    }
}

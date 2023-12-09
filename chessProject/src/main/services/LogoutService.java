package services;

import dao.AuthDao;
import dataAccess.DataAccessException;
import results.LogoutResult;

public class LogoutService {
    public LogoutResult logout(String authToken) {
        try {
            if (!new AuthDao().authorizeToken(authToken)) {
                LogoutResult result = new LogoutResult();
                result.setErrorCode(401);
                result.setMessage("Error: Unauthorized");
                return result;
            }
            else {
                new AuthDao().removeToken(authToken);
                LogoutResult result = new LogoutResult();
                result.setErrorCode(200);
                return result;
            }
        }
        catch (DataAccessException ex) {
            LogoutResult errorResult = new LogoutResult();
            errorResult.setErrorCode(500);
            errorResult.setMessage(ex.getMessage());
            return errorResult;
        }
    }
}

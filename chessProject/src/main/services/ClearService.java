package services;

import dataAccess.DataAccessException;
import results.ClearResult;
import dao.*;
public class ClearService {
    public ClearResult clear() {
        ClearResult result = new ClearResult();
        try {
            new AuthDao().clear();
            new GameDao().clear();
            new UserDao().clear();
            result.setErrorCode(200);
            return result;
        }
        catch (DataAccessException ex) {
            ClearResult errorResult = new ClearResult();
            errorResult.setErrorCode(500);
            errorResult.setMessage(ex.getMessage());
            return errorResult;
        }
    };
}

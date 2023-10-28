package services;

import dataAccess.DataAccessException;
import results.ClearResult;
import dao.*;

/**
 * Class Create models and uses Data Access Objects to Clear Database
 */
public class ClearService {
    /**
     * Clears entire database
     * @return A ClearResult object with error codes
     */
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

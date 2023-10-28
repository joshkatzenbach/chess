package services;

import dataAccess.DataAccessException;
import requests.RegisterRequest;
import results.RegisterResult;
import dao.*;
import models.*;

/**
 * Class Creates models and uses Data Access Objects to add new user to database
 */
public class RegisterService {
    /**
     * Adds new user to database and captures server response
     * @param request RegisterRequest object with HTTP info
     * @return RegisterResult object with info from server
     */
    public RegisterResult register(RegisterRequest request) {
        RegisterResult result = new RegisterResult();

        if ((request.getUsername() == null) || (request.getPassword() == null) || (request.getEmail() == null)) {
            result.setErrorCode(400);
            result.setMessage("Error: Bad Request");
            return result;
        }

        try {
            new UserDao().addUser(new User(request.getUsername(), request.getPassword(), request.getEmail()));
        }
        catch (DataAccessException ex) {
            RegisterResult errorResult = new RegisterResult();
            errorResult.setMessage(ex.getMessage());
            errorResult.setErrorCode(403);
            return errorResult;
        }

        AuthToken token = new AuthDao().generateToken(request.getUsername());
        result.setErrorCode(200);
        result.setUsername(token.getUsername());
        result.setAuthToken(token.getAuthToken());
        return result;
    }
}

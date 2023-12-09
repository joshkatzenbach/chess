package services;

import dataAccess.DataAccessException;
import requests.RegisterRequest;
import results.RegisterResult;
import dao.*;
import models.*;

public class RegisterService {
    public RegisterResult register(RegisterRequest request) {
        RegisterResult result = new RegisterResult();

        if ((request.getUsername() == null) || (request.getPassword() == null) || (request.getEmail() == null)) {
            result.setErrorCode(400);
            result.setMessage("Error: Bad Request");
            return result;
        }

        try {
            if (!new UserDao().addUser(new User(request.getUsername(), request.getPassword(), request.getEmail()))) {
                result.setMessage("Error: Username taken");
                result.setErrorCode(403);
                return result;
            }

            AuthToken token = new AuthDao().generateToken(request.getUsername());
            result.setErrorCode(200);
            result.setUsername(token.getUsername());
            result.setAuthToken(token.getAuthToken());
            return result;
        }
        catch (DataAccessException ex) {
            RegisterResult errorResult = new RegisterResult();
            errorResult.setMessage(ex.getMessage());
            errorResult.setErrorCode(500);
            return errorResult;
        }
    }
}

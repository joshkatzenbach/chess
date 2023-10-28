package services;

import dao.AuthDao;
import dao.UserDao;
import dataAccess.DataAccessException;
import models.AuthToken;
import requests.*;
import results.*;

/**
 * Class Creates models and uses Data Access Objects to create a login session
 */
public class LoginService {
    /**
     * Logs into the system and captures server response
     * @param request LoginRequest object that contains HTTP request info
     * @return LoginResult object with information from server
     */
    public LoginResult login(LoginRequest request) {
        LoginResult result = new LoginResult();
        if (new UserDao().authenticate(request.getUsername(), request.getPassword())) {
            AuthToken token = new AuthDao().generateToken(request.getUsername());
            result.setUsername(request.getUsername());
            result.setAuthToken(token.getAuthToken());
            result.setErrorCode(200);
            return result;
        }
        else {
            result.setErrorCode(401);
            result.setMessage("Error: Unauthorized");
            return result;
        }
    };
}

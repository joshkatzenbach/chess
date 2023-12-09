package services;

import dao.AuthDao;
import dao.UserDao;
import dataAccess.DataAccessException;
import models.AuthToken;
import requests.*;
import results.*;

public class LoginService {
    public LoginResult login(LoginRequest request) {
        LoginResult result = new LoginResult();
        try {
            if (new UserDao().authenticate(request.getUsername(), request.getPassword())) {
                AuthToken token = new AuthDao().generateToken(request.getUsername());
                result.setUsername(request.getUsername());
                result.setAuthToken(token.getAuthToken());
                result.setErrorCode(200);
                return result;
            } else {
                result.setErrorCode(401);
                result.setMessage("Error: Unauthorized");
                return result;
            }
        }
        catch (DataAccessException ex) {
            System.out.println("Here");
            LoginResult errorResult = new LoginResult();
            errorResult.setMessage(ex.getMessage());
            errorResult.setErrorCode(500);
            return errorResult;
        }
    };
}

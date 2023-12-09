package services;

import dao.*;
import dataAccess.DataAccessException;
import models.Game;
import results.ListGamesResult;

import java.util.ArrayList;

public class ListGamesService {
    public  ListGamesResult listGames(String authToken) {
        ListGamesResult result = new ListGamesResult();
        try {
            if (!new AuthDao().authorizeToken(authToken)) {
                result.setErrorCode(401);
                result.setMessage("Error: unauthorized");
            } else {
                ArrayList<Game> games = new GameDao().returnAll();
                result.setGames(games);
                result.setErrorCode(200);
            }
        }
        catch (DataAccessException ex) {
            ListGamesResult errorResult = new ListGamesResult();
            errorResult.setErrorCode(500);
            errorResult.setMessage(ex.getMessage());
            return errorResult;
        }

        return result;
    };
}

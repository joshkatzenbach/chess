package services;

import dao.*;
import models.Game;
import results.ListGamesResult;

import java.util.ArrayList;

/**
 * Class Creates models and uses Data Access Objects to List available games
 */
public class ListGamesService {
    /**
     * Lists all games and receives response from server
     * @return ListGamesResult object with information from server
     */
    public  ListGamesResult listGames(String authToken) {
        ListGamesResult result = new ListGamesResult();
        if (!new AuthDao().authorizeToken(authToken)) {
            result.setErrorCode(401);
            result.setMessage("Error: unauthorized");
        }
        else {
            ArrayList<Game> games = new GameDao().returnAll();
            result.setGames(games);
            result.setErrorCode(200);
        }

        return result;
    };
}

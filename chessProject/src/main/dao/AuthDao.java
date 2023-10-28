package dao;

import models.*;
import dataAccess.*;
import java.util.UUID.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Class takes care of all reads and writes to token database
 */
public class AuthDao {
    /**
     * Temporary storage space until database is created
     */
    private static ArrayList<AuthToken> tokens = new ArrayList<AuthToken>();

    /**
     * Default constructor initializes storage
     */
    public AuthDao() {};

    public AuthToken generateToken(String username) {

        String randomToken = java.util.UUID.randomUUID().toString();
        AuthToken token = new AuthToken();
        token.setAuthToken(randomToken);
        token.setUsername(username);
        tokens.add(token);

        return token;
    };


    public void removeToken(String authToken) throws DataAccessException {
        for (int i = 0; i < tokens.size(); i++) {
            if (Objects.equals(tokens.get(i).getAuthToken(), authToken)) {
                tokens.remove(i);
                return;
            }
        }

        throw new DataAccessException("Not logged in");

    };


    public boolean authorizeToken(String authToken) {
        for (AuthToken token : tokens) {
            if (Objects.equals(token.getAuthToken(), authToken)) {
                return true;
            }
        }
        return false;
    }

    public String getUsernamefromToken(String authToken) throws DataAccessException {
        for (AuthToken token : tokens) {
            if (Objects.equals(token.getAuthToken(), authToken)) {
                return token.getUsername();
            }
        }
        throw new DataAccessException("Token not found");
    }

    public boolean clear() throws DataAccessException {
        tokens.clear();
        return true;
    }

    public static ArrayList<AuthToken> getTokens() {
        return tokens;
    }
}

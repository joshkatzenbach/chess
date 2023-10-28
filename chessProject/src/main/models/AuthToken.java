package models;

import java.util.Objects;

/**
 * Model that matches a username with an authentication token
 */
public class AuthToken {
    /**
     * Unique string identifer
     */
    private String authToken;
    /**
     * Username of the User
     */
    private String username;

    /**
     * Constructor that initializes both the username and token
     */

    public AuthToken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }
    public AuthToken() {};
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthToken authToken1)) return false;
        return Objects.equals(getAuthToken(), authToken1.getAuthToken()) && Objects.equals(getUsername(), authToken1.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthToken(), getUsername());
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}

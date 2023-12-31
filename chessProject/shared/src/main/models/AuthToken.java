package models;

import java.util.Objects;

public class AuthToken {
    private String authToken;
    private String username;


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

package results;

public class LoginResult {
    private transient int errorCode;
    private String username;
    private String authToken;
    private String message;

    public LoginResult(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public LoginResult() {};

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}

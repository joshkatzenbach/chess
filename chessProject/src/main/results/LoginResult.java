package results;

/**
 * Information about Login Response
 */
public class LoginResult {
    /**
     * Nickname for user
     */
    private transient int errorCode;
    private String username;
    /**
     * Unique identifier of login session
     */
    private String authToken;
    /**
     * Response from server
     */
    private String message;

    /**
     * Constructor initializes all fields
     * @param username Nickname for user
     * @param authToken Unique identifier for login session
     * @param message Response from server
     */
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

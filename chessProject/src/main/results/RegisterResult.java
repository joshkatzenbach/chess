package results;

/**
 * Contains information about Register Response
 */
public class RegisterResult {
    /**
     * Nickname for User
     */
    private String username;
    /**
     * Unique identifier for login session
     */
    private String authToken;
    /**
     * Response from server
     */
    private String message;

    private transient int errorCode;

    /**\
     * Constructor initializes all fields
     * @param username Nickname for user
     * @param authToken Unique identifier for login session
     */
    public RegisterResult(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public RegisterResult() {};

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

package requests;

/**
 * Contains information about Login Requests
 */
public class LoginRequest {
    /**
     * Nickname for user
     */
    private String username;
    /**
     * Password associated with username
     */
    private String password;

    /**
     * Constructor initializes username and password
     * @param username Nickname for user
     * @param password Password associated with username
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    };

    public LoginRequest() {};
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

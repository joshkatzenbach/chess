package requests;

/**
 * Constains information about registration requests
 */
public class RegisterRequest {
    /**
     * Nickname for user
     */
    private String username;
    /**
     * Password to be associated with user
     */
    private String password;
    /**
     * Email of user
     */
    private String email;

    /**
     * Constructor that initializes all values
     * @param username Nickname of user
     * @param password Password associated with user
     * @param email User email
     */
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public RegisterRequest() {};

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

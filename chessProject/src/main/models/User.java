package models;

/**
 * Class contains all essential information about a user
 */
public class User {
    /**
     * Unique username for User
     */
    private String username;
    /**
     * Password for account validation
     */
    private String password;
    /**
     * Email of the user
     */
    private String email;

    /**
     * Constructor initializes all parameters
     * @param username Nickname for user
     * @param password string for account validation
     * @param email Contact information
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    /**
     * Constructor initializes just username and password
     * @param username Nickname for user
     * @param password String for account validation
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor initializes just username
     * @param username Nickname for user
     */
    public User(String username) {
        this.username = username;
    }
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

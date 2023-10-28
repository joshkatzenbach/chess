package requests;

/**
 * Class Contains information for new game request
 */
public class CreateGameRequest {
    /**
     * Nickname for game
     */
    private String gameName;

    /**
     * Constructor initializes gameName
     * @param gameName Nickname for game
     */
    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }

    public CreateGameRequest () {};

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}

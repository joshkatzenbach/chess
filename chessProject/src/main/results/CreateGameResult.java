package results;

/**
 * Contains information from Create Game Response
 */
public class CreateGameResult {
    /**
     * GameID of the game created
     */
    private int gameID;

    private String message;

    private transient int errorCode;

    public CreateGameResult() {};

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

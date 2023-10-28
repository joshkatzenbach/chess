package results;

/**
 * Contains information from Join Game Response
 */
public class JoinGameResult {
    /**
     * Response from server
     */
    private String message;

    private transient int errorCode;

    /**
     * Constructor initializes message
     * @param message Response from server
     */
    public JoinGameResult() {};


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

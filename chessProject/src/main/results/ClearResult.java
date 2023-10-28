package results;

/**
 * Contains information about Clear Results
 */
public class ClearResult {
    /**
     * Response from server
     */
    private String message;

    private transient int errorCode;

    /**
     * Constructor initializes message
     * @param message Response from server
     */

    public ClearResult(String message) {
        this.message = message;
    }

    public ClearResult() {};

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

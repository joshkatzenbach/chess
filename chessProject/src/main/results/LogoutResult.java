package results;

/**
 * Contains information about logout response
 */
public class LogoutResult {
    /**
     * Response from server
     */
    private String message;

    private transient int errorCode;

    public LogoutResult() {}

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

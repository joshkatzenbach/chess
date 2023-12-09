package results;

public class ClearResult {
    private String message;
    private transient int errorCode;

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

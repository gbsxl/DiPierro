package Di.Pierro.infrastructure.exception.custom;

import org.springframework.http.HttpStatus;

public abstract class DiPierroException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    protected DiPierroException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    protected DiPierroException(HttpStatus status, String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

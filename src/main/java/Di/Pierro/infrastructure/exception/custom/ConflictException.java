package Di.Pierro.infrastructure.exception.custom;

import org.springframework.http.HttpStatus;

public class ConflictException extends DiPierroException {

    public ConflictException(String errorCode, String message) {
        super(HttpStatus.CONFLICT, errorCode, message);
    }
}

package Di.Pierro.infrastructure.exception.custom;

import org.springframework.http.HttpStatus;

public class BadRequestException extends DiPierroException {

    public BadRequestException(String errorCode, String message) {
        super(HttpStatus.BAD_REQUEST, errorCode, message);
    }
}

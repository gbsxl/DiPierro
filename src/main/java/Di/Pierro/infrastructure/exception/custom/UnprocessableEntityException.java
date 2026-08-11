package Di.Pierro.infrastructure.exception.custom;

import org.springframework.http.HttpStatus;

public class UnprocessableEntityException extends DiPierroException {

    public UnprocessableEntityException(String errorCode, String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, errorCode, message);
    }
}

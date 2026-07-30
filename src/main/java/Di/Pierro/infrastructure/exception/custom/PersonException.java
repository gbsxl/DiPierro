package Di.Pierro.infrastructure.exception.custom;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Getter
public class PersonException extends RuntimeException {
    private HttpStatus httpStatus;
    private String error;

    public PersonException(HttpStatus httpStatus, String error, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.error = error;
    }
}

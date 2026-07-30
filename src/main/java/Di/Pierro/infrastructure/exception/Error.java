package Di.Pierro.infrastructure.exception;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.List;

public record Error(
        HttpStatus httpStatus,
        String error,
        String message,
        String path,
        List<?> errorList,
        Timestamp timestamp
) {
}

package Di.Pierro.infrastructure.exception;

import Di.Pierro.infrastructure.exception.custom.PersonException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> beanValidationExceptionHandler(MethodArgumentNotValidException exception, HttpServletRequest request){
        BeanValidationErrorResponse beanValidationErrorResponse = new BeanValidationErrorResponse();

        exception.getBindingResult().getFieldErrors().forEach(
        fieldError -> {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            beanValidationErrorResponse.addError(new BeanValidationError(field, message));
        });

        List<String> errorProperties = new ArrayList<>();

        beanValidationErrorResponse.getErrors().forEach(
                error -> errorProperties.add(error.getProperty())
        );

        Error error = new Error(
                HttpStatus.BAD_REQUEST,
                "Bean Validation Error",
                "Property(s):" + errorProperties + " has violations",
                request.getRequestURI() + "(" + request.getMethod() + ")",
                beanValidationErrorResponse.errors,
                Timestamp.from(Instant.now())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler
    public ResponseEntity<?> personExceptionHandler(PersonException exception, HttpServletRequest request){
        Error error = new Error(
                exception.getHttpStatus(),
                exception.getError(),
                exception.getMessage(),
                request.getRequestURI() + "(" + request.getMethod() + ")",
                List.of(),
                Timestamp.from(Instant.now())
        );

        return ResponseEntity.status(error.httpStatus()).body(error);
    }





}

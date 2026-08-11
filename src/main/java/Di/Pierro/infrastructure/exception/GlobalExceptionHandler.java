package Di.Pierro.infrastructure.exception;

import Di.Pierro.infrastructure.exception.custom.DiPierroException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DiPierroException.class)
    public ProblemDetail handleBusinessException(DiPierroException ex, HttpServletRequest req) {
        log.warn("Business error [{} {}] code='{}' message='{}'",
                req.getMethod(), req.getRequestURI(), ex.getErrorCode(), ex.getMessage());
        ProblemDetail pd = ApiExceptionUtils.build(ex.getStatus(), ex.getStatus().getReasonPhrase(), ex.getMessage(), req);
        pd.setProperty("errorCode", ex.getErrorCode());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "invalid value",
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
        log.warn("Validation failure [{} {}]: {}", req.getMethod(), req.getRequestURI(), fieldErrors);
        ProblemDetail pd = ApiExceptionUtils.build(HttpStatus.BAD_REQUEST, "Validation Error", "One or more fields are invalid.", req);
        pd.setProperty("errors", fieldErrors);
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, String> violations = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> {
                            String path = v.getPropertyPath().toString();
                            int lastDot = path.lastIndexOf('.');
                            return lastDot >= 0 ? path.substring(lastDot + 1) : path;
                        },
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
        log.warn("Constraint violation [{} {}]: {}", req.getMethod(), req.getRequestURI(), violations);
        ProblemDetail pd = ApiExceptionUtils.build(HttpStatus.BAD_REQUEST, "Invalid Parameters", "One or more request parameters are invalid.", req);
        pd.setProperty("errors", violations);
        return pd;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        log.warn("Unreadable request body [{} {}]: {}", req.getMethod(), req.getRequestURI(), ex.getMessage());
        return ApiExceptionUtils.build(HttpStatus.BAD_REQUEST, "Invalid Request Body", "The request body is missing, malformed, or contains incompatible types.", req);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public ProblemDetail handleBadParams(Exception ex, HttpServletRequest req) {
        log.warn("Bad request parameter [{} {}]: {}", req.getMethod(), req.getRequestURI(), ex.getMessage());
        return ApiExceptionUtils.build(HttpStatus.BAD_REQUEST, "Invalid Parameter", ex.getMessage(), req);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        log.warn("Method not allowed [{} {}]: {}", req.getMethod(), req.getRequestURI(), ex.getMessage());
        return ApiExceptionUtils.build(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed", ex.getMessage(), req);
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ProblemDetail handleNotFound(Exception ex, HttpServletRequest req) {
        log.warn("Route or resource not found [{} {}]: {}", req.getMethod(), req.getRequestURI(), ex.getMessage());
        ProblemDetail pd = ApiExceptionUtils.build(HttpStatus.NOT_FOUND, "Resource Not Found", "The requested route or resource does not exist on this server.", req);
        pd.setProperty("invalidPath", req.getRequestURI());
        return pd;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        log.error("Data integrity violation [{} {}]", req.getMethod(), req.getRequestURI(), ex);
        return ApiExceptionUtils.build(HttpStatus.CONFLICT, "Data Conflict", "The operation violates an integrity constraint (duplicate record or invalid reference).", req);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ProblemDetail handleWebClient(WebClientResponseException ex, HttpServletRequest req) {
        log.error("External service error [{} {}] status={} body='{}'",
                req.getMethod(), req.getRequestURI(), ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
        return ApiExceptionUtils.build(HttpStatus.BAD_GATEWAY, "External Service Error", "Failed to communicate with an external service.", req);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception [{} {}]", req.getMethod(), req.getRequestURI(), ex);
        return ApiExceptionUtils.build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred. If the problem persists, contact support with the traceId.", req);
    }
}

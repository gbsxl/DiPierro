package Di.Pierro.infrastructure.exception.custom;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends DiPierroException {

    public ResourceNotFoundException(String errorCode, String message) {
        super(HttpStatus.NOT_FOUND, errorCode, message);
    }

    public static ResourceNotFoundException of(String errorCode, String entity, Object id) {
        return new ResourceNotFoundException(errorCode, entity + " not found with id: " + id);
    }
}

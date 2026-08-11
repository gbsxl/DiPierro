package Di.Pierro.infrastructure.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BeanValidationErrorResponse {
    List<BeanValidationError> errors;

    public BeanValidationErrorResponse(){
        this.errors = new ArrayList<>();
    }

    public void addError(BeanValidationError error){
        this.errors.add(error);
    }

}

package Di.Pierro.application.validators.cnpj;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CnpjValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CNPJ {
    String message() default "Invalid CNPJ";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

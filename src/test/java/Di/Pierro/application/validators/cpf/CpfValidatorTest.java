package Di.Pierro.application.validators.cpf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpfValidatorTest {

    private CpfValidator cpfValidator;

    @BeforeEach
    void setUp() {
        cpfValidator = new CpfValidator();
    }

    @Test
    void shouldValidateMaskedCpfWithAsterisks() {
        assertTrue(cpfValidator.isValid("123****8901", null));
        assertTrue(cpfValidator.isValid("109****4321", null));
        assertTrue(cpfValidator.isValid("456****1230", null));
    }

    @Test
    void shouldRejectInvalidLength() {
        assertFalse(cpfValidator.isValid("123*8901", null));
        assertFalse(cpfValidator.isValid("123456789012", null));
    }
}

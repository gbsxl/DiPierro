package Di.Pierro.application.port.input.person;

import Di.Pierro.application.useCases.person.CreatePersonInput;

public interface CreatePersonUseCase {
    void execute(CreatePersonInput personInput);
}

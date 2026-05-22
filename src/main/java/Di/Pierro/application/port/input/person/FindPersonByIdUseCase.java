package Di.Pierro.application.port.input.person;

import Di.Pierro.domain.model.Person;

import java.util.Optional;
import java.util.UUID;

public interface FindPersonByIdUseCase {
    Optional<Person> execute(UUID id);
}

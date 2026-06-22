package Di.Pierro.application.port.input;

import Di.Pierro.application.dto.person.CreatePersonInput;
import Di.Pierro.domain.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonUseCases {
    void createPerson(CreatePersonInput createPersonInput);
    List<Person> findAll();
    Optional<Person> findById(UUID id);
}

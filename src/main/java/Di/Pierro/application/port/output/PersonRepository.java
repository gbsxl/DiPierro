package Di.Pierro.application.port.output;

import Di.Pierro.domain.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository {
    void save(Person person, UUID actorId);
    Optional<Person> findPersonById(UUID id);
    List<Person> findAllPersons();
}

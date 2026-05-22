package Di.Pierro.application.port.output.person;

import Di.Pierro.domain.entity.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository {
    void save(Person person, UUID actorId);
    Optional<Person> findPersonById(UUID id);
    List<Person> findAllPersons();
}

package Di.Pierro.application.port.output;

import Di.Pierro.application.dto.person.CreatePersonInput;
import Di.Pierro.domain.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository {
    void save(Person person);
    Optional<Person> findById(UUID id);
    List<Person> findAll();
    List<Person> findAllByIds(List<UUID> ids);
    Optional<Person> findByActorId(UUID id);
    List<Person> findByActorIds(List<UUID> actorIds);
    List<Person> findByCompleteName(String string);
    List<Person> findByCPF(String string);
    List<Person> findByGender(String string);
    List<Person> findByEmail(String string);
    //TODO Optional<Person> findByCEP(String string);
    Person updateById(UUID id, CreatePersonInput person);
    void deleteById(UUID id);
}

package Di.Pierro.infrastructure.persistence.person;

import Di.Pierro.application.port.output.PersonRepository;
import Di.Pierro.domain.model.Person;
import Di.Pierro.infrastructure.mapper.PersonMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaPersonRepositoryAdapter implements PersonRepository {

    private final JpaPersonRepository jpaPersonRepository;
    private final PersonMapper personMapper;

    @Override
    public void save(Person person) {
        jpaPersonRepository.save(personMapper.toEntity(person));
    }

    @Override
    public Optional<Person> findById(UUID id) {
        return jpaPersonRepository.findById(id).map(personMapper::toDomain);
    }

    @Override
    public List<Person> findAll() {
        return jpaPersonRepository.findAll().stream().map(personMapper::toDomain).toList();
    }
}

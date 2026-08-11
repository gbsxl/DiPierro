package Di.Pierro.infrastructure.persistence.person;

import Di.Pierro.application.dto.person.CreatePersonInput;
import Di.Pierro.application.port.output.PersonRepository;
import Di.Pierro.domain.enums.Gender;
import Di.Pierro.domain.model.Person;
import Di.Pierro.infrastructure.exception.custom.BadRequestException;
import Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException;
import Di.Pierro.infrastructure.mapper.PersonMapper;
import Di.Pierro.infrastructure.persistence.entity.PersonEntity;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaPersonRepositoryAdapter implements PersonRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaPersonRepositoryAdapter.class);

    private final JpaPersonRepository jpaPersonRepository;
    private final PersonMapper personMapper;

    @Override
    public void save(Person person) {
        log.debug("Saving person with CPF ending in ...{}", safeSuffix(person.getCpf()));
        jpaPersonRepository.save(personMapper.toEntity(person));
    }

    @Override
    public Optional<Person> findById(UUID id) {
        log.debug("Looking up person by id={}", id);
        return jpaPersonRepository.findById(id).map(personMapper::toDomain);
    }

    @Override
    public List<Person> findAll() {
        log.debug("Fetching all persons");
        return jpaPersonRepository.findAll().stream().map(personMapper::toDomain).toList();
    }

    @Override
    public Optional<Person> findByActorId(UUID id) {
        log.debug("Looking up person by actorId={}", id);
        return jpaPersonRepository.findByActorId(id).map(personMapper::toDomain);
    }

    @Override
    public List<Person> findByEmail(String string) {
        if (string == null || string.trim().length() < 3) {
            throw new BadRequestException("person.email-too-short", "Search term for email must have at least 3 characters.");
        }
        log.debug("Searching persons by email containing '{}'", string.trim());
        return map(jpaPersonRepository.findTop100ByEmailContainingIgnoreCase(string.trim()));
    }

    @Override
    public List<Person> findByCompleteName(String string) {
        if (string == null || string.trim().length() < 3) {
            throw new BadRequestException("person.name-too-short", "Search term for name must have at least 3 characters.");
        }
        log.debug("Searching persons by name containing '{}'", string.trim());
        return map(jpaPersonRepository.findTop100ByCompleteNameContainingIgnoreCase(string.trim()));
    }

    @Override
    public List<Person> findByCPF(String string) {
        if (string == null || string.isBlank()) {
            throw new BadRequestException("person.cpf-blank", "CPF cannot be blank.");
        }

        String sqlPattern = string.replaceAll("[Xx*?-]", "_");

        if (sqlPattern.length() < 11 && !sqlPattern.contains("_")) {
            sqlPattern = sqlPattern + "%";
        }

        if (sqlPattern.equals("___________") || sqlPattern.equals("%")) {
            throw new BadRequestException("person.cpf-invalid-wildcard", "CPF cannot be a full wildcard pattern.");
        }

        log.debug("Searching persons by CPF pattern '{}'", sqlPattern);
        return map(jpaPersonRepository.findTop100ByCpfLike(sqlPattern));
    }

    @Override
    public List<Person> findByGender(String string) {
        Gender gender = Gender.from(string);
        if (gender.isUndefined()) {
            throw new BadRequestException("person.gender-undefined", "Gender value was not recognized.");
        }
        log.debug("Searching persons by gender={}", gender);
        return map(jpaPersonRepository.findByGender(gender));
    }

    @Override
    public Person updateById(UUID id, CreatePersonInput person) {
        PersonEntity entity = jpaPersonRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("person.not-found", "Person", id));

        entity.setCompleteName(person.completeName());
        entity.setCpf(person.cpf());
        entity.setAddress(person.address());
        entity.setGender(person.gender());
        entity.setPhoneNumber(person.phoneNumber());
        entity.setEmail(person.email());

        log.debug("Updating person id={}", id);
        jpaPersonRepository.save(entity);
        return personMapper.toDomain(entity);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting person id={}", id);
        jpaPersonRepository.deleteById(id);
    }

    private List<Person> map(List<PersonEntity> entities) {
        return entities.stream().map(personMapper::toDomain).toList();
    }

    private String safeSuffix(String value) {
        if (value == null || value.length() < 4) return "****";
        return value.substring(value.length() - 4);
    }
}

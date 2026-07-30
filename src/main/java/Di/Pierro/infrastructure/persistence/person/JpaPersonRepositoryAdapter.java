package Di.Pierro.infrastructure.persistence.person;

import Di.Pierro.application.dto.person.CreatePersonInput;
import Di.Pierro.application.port.output.PersonRepository;
import Di.Pierro.domain.enums.Gender;
import Di.Pierro.domain.model.Person;
import Di.Pierro.infrastructure.exception.custom.PersonException;
import Di.Pierro.infrastructure.mapper.PersonMapper;
import Di.Pierro.infrastructure.persistence.entity.PersonEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Override
    public Optional<Person> findByActorId(UUID id) {
        return jpaPersonRepository.findByActorId(id).map(personMapper::toDomain);
    }

    @Override
    public List<Person> findByEmail(String string) {
        if (string == null || string.trim().length() < 3) {
            throw new PersonException(
                    HttpStatus.BAD_REQUEST,
                    "invalid email",
                    "email is null or too small"
            );
        }

        return map(jpaPersonRepository.findTop100ByEmailContainingIgnoreCase(string.trim()));
    }

    @Override
    public List<Person> findByCompleteName(String string) {
        if (string == null || string.trim().length() < 3) {
            throw new PersonException(
                    HttpStatus.BAD_REQUEST,
                    "invalid name",
                    "name is null or too small"
            );
        }

        return map(jpaPersonRepository.findTop100ByCompleteNameContainingIgnoreCase(string.trim()));
    }
    @Override
    public List<Person> findByCPF(String string) {
        if (string == null || string.isBlank()) {
            throw new PersonException(
                    HttpStatus.BAD_REQUEST,
                    "invalid cpf",
                    "cpf is null or too small"
            );
        }

        String sqlPattern = string.replaceAll("[Xx*?-]", "_");

        if (sqlPattern.length() < 11 && !sqlPattern.contains("_")) {
            sqlPattern = sqlPattern + "%";
        }

        if (sqlPattern.equals("___________") || sqlPattern.equals("%")) {
            throw new PersonException(
                    HttpStatus.BAD_REQUEST,
                    "invalid cpf",
                    "cpf incorrect format, this field cannot be " + string
            );
        }

        return map(jpaPersonRepository.findTop100ByCpfLike(sqlPattern));
    }

    @Override
    public List<Person> findByGender(String string) {
        Gender gender = Gender.from(string);

        if(gender.isUndefined()) throw new PersonException(
                HttpStatus.BAD_REQUEST,
                "invalid gender",
                "genre was not understood"
        );

        return map(jpaPersonRepository.findByGender(gender));
    }

    @Override
    public Person updateById(UUID id, CreatePersonInput person) {
        Optional<PersonEntity> personOriginal = jpaPersonRepository.findById(id);
        personOriginal.ifPresent(value -> value.setCompleteName(person.completeName()));
        personOriginal.ifPresent(value -> value.setCpf(person.cpf()));
        personOriginal.ifPresent(value -> value.setAddress(person.address()));
        personOriginal.ifPresent(value -> value.setGender(person.gender()));
        personOriginal.ifPresent(value -> value.setPhoneNumber(person.phoneNumber()));
        personOriginal.ifPresent(value -> value.setEmail(person.email()));

        if(personOriginal.isPresent()){
            jpaPersonRepository.save(personOriginal.get());
            return personMapper.toDomain(personOriginal.get());
        }

        throw new PersonException(
                HttpStatus.NOT_FOUND,
                "person not found",
                "id not found"
        );
    }

    @Override
    public void deleteById(UUID id) {
        jpaPersonRepository.deleteById(id);
    }
    private List<Person> map(List<PersonEntity> entities){
        return entities.stream()
                .map(personMapper::toDomain)
                .toList();
    }
}

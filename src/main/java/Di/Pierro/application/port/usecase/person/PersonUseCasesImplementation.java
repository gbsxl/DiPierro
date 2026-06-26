package Di.Pierro.application.port.usecase.person;

import Di.Pierro.application.dto.person.CreatePersonInput;
import Di.Pierro.application.port.input.PersonUseCases;
import Di.Pierro.application.port.output.PersonRepository;
import Di.Pierro.domain.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PersonUseCasesImplementation implements PersonUseCases {
    PersonRepository personRepository;

    public PersonUseCasesImplementation(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void createPerson(CreatePersonInput createPersonInput) {
        Person person = Person.createPerson(
                createPersonInput.completeName(),
                createPersonInput.cpf(),
                createPersonInput.address(),
                createPersonInput.gender(),
                createPersonInput.phoneNumber(),
                createPersonInput.email()
        );
        personRepository.save(person, createPersonInput.actorId());
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> findById(UUID id) {
        return personRepository.findById(id);
    }
}

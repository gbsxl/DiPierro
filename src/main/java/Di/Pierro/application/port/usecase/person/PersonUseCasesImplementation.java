package Di.Pierro.application.port.usecase.person;

import Di.Pierro.application.dto.person.CreatePersonInput;
import Di.Pierro.application.port.input.ActorUseCases;
import Di.Pierro.application.port.input.PersonUseCases;
import Di.Pierro.application.port.output.PersonRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PersonUseCasesImplementation implements PersonUseCases {
    private final PersonRepository personRepository;
    private final ActorUseCases actorUseCases;

    public PersonUseCasesImplementation(PersonRepository personRepository, ActorUseCases actorUseCases) {
        this.personRepository = personRepository;
        this.actorUseCases = actorUseCases;
    }

    @Override
    public void createPerson(CreatePersonInput createPersonInput) {
        Actor actor = actorUseCases.createActor();

        Person person = new Person(
                createPersonInput.completeName(),
                createPersonInput.cpf(),
                createPersonInput.address(),
                createPersonInput.gender(),
                createPersonInput.phoneNumber(),
                createPersonInput.email(),
                actor
        );
        personRepository.save(person);
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> findById(UUID id) {
        return personRepository.findById(id);
    }

    @Override
    public Optional<Person> findByActorId(UUID id) {
        return personRepository.findByActorId(id);
    }

    @Override
    public List<Person> findByCompleteName(String string) {
        return personRepository.findByCompleteName(string);
    }

    @Override
    public List<Person> findByCPF(String string) {
        return personRepository.findByCPF(string);
    }

    @Override
    public List<Person> findByGender(String string) {
        return personRepository.findByGender(string);
    }

    @Override
    public List<Person> findByEmail(String string) {
        return personRepository.findByEmail(string);
    }

    @Override
    public Person updateById(UUID id, CreatePersonInput person) {
        return personRepository.updateById(id, person);
    }

    @Override
    public void deleteById(UUID id) {
        personRepository.deleteById(id);
    }
}

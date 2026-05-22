package Di.Pierro.application.useCases.person;

import Di.Pierro.application.port.input.person.CreatePersonUseCase;
import Di.Pierro.application.port.output.person.PersonRepository;
import Di.Pierro.domain.model.Person;

public class CreatePersonImplementation implements CreatePersonUseCase {

    PersonRepository personRepository;

    public CreatePersonImplementation(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void execute(CreatePersonInput personInput) {
        Person person = new Person(
                personInput.completeName(),
                personInput.cpf(),
                personInput.gender(),
                personInput.phoneNumber(),
                personInput.email()
        );
        personRepository.save(person, personInput.actorId());
    }
}

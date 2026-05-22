package Di.Pierro.application.usecase.person;

import Di.Pierro.application.port.output.PersonRepository;
import Di.Pierro.application.dto.person.CreatePersonInput;
import Di.Pierro.domain.model.Person;

public class CreatePersonUseCase {

    PersonRepository personRepository;

    public CreatePersonUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

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

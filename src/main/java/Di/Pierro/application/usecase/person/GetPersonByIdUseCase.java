package Di.Pierro.application.usecase.person;

import Di.Pierro.application.port.output.PersonRepository;
import Di.Pierro.domain.model.Person;

import java.util.Optional;
import java.util.UUID;

public class GetPersonByIdUseCase {

    PersonRepository personRepository;

    public GetPersonByIdUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Optional<Person> execute(UUID id) {
        return personRepository.findById(id);
    }
}

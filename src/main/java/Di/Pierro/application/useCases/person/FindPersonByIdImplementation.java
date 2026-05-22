package Di.Pierro.application.useCases.person;

import Di.Pierro.application.port.input.person.FindPersonByIdUseCase;
import Di.Pierro.application.port.output.person.PersonRepository;
import Di.Pierro.domain.entity.Person;

import java.util.Optional;
import java.util.UUID;

public class FindPersonByIdImplementation implements FindPersonByIdUseCase {

    PersonRepository personRepository;

    public FindPersonByIdImplementation(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Optional<Person> execute(UUID id) {
        return personRepository.findPersonById(id);
    }
}

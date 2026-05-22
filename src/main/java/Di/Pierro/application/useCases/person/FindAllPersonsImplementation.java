package Di.Pierro.application.useCases.person;

import Di.Pierro.application.port.input.person.FindAllPersonsUseCase;
import Di.Pierro.application.port.output.person.PersonRepository;
import Di.Pierro.domain.model.Person;

import java.util.List;

public class FindAllPersonsImplementation implements FindAllPersonsUseCase {

    PersonRepository personRepository;

    public FindAllPersonsImplementation(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> execute() {
        return personRepository.findAllPersons();
    }
}

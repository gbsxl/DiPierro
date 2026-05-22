package Di.Pierro.application.usecase.person;

import Di.Pierro.application.port.output.PersonRepository;
import Di.Pierro.domain.model.Person;

import java.util.List;

public class FindAllPersonsUseCase {

    PersonRepository personRepository;

    public FindAllPersonsUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> execute() {
        return personRepository.findAllPersons();
    }
}

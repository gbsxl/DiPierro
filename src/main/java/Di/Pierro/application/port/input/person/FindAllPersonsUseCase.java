package Di.Pierro.application.port.input.person;

import Di.Pierro.domain.model.Person;

import java.util.List;

public interface FindAllPersonsUseCase {
    List<Person> execute();
}

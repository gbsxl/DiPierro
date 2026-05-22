package Di.Pierro.infrastructure.config;

import Di.Pierro.application.port.input.person.CreatePersonUseCase;
import Di.Pierro.application.port.input.person.FindAllPersonsUseCase;
import Di.Pierro.application.port.input.person.FindPersonByIdUseCase;
import Di.Pierro.application.port.output.person.PersonRepository;
import Di.Pierro.application.useCases.person.CreatePersonImplementation;
import Di.Pierro.application.useCases.person.FindAllPersonsImplementation;
import Di.Pierro.application.useCases.person.FindPersonByIdImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonConfig {

    @Bean
    public CreatePersonUseCase createPersonUseCase(PersonRepository personRepository){
        return new CreatePersonImplementation(personRepository);
    }

    @Bean
    public FindPersonByIdUseCase findPersonByIdUseCase(PersonRepository personRepository){
        return new FindPersonByIdImplementation(personRepository);
    }

    @Bean
    public FindAllPersonsUseCase findAllPersonsUseCase(PersonRepository personRepository){
        return new FindAllPersonsImplementation(personRepository);
    }

}

package Di.Pierro.infrastructure.config;

import Di.Pierro.application.port.output.PersonRepository;
import Di.Pierro.application.usecase.person.CreatePersonUseCase;
import Di.Pierro.application.usecase.person.FindAllPersonsUseCase;
import Di.Pierro.application.usecase.person.FindPersonByIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonConfig {

    @Bean
    public Di.Pierro.application.port.input.person.CreatePersonUseCase createPersonUseCase(PersonRepository personRepository){
        return new CreatePersonUseCase(personRepository);
    }

    @Bean
    public Di.Pierro.application.port.input.person.FindPersonByIdUseCase findPersonByIdUseCase(PersonRepository personRepository){
        return new FindPersonByIdUseCase(personRepository);
    }

    @Bean
    public Di.Pierro.application.port.input.person.FindAllPersonsUseCase findAllPersonsUseCase(PersonRepository personRepository){
        return new FindAllPersonsUseCase(personRepository);
    }

}

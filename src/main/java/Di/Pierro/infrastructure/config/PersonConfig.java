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
    public CreatePersonUseCase createPersonUseCase(PersonRepository personRepository){
        return new CreatePersonUseCase(personRepository);
    }

    @Bean
    public FindPersonByIdUseCase findPersonByIdUseCase(PersonRepository personRepository){
        return new FindPersonByIdUseCase(personRepository);
    }

    @Bean
    public FindAllPersonsUseCase findAllPersonsUseCase(PersonRepository personRepository){
        return new FindAllPersonsUseCase(personRepository);
    }

}

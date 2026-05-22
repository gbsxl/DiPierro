package Di.Pierro.infrastructure.config;

import Di.Pierro.application.port.output.PersonRepository;
import Di.Pierro.application.usecase.person.CreatePersonUseCase;
import Di.Pierro.application.usecase.person.SearchPersonUseCase;
import Di.Pierro.application.usecase.person.GetPersonByIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonBeanConfig {

    @Bean
    public CreatePersonUseCase createPersonUseCase(PersonRepository personRepository){
        return new CreatePersonUseCase(personRepository);
    }

    @Bean
    public GetPersonByIdUseCase findPersonByIdUseCase(PersonRepository personRepository){
        return new GetPersonByIdUseCase(personRepository);
    }

    @Bean
    public SearchPersonUseCase findAllPersonsUseCase(PersonRepository personRepository){
        return new SearchPersonUseCase(personRepository);
    }

}

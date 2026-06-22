package Di.Pierro.infrastructure.config;

import Di.Pierro.application.port.output.*;
import Di.Pierro.application.port.usecase.actor.ActorUseCasesImplementation;
import Di.Pierro.application.port.usecase.association.AssociationUseCasesImplementation;
import Di.Pierro.application.port.usecase.business.BusinessUseCasesImplementation;
import Di.Pierro.application.port.usecase.person.PersonUseCasesImplementation;
import Di.Pierro.application.port.usecase.transaction.TransactionUseCasesImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesBeanConfig {

    @Bean
    public ActorUseCasesImplementation actorUseCasesImplementation(ActorRepository actorRepository) {
        return new ActorUseCasesImplementation(actorRepository);
    }

    @Bean
    public AssociationUseCasesImplementation associationUseCasesImplementation(AssociationRepository associationRepository) {
        return new AssociationUseCasesImplementation(associationRepository);
    }

    @Bean
    public BusinessUseCasesImplementation businessUseCasesImplementation(BusinessRepository businessRepository) {
        return new BusinessUseCasesImplementation(businessRepository);
    }

    @Bean
    public PersonUseCasesImplementation personUseCasesImplementation(PersonRepository personRepository) {
        return new PersonUseCasesImplementation(personRepository);
    }

    @Bean
    public TransactionUseCasesImplementation transactionUseCasesImplementation(TransactionRepository transactionRepository) {
        return new TransactionUseCasesImplementation(transactionRepository);
    }
}

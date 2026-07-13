package Di.Pierro.infrastructure.config;

import Di.Pierro.application.port.output.*;
import Di.Pierro.application.port.usecase.actorindicator.ActorIndicatorUseCasesImplementation;
import Di.Pierro.application.port.usecase.actor.ActorUseCasesImplementation;
import Di.Pierro.application.port.usecase.asset.AssetUseCasesImplementation;
import Di.Pierro.application.port.usecase.association.AssociationUseCasesImplementation;
import Di.Pierro.application.port.usecase.business.BusinessUseCasesImplementation;
import Di.Pierro.application.port.usecase.document.DocumentUseCasesImplementation;
import Di.Pierro.application.port.usecase.documentmention.DocumentMentionUseCasesImplementation;
import Di.Pierro.application.port.usecase.person.PersonUseCasesImplementation;
import Di.Pierro.application.port.usecase.publicprocurement.PublicProcurementUseCasesImplementation;
import Di.Pierro.application.port.usecase.redflag.RedFlagUseCasesImplementation;
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

    @Bean
    public PublicProcurementUseCasesImplementation publicProcurementUseCasesImplementation(PublicProcurementRepository publicProcurementRepository) {
        return new PublicProcurementUseCasesImplementation(publicProcurementRepository);
    }

    @Bean
    public DocumentUseCasesImplementation documentUseCasesImplementation(DocumentRepository documentRepository) {
        return new DocumentUseCasesImplementation(documentRepository);
    }

    @Bean
    public DocumentMentionUseCasesImplementation documentMentionUseCasesImplementation(DocumentMentionRepository documentMentionRepository) {
        return new DocumentMentionUseCasesImplementation(documentMentionRepository);
    }

    @Bean
    public AssetUseCasesImplementation assetUseCasesImplementation(AssetRepository assetRepository) {
        return new AssetUseCasesImplementation(assetRepository);
    }

    @Bean
    public ActorIndicatorUseCasesImplementation actorIndicatorUseCasesImplementation(ActorIndicatorRepository actorIndicatorRepository) {
        return new ActorIndicatorUseCasesImplementation(actorIndicatorRepository);
    }

    @Bean
    public RedFlagUseCasesImplementation redFlagUseCasesImplementation(RedFlagRepository redFlagRepository) {
        return new RedFlagUseCasesImplementation(redFlagRepository);
    }
}


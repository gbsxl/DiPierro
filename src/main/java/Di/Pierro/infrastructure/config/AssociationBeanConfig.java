package Di.Pierro.infrastructure.config;

import Di.Pierro.application.port.output.AssociationRepository;
import Di.Pierro.application.usecase.association.CreateAssociationUseCase;
import Di.Pierro.application.usecase.association.GetAssociationByIdUseCase;
import Di.Pierro.application.usecase.association.SearchAssociationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssociationBeanConfig {
    @Bean
    public CreateAssociationUseCase associationUseCase(AssociationRepository associationRepository){
        return new CreateAssociationUseCase(associationRepository);
    }

    @Bean
    public GetAssociationByIdUseCase getAssociationByIdUseCase(AssociationRepository associationRepository){
        return new GetAssociationByIdUseCase(associationRepository);
    }

    @Bean
    public SearchAssociationUseCase searchAssociationUseCase(AssociationRepository associationRepository){
        return new SearchAssociationUseCase(associationRepository);
    }
}

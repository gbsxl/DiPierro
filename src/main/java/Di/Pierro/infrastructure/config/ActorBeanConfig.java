package Di.Pierro.infrastructure.config;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.usecase.actor.CreateActorUseCase;
import Di.Pierro.application.usecase.actor.GetActorByIdUseCase;
import Di.Pierro.application.usecase.actor.SearchActorUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActorBeanConfig {
    @Bean
    public CreateActorUseCase createActorUseCase(ActorRepository actorRepository) {
        return new CreateActorUseCase(actorRepository);
    }

    @Bean
    public GetActorByIdUseCase findActorByIdUseCase(ActorRepository actorRepository){
        return new GetActorByIdUseCase(actorRepository);
    }

    @Bean
    public SearchActorUseCase findAllActorsUseCase(ActorRepository actorRepository){
        return new SearchActorUseCase(actorRepository);
    }

}


package Di.Pierro.infrastructure.config;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.usecase.actor.CreateActorUseCase;
import Di.Pierro.application.usecase.actor.FindActorByIdUseCase;
import Di.Pierro.application.usecase.actor.FindAllActorsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActorConfig {
    @Bean
    public CreateActorUseCase createActorUseCase(ActorRepository actorRepository) {
        return new CreateActorUseCase(actorRepository);
    }

    @Bean
    public FindActorByIdUseCase findActorByIdUseCase(ActorRepository actorRepository){
        return new FindActorByIdUseCase(actorRepository);
    }

    @Bean
    public FindAllActorsUseCase findAllActorsUseCase(ActorRepository actorRepository){
        return new FindAllActorsUseCase(actorRepository);
    }

}


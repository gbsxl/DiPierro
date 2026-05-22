package Di.Pierro.infrastructure.config;

import Di.Pierro.application.port.input.actor.CreateActorUseCase;
import Di.Pierro.application.port.input.actor.FindActorByIdUseCase;
import Di.Pierro.application.port.input.actor.FindAllActorsUseCase;
import Di.Pierro.application.port.output.actor.ActorRepository;
import Di.Pierro.application.useCases.actor.CreateActorImplementation;
import Di.Pierro.application.useCases.actor.FindActorByIdImplementation;
import Di.Pierro.application.useCases.actor.FindAllActorsImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActorConfig {
    @Bean
    public CreateActorUseCase createActorUseCase(ActorRepository actorRepository) {
        return new CreateActorImplementation(actorRepository);
    }

    @Bean
    public FindActorByIdUseCase findActorByIdUseCase(ActorRepository actorRepository){
        return new FindActorByIdImplementation(actorRepository);
    }

    @Bean
    public FindAllActorsUseCase findAllActorsUseCase(ActorRepository actorRepository){
        return new FindAllActorsImplementation(actorRepository);
    }

}


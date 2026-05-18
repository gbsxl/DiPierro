package Di.Pierro.infrastructure.config;

import Di.Pierro.application.port.input.CreateActorUseCase;
import Di.Pierro.application.port.input.FindActorByIdUseCase;
import Di.Pierro.application.port.input.FindAllActorsUseCase;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.useCases.CreateActorImplementation;
import Di.Pierro.application.useCases.FindActorByIdImplementation;
import Di.Pierro.application.useCases.FindAllActorsImplementation;
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


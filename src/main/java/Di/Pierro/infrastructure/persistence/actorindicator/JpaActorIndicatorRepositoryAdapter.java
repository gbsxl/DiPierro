package Di.Pierro.infrastructure.persistence.actorindicator;

import Di.Pierro.application.dto.actorindicator.CreateActorIndicatorInput;
import Di.Pierro.application.port.output.ActorIndicatorRepository;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.ActorIndicator;
import Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException;
import Di.Pierro.infrastructure.mapper.ActorIndicatorMapper;
import Di.Pierro.infrastructure.persistence.entity.ActorIndicatorEntity;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaActorIndicatorRepositoryAdapter implements ActorIndicatorRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaActorIndicatorRepositoryAdapter.class);

    private final ActorRepository actorRepository;
    private final JpaActorIndicatorRepository jpaActorIndicatorRepository;
    private final ActorIndicatorMapper actorIndicatorMapper;

    @Override
    public void save(ActorIndicator actorIndicator, UUID actorId) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> ResourceNotFoundException.of("actor-indicator.actor-not-found", "Actor", actorId));
        actorIndicator.setActor(actor);
        log.debug("Saving actor indicator for actorId={}", actorId);
        jpaActorIndicatorRepository.save(actorIndicatorMapper.toEntity(actorIndicator));
    }

    @Override
    public Optional<ActorIndicator> findById(UUID id) {
        log.debug("Looking up actor indicator by id={}", id);
        return jpaActorIndicatorRepository.findById(id).map(actorIndicatorMapper::toDomain);
    }

    @Override
    public List<ActorIndicator> findAll() {
        log.debug("Fetching all actor indicators");
        return jpaActorIndicatorRepository.findAll().stream().map(actorIndicatorMapper::toDomain).toList();
    }

    @Override
    public List<ActorIndicator> findByActorId(UUID id) {
        log.debug("Searching actor indicators by actorId={}", id);
        return map(jpaActorIndicatorRepository.findByActorId(id));
    }

    @Override
    public List<ActorIndicator> findByIndicatorType(String string) {
        if (string == null || string.isBlank()) {
            return List.of();
        }
        log.debug("Searching actor indicators by type containing '{}'", string.trim());
        return map(jpaActorIndicatorRepository.findTop100ByIndicatorTypeContainingIgnoreCase(string.trim()));
    }

    @Override
    public ActorIndicator updateById(UUID id, CreateActorIndicatorInput actorIndicator) {
        ActorIndicatorEntity entity = jpaActorIndicatorRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("actor-indicator.not-found", "ActorIndicator", id));

        entity.setIndicatorType(actorIndicator.indicatorType());
        entity.setValue(actorIndicator.value());
        entity.setSource(actorIndicator.source());
        entity.setDate(actorIndicator.date());

        log.debug("Updating actor indicator id={}", id);
        jpaActorIndicatorRepository.save(entity);
        return actorIndicatorMapper.toDomain(entity);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting actor indicator id={}", id);
        jpaActorIndicatorRepository.deleteById(id);
    }

    private List<ActorIndicator> map(List<ActorIndicatorEntity> entities) {
        return entities.stream().map(actorIndicatorMapper::toDomain).toList();
    }
}

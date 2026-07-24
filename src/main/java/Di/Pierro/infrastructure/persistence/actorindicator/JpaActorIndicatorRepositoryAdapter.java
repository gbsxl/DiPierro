package Di.Pierro.infrastructure.persistence.actorindicator;

import Di.Pierro.application.dto.actorindicator.CreateActorIndicatorInput;
import Di.Pierro.application.port.output.ActorIndicatorRepository;
import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.ActorIndicator;
import Di.Pierro.infrastructure.mapper.ActorIndicatorMapper;
import Di.Pierro.infrastructure.persistence.entity.ActorIndicatorEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaActorIndicatorRepositoryAdapter implements ActorIndicatorRepository {
    private final ActorRepository actorRepository;
    private final JpaActorIndicatorRepository jpaActorIndicatorRepository;
    private final ActorIndicatorMapper actorIndicatorMapper;

    @Override
    public void save(ActorIndicator actorIndicator, UUID actorId) {
        Optional<Actor> optionalActor = actorRepository.findById(actorId);
        if (optionalActor.isPresent()) {
            actorIndicator.setActor(optionalActor.get());
            jpaActorIndicatorRepository.save(actorIndicatorMapper.toEntity(actorIndicator));
        }
    }

    @Override
    public Optional<ActorIndicator> findById(UUID id) {
        return jpaActorIndicatorRepository.findById(id).map(actorIndicatorMapper::toDomain);
    }

    @Override
    public List<ActorIndicator> findAll() {
        return jpaActorIndicatorRepository.findAll().stream().map(actorIndicatorMapper::toDomain).toList();
    }

    @Override
    public List<ActorIndicator> findByActorId(UUID id) {
        return map(jpaActorIndicatorRepository.findByActorId(id));
    }

    @Override
    public List<ActorIndicator> findByIndicatorType(String string) {
        if (string == null || string.isBlank()) {
            return List.of();
        }

        return map(jpaActorIndicatorRepository.findTop100ByIndicatorTypeContainingIgnoreCase(string.trim()));
    }

    @Override
    public ActorIndicator updateById(UUID id, CreateActorIndicatorInput actorIndicator) {
        Optional<ActorIndicatorEntity> original = jpaActorIndicatorRepository.findById(id);
        original.ifPresent(value -> value.setIndicatorType(actorIndicator.indicatorType()));
        original.ifPresent(value -> value.setValue(actorIndicator.value()));
        original.ifPresent(value -> value.setSource(actorIndicator.source()));
        original.ifPresent(value -> value.setDate(actorIndicator.date()));

        if (original.isPresent()) {
            jpaActorIndicatorRepository.save(original.get());
            return actorIndicatorMapper.toDomain(original.get());
        }

        return new ActorIndicator();
    }

    @Override
    public void deleteById(UUID id) {
        jpaActorIndicatorRepository.deleteById(id);
    }

    private List<ActorIndicator> map(List<ActorIndicatorEntity> entities) {
        return entities.stream()
                .map(actorIndicatorMapper::toDomain)
                .toList();
    }
}

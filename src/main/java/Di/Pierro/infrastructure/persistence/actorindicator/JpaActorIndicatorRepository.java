package Di.Pierro.infrastructure.persistence.actorindicator;

import Di.Pierro.infrastructure.persistence.entity.ActorIndicatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaActorIndicatorRepository extends JpaRepository<ActorIndicatorEntity, UUID> {
    List<ActorIndicatorEntity> findByActorId(UUID actorId);
    List<ActorIndicatorEntity> findTop100ByIndicatorTypeContainingIgnoreCase(String indicatorType);
}

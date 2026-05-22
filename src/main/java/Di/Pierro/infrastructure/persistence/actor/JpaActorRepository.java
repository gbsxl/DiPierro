package Di.Pierro.infrastructure.persistence.actor;

import Di.Pierro.infrastructure.persistence.entity.ActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface JpaActorRepository extends JpaRepository<ActorEntity, UUID> {
}

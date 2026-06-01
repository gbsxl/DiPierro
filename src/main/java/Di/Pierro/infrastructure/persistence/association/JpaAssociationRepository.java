package Di.Pierro.infrastructure.persistence.association;

import Di.Pierro.infrastructure.persistence.entity.AssociationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaAssociationRepository extends JpaRepository<AssociationEntity, UUID> {
}

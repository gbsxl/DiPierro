package Di.Pierro.infrastructure.persistence.association;

import Di.Pierro.domain.enums.AssociationType;
import Di.Pierro.infrastructure.persistence.entity.AssociationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaAssociationRepository extends JpaRepository<AssociationEntity, UUID>, JpaSpecificationExecutor<AssociationEntity> {
    List<AssociationEntity> findByFirstActorIdOrSecondActorId(UUID firstActorId, UUID secondActorId);
    List<AssociationEntity> findByAssociationType(AssociationType associationType);
    List<AssociationEntity> findByAssociationEnded(boolean associationEnded);
}

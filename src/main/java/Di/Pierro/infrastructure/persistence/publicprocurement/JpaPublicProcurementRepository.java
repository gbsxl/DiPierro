package Di.Pierro.infrastructure.persistence.publicprocurement;

import Di.Pierro.infrastructure.persistence.entity.PublicProcurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPublicProcurementRepository extends JpaRepository<PublicProcurementEntity, UUID>, JpaSpecificationExecutor<PublicProcurementEntity> {
    Optional<PublicProcurementEntity> findPublicProcurementEntityByActor_Id(UUID actorId);
}

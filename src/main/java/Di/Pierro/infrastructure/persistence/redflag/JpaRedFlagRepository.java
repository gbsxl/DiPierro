package Di.Pierro.infrastructure.persistence.redflag;

import Di.Pierro.infrastructure.persistence.entity.RedFlagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaRedFlagRepository extends JpaRepository<RedFlagEntity, UUID>, JpaSpecificationExecutor<RedFlagEntity> {
}

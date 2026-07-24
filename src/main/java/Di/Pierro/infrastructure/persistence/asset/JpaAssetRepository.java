package Di.Pierro.infrastructure.persistence.asset;

import Di.Pierro.infrastructure.persistence.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaAssetRepository extends JpaRepository<AssetEntity, UUID> {
    List<AssetEntity> findTop100ByTypeContainingIgnoreCase(String type);
    List<AssetEntity> findByStillHaveIt(boolean stillHaveIt);
}

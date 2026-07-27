package Di.Pierro.infrastructure.persistence.business;

import Di.Pierro.infrastructure.persistence.entity.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaBusinessRepository extends JpaRepository<BusinessEntity, UUID> {
    Optional<BusinessEntity> findByActorId(UUID actorId);
    List<BusinessEntity> findTop100ByLegalNameContainingIgnoreCaseOrFantasyNameContainingIgnoreCase(String legalName, String fantasyName);
    List<BusinessEntity> findTop100ByCnpjLike(String cnpj);
    List<BusinessEntity> findTop100ByPhoneNumberContainingIgnoreCase(String phoneNumber);
    List<BusinessEntity> findTop100ByEmailContainingIgnoreCase(String email);
    List<BusinessEntity> findByIsPublicCompany(boolean isPublicCompany);
}

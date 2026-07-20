package Di.Pierro.infrastructure.persistence.person;

import Di.Pierro.domain.enums.Gender;
import Di.Pierro.infrastructure.persistence.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPersonRepository extends JpaRepository<PersonEntity, UUID> {
    Optional<PersonEntity> findByActorId(UUID actorId);
    List<PersonEntity> findTop100ByEmailContainingIgnoreCase(String email);
    List<PersonEntity> findTop100ByCompleteNameContainingIgnoreCase(String name);
    List<PersonEntity> findTop100ByCpfLike(String cpf);
    List<PersonEntity> findByGender(Gender gender);
}

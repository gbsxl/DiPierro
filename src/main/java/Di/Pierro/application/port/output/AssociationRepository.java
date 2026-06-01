package Di.Pierro.application.port.output;

import Di.Pierro.domain.model.Association;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssociationRepository {
    void save(Association association, UUID firstActor, UUID secondActor);
    Optional<Association> findById(UUID id);
    List<Association> findAll();
}

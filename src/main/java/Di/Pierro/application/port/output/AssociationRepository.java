package Di.Pierro.application.port.output;

import Di.Pierro.application.dto.association.CreateAssociationInput;
import Di.Pierro.domain.model.Association;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssociationRepository {
    void save(Association association, UUID firstActor, UUID secondActor);
    Optional<Association> findById(UUID id);
    List<Association> findAll();
    List<Association> findByActorId(UUID id);
    List<Association> findByType(String string);
    List<Association> findEndedAssociations();
    Association updateById(UUID id, CreateAssociationInput association);
    void deleteById(UUID id);
}

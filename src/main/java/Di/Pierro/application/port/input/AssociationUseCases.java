package Di.Pierro.application.port.input;

import Di.Pierro.application.dto.association.AssociationFilter;
import Di.Pierro.application.dto.association.CreateAssociationInput;
import Di.Pierro.domain.model.Association;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssociationUseCases {
    void createAssociation(CreateAssociationInput createAssociationInput);
    List<Association> findAll();
    List<Association> findAllByIds(List<UUID> ids);
    List<Association> findByFilter(AssociationFilter filter);
    Optional<Association> findById(UUID id);
    List<Association> findByActorId(UUID id);
    List<Association> findByType(String string);
    List<Association> findEndedAssociations();
    Association updateById(UUID id, CreateAssociationInput association);
    void deleteById(UUID id);
}

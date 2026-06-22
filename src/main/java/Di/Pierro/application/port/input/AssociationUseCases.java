package Di.Pierro.application.port.input;

import Di.Pierro.application.dto.association.CreateAssociationInput;
import Di.Pierro.domain.model.Association;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssociationUseCases {
    void createAssociation(CreateAssociationInput createAssociationInput);
    List<Association> findAll();
    Optional<Association> findById(UUID id);
}

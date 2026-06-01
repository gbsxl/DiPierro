package Di.Pierro.application.usecase.association;

import Di.Pierro.application.port.output.AssociationRepository;
import Di.Pierro.domain.model.Association;

import java.util.Optional;
import java.util.UUID;

public class GetAssociationByIdUseCase {
    AssociationRepository associationRepository;

    public GetAssociationByIdUseCase(AssociationRepository associationRepository) {
        this.associationRepository = associationRepository;
    }

    public Optional<Association> execute(UUID id){
        return associationRepository.findById(id);
    }
}

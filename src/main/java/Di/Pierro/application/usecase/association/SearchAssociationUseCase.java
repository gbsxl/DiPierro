package Di.Pierro.application.usecase.association;

import Di.Pierro.application.port.output.AssociationRepository;
import Di.Pierro.domain.model.Association;

import java.util.List;

public class SearchAssociationUseCase {
    AssociationRepository associationRepository;

    public SearchAssociationUseCase(AssociationRepository associationRepository) {
        this.associationRepository = associationRepository;
    }

    public List<Association> execute(){
        return associationRepository.findAll();
    }
}

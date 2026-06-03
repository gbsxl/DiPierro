package Di.Pierro.application.usecase.association;

import Di.Pierro.application.dto.association.CreateAssociationInput;
import Di.Pierro.application.port.output.AssociationRepository;
import Di.Pierro.domain.enums.AssociationType;
import Di.Pierro.domain.model.Association;

public class CreateAssociationUseCase {
    AssociationRepository associationRepository;

    public CreateAssociationUseCase(AssociationRepository associationRepository) {
        this.associationRepository = associationRepository;
    }

    public void execute(CreateAssociationInput associationInput){
        Association association = Association.createAssociation(
                AssociationType.fromCode(associationInput.associationType()),
                associationInput.source(),
                associationInput.confidenceLevel(),
                associationInput.associationEnded(),
                associationInput.associationStart()
        );

        if(associationInput.associationEnd() != null) association.setAssociationEnd(associationInput.associationEnd());

        associationRepository.save(association, associationInput.firstActorId() ,associationInput.secondActorId());
    }
}

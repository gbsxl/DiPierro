package Di.Pierro.application.port.usecase.association;

import Di.Pierro.application.dto.association.CreateAssociationInput;
import Di.Pierro.application.port.input.AssociationUseCases;
import Di.Pierro.application.port.output.AssociationRepository;
import Di.Pierro.domain.enums.AssociationType;
import Di.Pierro.domain.model.Association;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AssociationUseCasesImplementation implements AssociationUseCases {
    private final AssociationRepository associationRepository;

    public AssociationUseCasesImplementation(AssociationRepository associationRepository) {
        this.associationRepository = associationRepository;
    }

    @Override
    public void createAssociation(CreateAssociationInput createAssociationInput) {
        Association association = Association.createAssociation(
                AssociationType.fromCode(createAssociationInput.associationType()),
                createAssociationInput.source(),
                createAssociationInput.confidenceLevel(),
                createAssociationInput.associationEnded(),
                createAssociationInput.associationStart()
        );

        if (createAssociationInput.associationEnd() != null) association.setAssociationEnd(createAssociationInput.associationEnd());

        associationRepository.save(association, createAssociationInput.firstActorId(), createAssociationInput.secondActorId());
    }

    @Override
    public List<Association> findAll() {
        return associationRepository.findAll();
    }

    @Override
    public Optional<Association> findById(UUID id) {
        return associationRepository.findById(id);
    }
}

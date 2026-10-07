package Di.Pierro.application.port.usecase.association;

import Di.Pierro.application.dto.association.AssociationFilter;
import Di.Pierro.application.dto.association.CreateAssociationInput;
import Di.Pierro.application.port.input.AssociationUseCases;
import Di.Pierro.application.port.input.BusinessUseCases;
import Di.Pierro.application.port.input.PersonUseCases;
import Di.Pierro.application.port.input.PublicProcurementUseCases;
import Di.Pierro.application.port.output.AssociationRepository;
import Di.Pierro.application.validator.association.AssociationDomainValidator;
import Di.Pierro.domain.enums.ActorCategory;
import Di.Pierro.domain.enums.AssociationType;
import Di.Pierro.domain.model.Association;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AssociationUseCasesImplementation implements AssociationUseCases {
    private final AssociationRepository associationRepository;
    private final PersonUseCases personUseCases;
    private final BusinessUseCases businessUseCases;
    private final PublicProcurementUseCases publicProcurementUseCases;
    private final AssociationDomainValidator associationDomainValidator;

    public AssociationUseCasesImplementation(
            AssociationRepository associationRepository,
            PersonUseCases personUseCases,
            BusinessUseCases businessUseCases,
            PublicProcurementUseCases publicProcurementUseCases,
            AssociationDomainValidator associationDomainValidator
    ) {
        this.associationRepository = associationRepository;
        this.personUseCases = personUseCases;
        this.businessUseCases = businessUseCases;
        this.publicProcurementUseCases = publicProcurementUseCases;
        this.associationDomainValidator = associationDomainValidator;
    }

    @Override
    public void createAssociation(CreateAssociationInput createAssociationInput) {
        validateAssociation(createAssociationInput);

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
    public List<Association> findAllByIds(List<UUID> ids) {
        return associationRepository.findAllByIds(ids);
    }

    @Override
    public List<Association> findByFilter(AssociationFilter filter) {
        return associationRepository.findByFilter(filter);
    }

    @Override
    public Optional<Association> findById(UUID id) {
        return associationRepository.findById(id);
    }

    @Override
    public List<Association> findByActorId(UUID id) {
        return associationRepository.findByActorId(id);
    }

    @Override
    public List<Association> findByType(String string) {
        return associationRepository.findByType(string);
    }

    @Override
    public List<Association> findEndedAssociations() {
        return associationRepository.findEndedAssociations();
    }

    @Override
    public Association updateById(UUID id, CreateAssociationInput association) {
        validateAssociation(association);
        return associationRepository.updateById(id, association);
    }

    @Override
    public void deleteById(UUID id) {
        associationRepository.deleteById(id);
    }

    private void validateAssociation(CreateAssociationInput input) {
        if (input == null) return;
        AssociationType associationType = AssociationType.fromCode(input.associationType());
        ActorCategory firstCategory = resolveActorCategory(input.firstActorId());
        ActorCategory secondCategory = resolveActorCategory(input.secondActorId());

        associationDomainValidator.validateAssociationScope(
                input.firstActorId(),
                firstCategory,
                input.secondActorId(),
                secondCategory,
                associationType
        );
    }

    private ActorCategory resolveActorCategory(UUID actorId) {
        if (actorId == null) return ActorCategory.UNKNOWN;
        if (personUseCases.findByActorId(actorId).isPresent()) {
            return ActorCategory.PERSON;
        }
        if (businessUseCases.findByActorId(actorId).isPresent()) {
            return ActorCategory.BUSINESS;
        }
        if (publicProcurementUseCases.findByActorId(actorId).isPresent()) {
            return ActorCategory.PUBLIC_PROCUREMENT;
        }
        return ActorCategory.UNKNOWN;
    }
}

package Di.Pierro.application.port.usecase.publicprocurement;

import Di.Pierro.application.dto.publicprocurement.CreatePublicProcurementInput;
import Di.Pierro.application.port.input.PublicProcurementUseCases;
import Di.Pierro.application.port.output.PublicProcurementRepository;
import Di.Pierro.domain.model.PublicProcurement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PublicProcurementUseCasesImplementation implements PublicProcurementUseCases {
    PublicProcurementRepository publicProcurementRepository;

    public PublicProcurementUseCasesImplementation(PublicProcurementRepository publicProcurementRepository) {
        this.publicProcurementRepository = publicProcurementRepository;
    }

    @Override
    public void createPublicProcurement(CreatePublicProcurementInput createPublicProcurementInput) {
        PublicProcurement publicProcurement = PublicProcurement.createPublicProcurement(
                createPublicProcurementInput.publicProcurementNumber(),
                createPublicProcurementInput.processNumber(),
                createPublicProcurementInput.object(),
                createPublicProcurementInput.modality(),
                createPublicProcurementInput.situation(),
                createPublicProcurementInput.legalInstrument(),
                createPublicProcurementInput.estimatedValue(),
                createPublicProcurementInput.publicationDate(),
                createPublicProcurementInput.openingDate(),
                createPublicProcurementInput.designatedContact(),
                createPublicProcurementInput.ibgeCityCode(),
                createPublicProcurementInput.federativeUnitAcronym(),
                createPublicProcurementInput.managingUnityCode(),
                createPublicProcurementInput.cnpjGovernmentAgency()
        );
        publicProcurementRepository.save(publicProcurement, createPublicProcurementInput.actorId());
    }

    @Override
    public List<PublicProcurement> findAll() {
        return publicProcurementRepository.findAll();
    }

    @Override
    public Optional<PublicProcurement> findById(UUID id) {
        return publicProcurementRepository.findById(id);
    }
}

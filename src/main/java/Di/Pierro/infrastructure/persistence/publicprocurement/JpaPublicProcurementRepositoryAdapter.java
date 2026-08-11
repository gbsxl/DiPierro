package Di.Pierro.infrastructure.persistence.publicprocurement;

import Di.Pierro.application.dto.publicprocurement.CreatePublicProcurementInput;
import Di.Pierro.application.dto.publicprocurement.PublicProcurementFilter;
import Di.Pierro.application.port.output.PublicProcurementRepository;
import Di.Pierro.domain.model.PublicProcurement;
import Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException;
import Di.Pierro.infrastructure.mapper.PublicProcurementMapper;
import Di.Pierro.infrastructure.persistence.entity.PublicProcurementEntity;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaPublicProcurementRepositoryAdapter implements PublicProcurementRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaPublicProcurementRepositoryAdapter.class);

    private final JpaPublicProcurementRepository jpaPublicProcurementRepository;
    private final PublicProcurementMapper publicProcurementMapper;

    @Override
    public void save(PublicProcurement publicProcurement) {
        log.debug("Saving public procurement");
        jpaPublicProcurementRepository.save(publicProcurementMapper.toEntity(publicProcurement));
    }

    @Override
    public Optional<PublicProcurement> findById(UUID id) {
        log.debug("Looking up public procurement by id={}", id);
        return jpaPublicProcurementRepository.findById(id).map(publicProcurementMapper::toDomain);
    }

    @Override
    public Optional<PublicProcurement> findByActorId(UUID id) {
        log.debug("Looking up public procurement by actorId={}", id);
        return jpaPublicProcurementRepository.findPublicProcurementEntityByActor_Id(id).map(publicProcurementMapper::toDomain);
    }

    @Override
    public List<PublicProcurement> findAll() {
        log.debug("Fetching all public procurements");
        return jpaPublicProcurementRepository.findAll().stream().map(publicProcurementMapper::toDomain).toList();
    }

    @Override
    public List<PublicProcurement> findByFilter(PublicProcurementFilter filter) {
        log.debug("Searching public procurements by filter={}", filter);
        Specification<PublicProcurementEntity> spec = Specification.unrestricted();

        if (filter.publicProcurementNumber() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasPublicProcurementNumber(filter.publicProcurementNumber()));
        }
        if (filter.processNumber() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasProcessNumber(filter.processNumber()));
        }
        if (filter.object() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasObject(filter.object()));
        }
        if (filter.modality() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasModality(filter.modality()));
        }
        if (filter.situation() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasSituation(filter.situation()));
        }
        if (filter.legalInstrument() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasLegalInstrument(filter.legalInstrument()));
        }
        if (filter.estimatedValue() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasEstimatedValue(filter.estimatedValue()));
        }
        if (filter.publicationDate() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasPublicationDate(filter.publicationDate()));
        }
        if (filter.openingDate() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasOpeningDate(filter.openingDate()));
        }
        if (filter.designatedContact() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasDesignatedContact(filter.designatedContact()));
        }
        if (filter.ibgeCityCode() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasIbgeCityCode(filter.ibgeCityCode()));
        }
        if (filter.federativeUnitAcronym() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasFederativeUnitAcronym(filter.federativeUnitAcronym()));
        }
        if (filter.managingUnityCode() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasManagingUnityCode(filter.managingUnityCode()));
        }
        if (filter.cnpjGovernmentAgency() != null) {
            spec = spec.and(PublicProcurementSpecifications.hasCnpjGovernmentAgency(filter.cnpjGovernmentAgency()));
        }

        return jpaPublicProcurementRepository.findAll(spec).stream().map(publicProcurementMapper::toDomain).toList();
    }

    @Override
    public PublicProcurement updateById(UUID id, CreatePublicProcurementInput procurementInput) {
        PublicProcurementEntity entity = jpaPublicProcurementRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("public-procurement.not-found", "PublicProcurement", id));

        entity.setPublicProcurementNumber(procurementInput.publicProcurementNumber());
        entity.setProcessNumber(procurementInput.processNumber());
        entity.setObject(procurementInput.object());
        entity.setModality(procurementInput.modality());
        entity.setSituation(procurementInput.situation());
        entity.setLegalInstrument(procurementInput.legalInstrument());
        entity.setEstimatedValue(procurementInput.estimatedValue());
        entity.setPublicationDate(procurementInput.publicationDate());
        entity.setOpeningDate(procurementInput.openingDate());
        entity.setDesignatedContact(procurementInput.designatedContact());
        entity.setIbgeCityCode(procurementInput.ibgeCityCode());
        entity.setFederativeUnitAcronym(procurementInput.federativeUnitAcronym());
        entity.setManagingUnityCode(procurementInput.managingUnityCode());
        entity.setCnpjGovernmentAgency(procurementInput.cnpjGovernmentAgency());

        log.debug("Updating public procurement id={}", id);
        jpaPublicProcurementRepository.save(entity);
        return publicProcurementMapper.toDomain(entity);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting public procurement id={}", id);
        jpaPublicProcurementRepository.deleteById(id);
    }
}

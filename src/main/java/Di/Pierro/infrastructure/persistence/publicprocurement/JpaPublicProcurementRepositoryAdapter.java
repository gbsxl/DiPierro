package Di.Pierro.infrastructure.persistence.publicprocurement;

import Di.Pierro.application.dto.publicprocurement.CreatePublicProcurementInput;
import Di.Pierro.application.dto.publicprocurement.PublicProcurementFilter;
import Di.Pierro.application.port.output.PublicProcurementRepository;
import Di.Pierro.domain.model.PublicProcurement;
import Di.Pierro.infrastructure.mapper.PublicProcurementMapper;
import Di.Pierro.infrastructure.persistence.entity.PublicProcurementEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaPublicProcurementRepositoryAdapter implements PublicProcurementRepository {

    private final JpaPublicProcurementRepository jpaPublicProcurementRepository;
    private final PublicProcurementMapper publicProcurementMapper;

    @Override
    public void save(PublicProcurement publicProcurement) {
        jpaPublicProcurementRepository.save(publicProcurementMapper.toEntity(publicProcurement));
    }

    @Override
    public Optional<PublicProcurement> findById(UUID id) {
        return jpaPublicProcurementRepository.findById(id).map(publicProcurementMapper::toDomain);
    }

    @Override
    public Optional<PublicProcurement> findByActorId(UUID id) {
        return jpaPublicProcurementRepository.findPublicProcurementEntityByActor_Id(id).map(publicProcurementMapper::toDomain);
    }

    @Override
    public List<PublicProcurement> findAll() {
        return jpaPublicProcurementRepository.findAll().stream().map(publicProcurementMapper::toDomain).toList();
    }

    @Override
    public List<PublicProcurement> findByFilter(PublicProcurementFilter filter) {
        Specification<PublicProcurementEntity> spec = Specification.unrestricted();

        if (filter.publicProcurementNumber() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasPublicProcurementNumber(
                            filter.publicProcurementNumber()
                    )
            );
        }

        if (filter.processNumber() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasProcessNumber(
                            filter.processNumber()
                    )
            );
        }

        if (filter.object() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasObject(
                            filter.object()
                    )
            );
        }

        if (filter.modality() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasModality(
                            filter.modality()
                    )
            );
        }

        if (filter.situation() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasSituation(
                            filter.situation()
                    )
            );
        }

        if (filter.legalInstrument() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasLegalInstrument(
                            filter.legalInstrument()
                    )
            );
        }

        if (filter.estimatedValue() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasEstimatedValue(
                            filter.estimatedValue()
                    )
            );
        }

        if (filter.publicationDate() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasPublicationDate(
                            filter.publicationDate()
                    )
            );
        }

        if (filter.openingDate() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasOpeningDate(
                            filter.openingDate()
                    )
            );
        }

        if (filter.designatedContact() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasDesignatedContact(
                            filter.designatedContact()
                    )
            );
        }

        if (filter.ibgeCityCode() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasIbgeCityCode(
                            filter.ibgeCityCode()
                    )
            );
        }

        if (filter.federativeUnitAcronym() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasFederativeUnitAcronym(
                            filter.federativeUnitAcronym()
                    )
            );
        }

        if (filter.managingUnityCode() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasManagingUnityCode(
                            filter.managingUnityCode()
                    )
            );
        }

        if (filter.cnpjGovernmentAgency() != null) {
            spec = spec.and(
                    PublicProcurementSpecifications.hasCnpjGovernmentAgency(
                            filter.cnpjGovernmentAgency()
                    )
            );
        }

        return jpaPublicProcurementRepository.findAll(spec).stream().map(publicProcurementMapper::toDomain).toList();
    }

    @Override
    public PublicProcurement updateById(UUID id, CreatePublicProcurementInput procurementInput) {
        Optional<PublicProcurementEntity> publicProcurementOriginal = jpaPublicProcurementRepository.findById(id);
        publicProcurementOriginal.ifPresent(value-> value.setPublicProcurementNumber(procurementInput.publicProcurementNumber()));
        publicProcurementOriginal.ifPresent(value-> value.setProcessNumber(procurementInput.processNumber()));
        publicProcurementOriginal.ifPresent(value-> value.setObject(procurementInput.object()));
        publicProcurementOriginal.ifPresent(value-> value.setModality(procurementInput.modality()));
        publicProcurementOriginal.ifPresent(value-> value.setSituation(procurementInput.situation()));
        publicProcurementOriginal.ifPresent(value-> value.setLegalInstrument(procurementInput.legalInstrument()));
        publicProcurementOriginal.ifPresent(value-> value.setEstimatedValue(procurementInput.estimatedValue()));
        publicProcurementOriginal.ifPresent(value-> value.setPublicationDate(procurementInput.publicationDate()));
        publicProcurementOriginal.ifPresent(value-> value.setOpeningDate(procurementInput.openingDate()));
        publicProcurementOriginal.ifPresent(value-> value.setDesignatedContact(procurementInput.designatedContact()));
        publicProcurementOriginal.ifPresent(value-> value.setIbgeCityCode(procurementInput.ibgeCityCode()));
        publicProcurementOriginal.ifPresent(value-> value.setFederativeUnitAcronym(procurementInput.federativeUnitAcronym()));
        publicProcurementOriginal.ifPresent(value-> value.setManagingUnityCode(procurementInput.managingUnityCode()));
        publicProcurementOriginal.ifPresent(value-> value.setCnpjGovernmentAgency(procurementInput.cnpjGovernmentAgency()));

        if(publicProcurementOriginal.isPresent()){
            jpaPublicProcurementRepository.save(publicProcurementOriginal.get());
            return publicProcurementMapper.toDomain(publicProcurementOriginal.get());
        }

        return new PublicProcurement();
    }

    @Override
    public void deleteById(UUID id) {
        jpaPublicProcurementRepository.deleteById(id);
    }
}

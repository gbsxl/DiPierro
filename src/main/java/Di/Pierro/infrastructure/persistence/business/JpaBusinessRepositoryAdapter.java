package Di.Pierro.infrastructure.persistence.business;

import Di.Pierro.application.dto.business.CreateBusinessInput;
import Di.Pierro.application.port.output.BusinessRepository;
import Di.Pierro.domain.enums.State;
import Di.Pierro.domain.model.Business;
import Di.Pierro.infrastructure.exception.custom.BadRequestException;
import Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException;
import Di.Pierro.infrastructure.mapper.BusinessMapper;
import Di.Pierro.infrastructure.persistence.entity.AddressEntity;
import Di.Pierro.infrastructure.persistence.entity.BusinessEntity;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaBusinessRepositoryAdapter implements BusinessRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaBusinessRepositoryAdapter.class);

    private final JpaBusinessRepository jpaBusinessRepository;
    private final BusinessMapper businessMapper;

    @Override
    public void save(Business business) {
        log.debug("Saving business with CNPJ ending in ...{}", safeSuffix(business.getCnpj()));
        jpaBusinessRepository.save(businessMapper.toEntity(business));
    }

    @Override
    public Optional<Business> findById(UUID id) {
        log.debug("Looking up business by id={}", id);
        return jpaBusinessRepository.findById(id).map(businessMapper::toDomain);
    }

    @Override
    public List<Business> findAll() {
        log.debug("Fetching all businesses");
        return jpaBusinessRepository.findAll().stream().map(businessMapper::toDomain).toList();
    }

    @Override
    public List<Business> findAllByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        log.debug("Fetching businesses by ids list count={}", ids.size());
        return map(jpaBusinessRepository.findAllById(ids));
    }

    @Override
    public Optional<Business> findByActorId(UUID id) {
        log.debug("Looking up business by actorId={}", id);
        return jpaBusinessRepository.findByActorId(id).map(businessMapper::toDomain);
    }

    @Override
    public List<Business> findByActorIds(List<UUID> actorIds) {
        if (actorIds == null || actorIds.isEmpty()) return List.of();
        log.debug("Fetching businesses by actorIds list count={}", actorIds.size());
        return map(jpaBusinessRepository.findByActorIdIn(actorIds));
    }

    @Override
    public List<Business> findByName(String string) {
        if (string == null || string.trim().length() < 3) {
            throw new BadRequestException("business.name-too-short", "Search term for name must have at least 3 characters.");
        }
        log.debug("Searching businesses by name containing '{}'", string.trim());
        return map(jpaBusinessRepository
                .findTop100ByLegalNameContainingIgnoreCaseOrFantasyNameContainingIgnoreCase(
                        string.trim(), string.trim()));
    }

    @Override
    public List<Business> findByCNPJ(String string) {
        if (string == null || string.isBlank()) {
            throw new BadRequestException("business.cnpj-blank", "CNPJ cannot be blank.");
        }

        String sqlPattern = string.replaceAll("[Xx*?-]", "_");

        if (sqlPattern.length() < 14 && !sqlPattern.contains("_")) {
            sqlPattern = sqlPattern + "%";
        }

        if (sqlPattern.equals("______________") || sqlPattern.equals("%")) {
            throw new BadRequestException("business.cnpj-invalid-wildcard", "CNPJ cannot be a full wildcard pattern.");
        }

        log.debug("Searching businesses by CNPJ pattern '{}'", sqlPattern);
        return map(jpaBusinessRepository.findTop100ByCnpjLike(sqlPattern));
    }

    @Override
    public List<Business> findByPhoneNumber(String string) {
        if (string == null || string.trim().length() < 3) {
            throw new BadRequestException("business.phone-too-short", "Search term for phone number must have at least 3 characters.");
        }
        log.debug("Searching businesses by phone containing '{}'", string.trim());
        return map(jpaBusinessRepository.findTop100ByPhoneNumberContainingIgnoreCase(string.trim()));
    }

    @Override
    public List<Business> findByEmail(String string) {
        if (string == null || string.trim().length() < 3) {
            throw new BadRequestException("business.email-too-short", "Search term for email must have at least 3 characters.");
        }
        log.debug("Searching businesses by email containing '{}'", string.trim());
        return map(jpaBusinessRepository.findTop100ByEmailContainingIgnoreCase(string.trim()));
    }

    @Override
    public List<Business> findPublicCompanies() {
        log.debug("Fetching all public companies");
        return map(jpaBusinessRepository.findByIsPublicCompany(true));
    }

    @Override
    public Business updateById(UUID id, CreateBusinessInput business) {
        BusinessEntity entity = jpaBusinessRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("business.not-found", "Business", id));

        entity.setLegalName(business.legalName());
        entity.setCnpj(business.cnpj());
        entity.setFantasyName(business.fantasyName());
        entity.setPhoneNumber(business.phoneNumber());
        entity.setEmail(business.email());
        entity.setPublicCompany(business.isPublicCompany());

        AddressEntity addressEntity = null;
        if (business.address() != null) {
            AddressEntity existing = entity.getAddress();
            UUID addressId = existing != null && existing.getId() != null ? existing.getId() : UUID.randomUUID();
            addressEntity = AddressEntity.builder()
                    .id(addressId)
                    .postalCode(business.address().postalCode())
                    .streetAddress(business.address().streetAddress())
                    .number(business.address().number())
                    .complement(business.address().complement())
                    .neighborhood(business.address().neighborhood())
                    .city(business.address().city())
                    .state(business.address().state() != null ? State.fromSigla(business.address().state()) : null)
                    .actor(entity.getActor())
                    .build();
        }
        entity.setAddress(addressEntity);

        entity.setCapitalStock(business.capitalStock());
        entity.setEstimatedNetWorth(business.estimatedNetWorth());
        entity.setOpenDate(business.openDate());

        log.debug("Updating business id={}", id);
        jpaBusinessRepository.save(entity);
        return businessMapper.toDomain(entity);
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting business id={}", id);
        jpaBusinessRepository.deleteById(id);
    }

    private List<Business> map(List<BusinessEntity> entities) {
        return entities.stream().map(businessMapper::toDomain).toList();
    }

    private String safeSuffix(String value) {
        if (value == null || value.length() < 4) return "****";
        return value.substring(value.length() - 4);
    }
}

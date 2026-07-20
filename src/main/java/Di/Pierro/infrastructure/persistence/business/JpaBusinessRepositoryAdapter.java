package Di.Pierro.infrastructure.persistence.business;

import Di.Pierro.application.dto.business.CreateBusinessInput;
import Di.Pierro.application.port.output.BusinessRepository;
import Di.Pierro.domain.model.Business;
import Di.Pierro.infrastructure.mapper.BusinessMapper;
import Di.Pierro.infrastructure.persistence.entity.BusinessEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaBusinessRepositoryAdapter implements BusinessRepository {

    private final JpaBusinessRepository jpaBusinessRepository;
    private final BusinessMapper businessMapper;

    @Override
    public void save(Business business) {
        jpaBusinessRepository.save(businessMapper.toEntity(business));
    }

    @Override
    public Optional<Business> findById(UUID id) {
        return jpaBusinessRepository.findById(id).map(businessMapper::toDomain);
    }

    @Override
    public List<Business> findAll() {
        return jpaBusinessRepository
                .findAll()
                .stream()
                .map(businessMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Business> findByActorId(UUID id) {
        return jpaBusinessRepository.findByActorId(id).map(businessMapper::toDomain);
    }

    @Override
    public List<Business> findByName(String string) {
        if (string == null || string.trim().length() < 3) {
            return List.of();
        }

        return map(jpaBusinessRepository
                .findTop100ByLegalNameContainingIgnoreCaseOrFantasyNameContainingIgnoreCase(
                        string.trim(), string.trim()));
    }

    @Override
    public List<Business> findByCNPJ(String string) {
        if (string == null || string.isBlank()) {
            return List.of();
        }

        String sqlPattern = string.replaceAll("[Xx*?-]", "_");

        if (sqlPattern.length() < 14 && !sqlPattern.contains("_")) {
            sqlPattern = sqlPattern + "%";
        }

        if (sqlPattern.equals("______________") || sqlPattern.equals("%")) {
            return List.of();
        }

        return map(jpaBusinessRepository.findTop100ByCnpjLike(sqlPattern));
    }

    @Override
    public List<Business> findByPhoneNumber(String string) {
        if (string == null || string.trim().length() < 3) {
            return List.of();
        }

        return map(jpaBusinessRepository.findTop100ByPhoneNumberContainingIgnoreCase(string.trim()));
    }

    @Override
    public List<Business> findByEmail(String string) {
        if (string == null || string.trim().length() < 3) {
            return List.of();
        }

        return map(jpaBusinessRepository.findTop100ByEmailContainingIgnoreCase(string.trim()));
    }

    @Override
    public List<Business> findPublicCompanies() {
        return map(jpaBusinessRepository.findByIsPublicCompany(true));
    }

    @Override
    public Business updateById(UUID id, CreateBusinessInput business) {
        Optional<BusinessEntity> businessOriginal = jpaBusinessRepository.findById(id);
        businessOriginal.ifPresent(value -> value.setLegalName(business.legalName()));
        businessOriginal.ifPresent(value -> value.setCnpj(business.cnpj()));
        businessOriginal.ifPresent(value -> value.setFantasyName(business.fantasyName()));
        businessOriginal.ifPresent(value -> value.setPhoneNumber(business.phoneNumber()));
        businessOriginal.ifPresent(value -> value.setEmail(business.email()));
        businessOriginal.ifPresent(value -> value.setPublicCompany(business.isPublicCompany()));
        businessOriginal.ifPresent(value -> value.setAddress(business.address()));
        businessOriginal.ifPresent(value -> value.setCapitalStock(business.capitalStock()));
        businessOriginal.ifPresent(value -> value.setEstimatedNetWorth(business.estimatedNetWorth()));

        if (businessOriginal.isPresent()) {
            jpaBusinessRepository.save(businessOriginal.get());
            return businessMapper.toDomain(businessOriginal.get());
        }

        return new Business();
    }

    @Override
    public void deleteById(UUID id) {
        jpaBusinessRepository.deleteById(id);
    }

    private List<Business> map(List<BusinessEntity> entities) {
        return entities.stream()
                .map(businessMapper::toDomain)
                .toList();
    }
}

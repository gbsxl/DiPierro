package Di.Pierro.infrastructure.persistence.business;

import Di.Pierro.application.port.output.BusinessRepository;
import Di.Pierro.domain.model.Business;
import Di.Pierro.infrastructure.mapper.BusinessMapper;
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
}

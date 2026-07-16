package Di.Pierro.infrastructure.persistence.publicprocurement;

import Di.Pierro.application.port.output.PublicProcurementRepository;
import Di.Pierro.domain.model.PublicProcurement;
import Di.Pierro.infrastructure.mapper.PublicProcurementMapper;
import lombok.AllArgsConstructor;
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
    public List<PublicProcurement> findAll() {
        return jpaPublicProcurementRepository.findAll().stream().map(publicProcurementMapper::toDomain).toList();
    }
}

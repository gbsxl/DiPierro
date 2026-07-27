package Di.Pierro.application.port.output;

import Di.Pierro.application.dto.publicprocurement.CreatePublicProcurementInput;
import Di.Pierro.application.dto.publicprocurement.PublicProcurementFilter;
import Di.Pierro.domain.model.PublicProcurement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicProcurementRepository {
    void save(PublicProcurement publicProcurement);
    Optional<PublicProcurement> findById(UUID id);
    Optional<PublicProcurement> findByActorId(UUID id);
    List<PublicProcurement> findAll();
    List<PublicProcurement> findByFilter(PublicProcurementFilter filter);
    PublicProcurement updateById(UUID id, CreatePublicProcurementInput procurementInput);
    void deleteById(UUID id);
}

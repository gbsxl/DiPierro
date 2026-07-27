package Di.Pierro.application.port.input;

import Di.Pierro.application.dto.publicprocurement.CreatePublicProcurementInput;
import Di.Pierro.application.dto.publicprocurement.PublicProcurementFilter;
import Di.Pierro.domain.model.PublicProcurement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicProcurementUseCases {
    void createPublicProcurement(CreatePublicProcurementInput createPublicProcurementInput);
    List<PublicProcurement> findAll();
    Optional<PublicProcurement> findById(UUID id);
    Optional<PublicProcurement> findByActorId(UUID id);
    List<PublicProcurement> findByFilter(PublicProcurementFilter filter);
    PublicProcurement updateById(UUID id, CreatePublicProcurementInput procurementInput);
    void deleteById(UUID id);
}

























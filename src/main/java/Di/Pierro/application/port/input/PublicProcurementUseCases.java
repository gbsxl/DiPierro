package Di.Pierro.application.port.input;

import Di.Pierro.application.dto.publicprocurement.CreatePublicProcurementInput;
import Di.Pierro.domain.model.PublicProcurement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicProcurementUseCases {
    void createPublicProcurement(CreatePublicProcurementInput createPublicProcurementInput);
    List<PublicProcurement> findAll();
    Optional<PublicProcurement> findById(UUID id);
}

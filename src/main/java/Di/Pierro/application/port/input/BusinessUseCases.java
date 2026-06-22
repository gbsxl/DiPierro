package Di.Pierro.application.port.input;

import Di.Pierro.application.dto.business.CreateBusinessInput;
import Di.Pierro.domain.model.Business;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessUseCases {
    void createBusiness(CreateBusinessInput createBusinessInput);
    List<Business> findAll();
    Optional<Business> findById(UUID id);
}

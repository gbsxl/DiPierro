package Di.Pierro.application.usecase.business;

import Di.Pierro.application.port.output.BusinessRepository;
import Di.Pierro.domain.model.Business;

import java.util.Optional;
import java.util.UUID;

public class GetBusinessByIdUseCase {
    private final BusinessRepository businessRepository;

    public GetBusinessByIdUseCase(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    public Optional<Business> execute(UUID id){
        return businessRepository.findById(id);
    }
}

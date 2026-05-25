package Di.Pierro.application.usecase.business;

import Di.Pierro.application.port.output.BusinessRepository;
import Di.Pierro.domain.model.Business;

import java.util.List;

public class SearchBusinessUseCase {
    private final BusinessRepository businessRepository;

    public SearchBusinessUseCase(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    public List<Business> execute(){
        return businessRepository.findAll();
    }
}

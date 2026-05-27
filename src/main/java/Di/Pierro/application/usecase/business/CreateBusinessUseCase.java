package Di.Pierro.application.usecase.business;

import Di.Pierro.application.dto.business.CreateBusinessInput;
import Di.Pierro.application.port.output.BusinessRepository;
import Di.Pierro.domain.model.Business;

public class CreateBusinessUseCase {
    private final BusinessRepository businessRepository;

    public CreateBusinessUseCase(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }
    public void execute(CreateBusinessInput businessInput){
        Business business = Business.createBusiness(
                businessInput.legalName(),
                businessInput.cnpj(),
                businessInput.fantasyName(),
                businessInput.phoneNumber(),
                businessInput.email(),
                businessInput.isPublicCompany()
        );
        businessRepository.save(business, businessInput.actorId());
    }
}

package Di.Pierro.application.port.usecase.business;

import Di.Pierro.application.dto.business.CreateBusinessInput;
import Di.Pierro.application.port.input.BusinessUseCases;
import Di.Pierro.application.port.output.BusinessRepository;
import Di.Pierro.domain.model.Business;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BusinessUseCasesImplementation implements BusinessUseCases {
    private final BusinessRepository businessRepository;

    public BusinessUseCasesImplementation(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public void createBusiness(CreateBusinessInput createBusinessInput) {
        Business business = Business.createBusiness(
                createBusinessInput.legalName(),
                createBusinessInput.cnpj(),
                createBusinessInput.fantasyName(),
                createBusinessInput.phoneNumber(),
                createBusinessInput.email(),
                createBusinessInput.isPublicCompany()
        );
        businessRepository.save(business, createBusinessInput.actorId());
    }

    @Override
    public List<Business> findAll() {
        return businessRepository.findAll();
    }

    @Override
    public Optional<Business> findById(UUID id) {
        return businessRepository.findById(id);
    }
}

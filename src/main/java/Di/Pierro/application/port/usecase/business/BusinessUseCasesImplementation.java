package Di.Pierro.application.port.usecase.business;

import Di.Pierro.application.dto.business.CreateBusinessInput;
import Di.Pierro.application.port.input.ActorUseCases;
import Di.Pierro.application.port.input.BusinessUseCases;
import Di.Pierro.application.port.output.BusinessRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Business;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BusinessUseCasesImplementation implements BusinessUseCases {
    private final BusinessRepository businessRepository;
    private final ActorUseCases actorUseCases;

    public BusinessUseCasesImplementation(BusinessRepository businessRepository, ActorUseCases actorUseCases) {
        this.businessRepository = businessRepository;
        this.actorUseCases = actorUseCases;
    }

    @Override
    public void createBusiness(CreateBusinessInput createBusinessInput) {
        Actor actor = actorUseCases.createActor();

        Business business = new Business(
                createBusinessInput.legalName(),
                createBusinessInput.cnpj(),
                createBusinessInput.fantasyName(),
                createBusinessInput.phoneNumber(),
                createBusinessInput.email(),
                createBusinessInput.isPublicCompany(),
                createBusinessInput.address(),
                createBusinessInput.estimatedNetWorth(),
                createBusinessInput.estimatedNetWorth(),
                actor
        );
        
        businessRepository.save(business);
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

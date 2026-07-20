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

    @Override
    public Optional<Business> findByActorId(UUID id) {
        return businessRepository.findByActorId(id);
    }

    @Override
    public List<Business> findByName(String string) {
        return businessRepository.findByName(string);
    }

    @Override
    public List<Business> findByCNPJ(String string) {
        return businessRepository.findByCNPJ(string);
    }

    @Override
    public List<Business> findByPhoneNumber(String string) {
        return businessRepository.findByPhoneNumber(string);
    }

    @Override
    public List<Business> findByEmail(String string) {
        return businessRepository.findByEmail(string);
    }

    @Override
    public List<Business> findPublicCompanies() {
        return businessRepository.findPublicCompanies();
    }

    @Override
    public Business updateById(UUID id, CreateBusinessInput business) {
        return businessRepository.updateById(id, business);
    }

    @Override
    public void deleteById(UUID id) {
        businessRepository.deleteById(id);
    }
}

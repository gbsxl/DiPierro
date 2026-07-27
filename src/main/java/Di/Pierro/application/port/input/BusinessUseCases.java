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
    Optional<Business> findByActorId(UUID id);
    List<Business> findByName(String string);
    List<Business> findByCNPJ(String string);
    List<Business> findByPhoneNumber(String string);
    List<Business> findByEmail(String string);
    //TODO List<Business> findByCEP(String string);
    List<Business> findPublicCompanies();
    Business updateById(UUID id, CreateBusinessInput business);
    void deleteById(UUID id);
}

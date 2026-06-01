package Di.Pierro.infrastructure.persistence.business;

import Di.Pierro.application.port.output.ActorRepository;
import Di.Pierro.application.port.output.BusinessRepository;

import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Business;

import Di.Pierro.infrastructure.mapper.BusinessMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JpaBusinessRepositoryAdapter implements BusinessRepository {

    JpaBusinessRepository jpaBusinessRepository;
    ActorRepository actorRepository;
    BusinessMapper businessMapper;

    @Override
    public void save(Business business, UUID actorId) {
        Optional<Actor> actorOptional = findActorById(actorId);
        if(actorOptional.isPresent()){
            business.setActor(actorOptional.get());
            jpaBusinessRepository.save(businessMapper.toEntity(business));
        }
    }

    @Override
    public Optional<Business> findById(UUID id) {
        return jpaBusinessRepository.findById(id).map(businessMapper::toDomain);
    }

    @Override
    public List<Business> findAll() {
        return jpaBusinessRepository
                .findAll()
                .stream()
                .map(businessMapper::toDomain)
                .toList();
    }

    private Optional<Actor> findActorById(UUID id){
        return actorRepository.findById(id);
    }

}

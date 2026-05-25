package Di.Pierro.infrastructure.persistence.business;

import Di.Pierro.application.port.output.BusinessRepository;

import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Business;

import Di.Pierro.infrastructure.persistence.actor.JpaActorRepositoryAdapter;
import Di.Pierro.infrastructure.persistence.entity.ActorEntity;
import Di.Pierro.infrastructure.persistence.entity.BusinessEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaBusinessRepositoryAdapter implements BusinessRepository {

    JpaBusinessRepository jpaBusinessRepository;
    JpaActorRepositoryAdapter jpaActorRepositoryAdapter;

    public JpaBusinessRepositoryAdapter(JpaBusinessRepository jpaBusinessRepository, JpaActorRepositoryAdapter jpaActorRepositoryAdapter) {
        this.jpaBusinessRepository = jpaBusinessRepository;
        this.jpaActorRepositoryAdapter = jpaActorRepositoryAdapter;
    }

    @Override
    public void save(Business business, UUID actorId) {
        Optional<Actor> actorOptional = findActorById(actorId);
        if(actorOptional.isPresent()){
            business.setActor(actorOptional.get());

            ActorEntity actor = new ActorEntity(
                    business.getActor().getId(),
                    business.getActor().getAddress(),
                    business.getActor().getCreatedAt(),
                    business.getActor().getUpdatedAt(),
                    business.getActor().isActive()
            );

            BusinessEntity businessEntity = new BusinessEntity(
                    business.getId(),
                    business.getLegalName(),
                    business.getCnpj(),
                    business.getFantasyName(),
                    business.getPhoneNumber(),
                    business.getEmail(),
                    business.isPublicCompany(),
                    actor
            );
            jpaBusinessRepository.save(businessEntity);
        }
    }

    @Override
    public Optional<Business> findById(UUID id) {
        return jpaBusinessRepository.findById(id).map(
                businessEntity ->
                        new Business(
                                businessEntity.getId(),
                                businessEntity.getLegalName(),
                                businessEntity.getCnpj(),
                                businessEntity.getFantasyName(),
                                businessEntity.getPhoneNumber(),
                                businessEntity.getEmail(),
                                businessEntity.isPublicCompany(),
                                new Actor(
                                        businessEntity.getActor().getId(),
                                        businessEntity.getActor().getAddress(),
                                        businessEntity.getActor().getCreatedAt(),
                                        businessEntity.getActor().getUpdatedAt(),
                                        businessEntity.getActor().isActive()
                                )
                        )
        );
    }

    @Override
    public List<Business> findAll() {
        return jpaBusinessRepository
                .findAll()
                .stream()
                .map(
            businessEntity ->
                    new Business(
                            businessEntity.getId(),
                            businessEntity.getLegalName(),
                            businessEntity.getCnpj(),
                            businessEntity.getFantasyName(),
                            businessEntity.getPhoneNumber(),
                            businessEntity.getEmail(),
                            businessEntity.isPublicCompany(),
                            new Actor(
                                    businessEntity.getActor().getId(),
                                    businessEntity.getActor().getAddress(),
                                    businessEntity.getActor().getCreatedAt(),
                                    businessEntity.getActor().getUpdatedAt(),
                                    businessEntity.getActor().isActive()
                            )
                    ))
                .toList();
    }

    private Optional<Actor> findActorById(UUID id){
        return jpaActorRepositoryAdapter.findById(id);
    }

}

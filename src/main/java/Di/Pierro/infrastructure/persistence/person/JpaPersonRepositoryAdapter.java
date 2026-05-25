package Di.Pierro.infrastructure.persistence.person;

import Di.Pierro.application.port.output.PersonRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Person;
import Di.Pierro.infrastructure.persistence.entity.ActorEntity;
import Di.Pierro.infrastructure.persistence.entity.PersonEntity;
import Di.Pierro.infrastructure.persistence.actor.JpaActorRepositoryAdapter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaPersonRepositoryAdapter implements PersonRepository {
    JpaActorRepositoryAdapter jpaActorRepositoryAdapter;
    JpaPersonRepository jpaPersonRepository;

    public JpaPersonRepositoryAdapter(JpaActorRepositoryAdapter actorRepositoryAdapter, JpaPersonRepository jpaPersonRepository) {
        this.jpaActorRepositoryAdapter = actorRepositoryAdapter;
        this.jpaPersonRepository = jpaPersonRepository;
    }

    @Override
    public void save(Person person, UUID actorId) {
        Optional<Actor> optionalActor = findActorById(actorId);
        if(optionalActor.isPresent()){
            person.setActor(optionalActor.get());

            ActorEntity actor = new ActorEntity(
                    person.getActor().getId(),
                    person.getActor().getAddress(),
                    person.getActor().getCreatedAt(),
                    person.getActor().getUpdatedAt(),
                    person.getActor().isActive()
            );

            jpaPersonRepository.save(
                    new PersonEntity(
                            person.getId(),
                            person.getCompleteName(),
                            person.getCpf(),
                            person.getGender(),
                            person.getPhoneNumber(),
                            person.getEmail(),
                            actor
            ));
        }

    }

    @Override
    public Optional<Person> findById(UUID id) {
        return jpaPersonRepository.findById(id).map(
        personEntity ->
                new Person(
                personEntity.getId(),
                personEntity.getCompleteName(),
                personEntity.getCpf(),
                personEntity.getGender(),
                personEntity.getPhoneNumber(),
                personEntity.getEmail(),
                new Actor(
                        personEntity.getActor().getId(),
                        personEntity.getActor().getAddress(),
                        personEntity.getActor().getCreatedAt(),
                        personEntity.getActor().getUpdatedAt(),
                        personEntity.getActor().isActive()
                )
                )
        );
    }

    @Override
    public List<Person> findAll() {
        return jpaPersonRepository
            .findAll()
            .stream()
            .map(personEntity ->
                new Person(
                    personEntity.getId(),
                    personEntity.getCompleteName(),
                    personEntity.getCpf(),
                    personEntity.getGender(),
                    personEntity.getPhoneNumber(),
                    personEntity.getEmail(),
                    new Actor(
                        personEntity.getActor().getId(),
                        personEntity.getActor().getAddress(),
                        personEntity.getActor().getCreatedAt(),
                        personEntity.getActor().getUpdatedAt(),
                        personEntity.getActor().isActive()
                    )
                )
        ).toList();
    }
    private Optional<Actor> findActorById(UUID id){
        return jpaActorRepositoryAdapter.findById(id);
    }

}

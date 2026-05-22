package Di.Pierro.infrastructure.persistence.person;

import Di.Pierro.application.port.output.person.PersonRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Person;
import Di.Pierro.infrastructure.entity.ActorEntity;
import Di.Pierro.infrastructure.entity.PersonEntity;
import Di.Pierro.infrastructure.persistence.actor.ActorRepositoryAdapter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PersonRepositoryAdapter implements PersonRepository {
    ActorRepositoryAdapter actorRepositoryAdapter;
    JpaPersonRepository jpaPersonRepository;

    public PersonRepositoryAdapter(ActorRepositoryAdapter actorRepositoryAdapter, JpaPersonRepository jpaPersonRepository) {
        this.actorRepositoryAdapter = actorRepositoryAdapter;
        this.jpaPersonRepository = jpaPersonRepository;
    }

    @Override
    public void save(Person person, UUID actorId) {
        Optional<Actor> optionalActor = findActor(actorId);
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
    public Optional<Person> findPersonById(UUID id) {
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
    public List<Person> findAllPersons() {
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
    private Optional<Actor> findActor(UUID id){
        return actorRepositoryAdapter.getActorById(id);
    }

}

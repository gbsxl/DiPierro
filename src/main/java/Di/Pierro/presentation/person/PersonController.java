package Di.Pierro.presentation.person;

import Di.Pierro.application.dto.person.CreatePersonInput;
import Di.Pierro.application.port.input.PersonUseCases;
import Di.Pierro.domain.model.Person;
import Di.Pierro.infrastructure.exception.custom.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    private final PersonUseCases personUseCases;

    public PersonController(PersonUseCases personUseCases) {
        this.personUseCases = personUseCases;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreatePersonInput personInput) {
        log.info("Creating person");
        personUseCases.createPerson(personInput);
        log.info("Person created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable @NotNull UUID id) {
        log.debug("Finding person by id={}", id);
        return personUseCases.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ResourceNotFoundException.of("person.not-found", "Person", id));
    }

    @GetMapping
    public ResponseEntity<List<Person>> findAll() {
        log.debug("Fetching all persons");
        return ResponseEntity.ok(personUseCases.findAll());
    }

    @PostMapping("/batch/ids")
    public ResponseEntity<List<Person>> findAllByIds(@RequestBody List<UUID> ids) {
        log.debug("Fetching persons by batch ids count={}", ids.size());
        return ResponseEntity.ok(personUseCases.findAllByIds(ids));
    }

    @GetMapping("/{actorId}/actorId")
    public ResponseEntity<Person> findByActorId(@PathVariable @NotNull UUID actorId) {
        log.debug("Finding person by actorId={}", actorId);
        return personUseCases.findByActorId(actorId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ResourceNotFoundException.of("person.not-found", "Person", actorId));
    }

    @PostMapping("/batch/actor-ids")
    public ResponseEntity<List<Person>> findByActorIds(@RequestBody List<UUID> actorIds) {
        log.debug("Fetching persons by batch actorIds count={}", actorIds.size());
        return ResponseEntity.ok(personUseCases.findByActorIds(actorIds));
    }

    @GetMapping("/{string}/completeName")
    public ResponseEntity<List<Person>> findByCompleteName(@PathVariable String string) {
        log.debug("Searching persons by completeName='{}'", string);
        return ResponseEntity.ok(personUseCases.findByCompleteName(string));
    }

    @GetMapping("/{string}/cpf")
    public ResponseEntity<List<Person>> findByCpf(@PathVariable String string) {
        log.debug("Searching persons by CPF pattern='{}'", string);
        return ResponseEntity.ok(personUseCases.findByCPF(string));
    }

    @GetMapping("/{string}/gender")
    public ResponseEntity<List<Person>> findByGender(@PathVariable String string) {
        log.debug("Searching persons by gender='{}'", string);
        return ResponseEntity.ok(personUseCases.findByGender(string));
    }

    @GetMapping("/{string}/email")
    public ResponseEntity<List<Person>> findByEmail(@PathVariable String string) {
        log.debug("Searching persons by email='{}'", string);
        return ResponseEntity.ok(personUseCases.findByEmail(string));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> update(@PathVariable @NotNull UUID id, @RequestBody @Valid CreatePersonInput person) {
        log.info("Updating person id={}", id);
        return ResponseEntity.ok(personUseCases.updateById(id, person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NotNull UUID id) {
        log.info("Deleting person id={}", id);
        personUseCases.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}

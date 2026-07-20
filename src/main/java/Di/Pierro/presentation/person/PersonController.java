package Di.Pierro.presentation.person;

import Di.Pierro.application.dto.person.CreatePersonInput;
import Di.Pierro.application.port.input.PersonUseCases;
import Di.Pierro.domain.model.Person;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonUseCases personUseCases;

    public PersonController(PersonUseCases personUseCases) {
        this.personUseCases = personUseCases;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreatePersonInput personInput) {
        personUseCases.createPerson(personInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable @NotNull UUID id) {
        Optional<Person> optionalPerson = personUseCases.findById(id);
        return optionalPerson.map(person -> ResponseEntity.ok(optionalPerson.get())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Person>> findAll() {
        return ResponseEntity.ok(personUseCases.findAll());
    }

    @GetMapping("/{actorId}/actorId")
    public ResponseEntity<Person> findByActorId(@PathVariable @NotNull UUID actorId){
        Optional<Person> optionalPerson = personUseCases.findByActorId(actorId);
        return optionalPerson.map(person -> ResponseEntity.ok(optionalPerson.get())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{string}/completeName")
    public ResponseEntity<List<Person>> findByCompleteName(@PathVariable String string){
        List<Person> personList = personUseCases.findByCompleteName(string);
        return ResponseEntity.ok(personList);
    }

    @GetMapping("/{string}/cpf")
    public ResponseEntity<List<Person>> findByCpf(@PathVariable String string){
        List<Person> personList = personUseCases.findByCPF(string);
        return ResponseEntity.ok(personList);
    }

    @GetMapping("/{string}/gender")
    public ResponseEntity<List<Person>> findByGender(@PathVariable String string){
        List<Person> personList = personUseCases.findByGender(string);
        return ResponseEntity.ok(personList);
    }

    @GetMapping("/{string}/email")
    public ResponseEntity<List<Person>> findByEmail(@PathVariable String string){
        List<Person> personList = personUseCases.findByEmail(string);
        return ResponseEntity.ok(personList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> update(@PathVariable @NotNull UUID id, @RequestBody CreatePersonInput person){
        return ResponseEntity.ok(personUseCases.updateById(id, person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NotNull UUID id){
        personUseCases.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}

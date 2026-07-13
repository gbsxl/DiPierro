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
}

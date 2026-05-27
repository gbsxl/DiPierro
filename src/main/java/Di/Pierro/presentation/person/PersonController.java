package Di.Pierro.presentation.person;

import Di.Pierro.application.dto.person.CreatePersonInput;
import Di.Pierro.application.usecase.person.CreatePersonUseCase;
import Di.Pierro.application.usecase.person.GetPersonByIdUseCase;
import Di.Pierro.application.usecase.person.SearchPersonUseCase;
import Di.Pierro.domain.model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/person")
public class PersonController {
    CreatePersonUseCase createPersonUseCase;
    SearchPersonUseCase searchPersonUseCase;
    GetPersonByIdUseCase getPersonByIdUseCase;

    public PersonController(CreatePersonUseCase createPersonUseCase, SearchPersonUseCase searchPersonUseCase, GetPersonByIdUseCase getPersonByIdUseCase) {
        this.createPersonUseCase = createPersonUseCase;
        this.searchPersonUseCase = searchPersonUseCase;
        this.getPersonByIdUseCase = getPersonByIdUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CreatePersonInput personInput){
        createPersonUseCase.execute(personInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable UUID id){
        Optional<Person> optionalPerson = getPersonByIdUseCase.execute(id);
        return optionalPerson.map( person -> ResponseEntity.ok(optionalPerson.get())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Person>> findAll(){
        return ResponseEntity.ok(searchPersonUseCase.execute());
    }
}

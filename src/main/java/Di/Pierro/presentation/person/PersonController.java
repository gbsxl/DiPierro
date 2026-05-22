package Di.Pierro.presentation.person;

import Di.Pierro.application.port.input.person.CreatePersonUseCase;
import Di.Pierro.application.port.input.person.FindAllPersonsUseCase;
import Di.Pierro.application.port.input.person.FindPersonByIdUseCase;
import Di.Pierro.application.dto.person.CreatePersonInput;
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
    FindAllPersonsUseCase findAllPersonsUseCase;
    FindPersonByIdUseCase findPersonByIdUseCase;

    public PersonController(CreatePersonUseCase createPersonUseCase, FindAllPersonsUseCase findAllPersonsUseCase, FindPersonByIdUseCase findPersonByIdUseCase) {
        this.createPersonUseCase = createPersonUseCase;
        this.findAllPersonsUseCase = findAllPersonsUseCase;
        this.findPersonByIdUseCase = findPersonByIdUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody CreatePersonRequest personRequest){
        CreatePersonInput createPersonInput = new CreatePersonInput(
                personRequest.completeName(),
                personRequest.cpf(),
                personRequest.gender(),
                personRequest.phoneNumber(),
                personRequest.email(),
                personRequest.actorId()
        );
        createPersonUseCase.execute(createPersonInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonFoundResponse> findById(@PathVariable UUID id){
        Optional<Person> optionalPerson = findPersonByIdUseCase.execute(id);
        return optionalPerson.map( person -> ResponseEntity.ok(new PersonFoundResponse(
                person.getId(),
                person.getCompleteName(),
                person.getCpf(),
                person.getGender(),
                person.getPhoneNumber(),
                person.getEmail(),
                person.getActor()
            ))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PersonFoundResponse>> findAll(){
        return ResponseEntity.ok(
                findAllPersonsUseCase.execute()
                        .stream()
                        .map(
                        person -> new PersonFoundResponse(
                                person.getId(),
                                person.getCompleteName(),
                                person.getCpf(),
                                person.getGender(),
                                person.getPhoneNumber(),
                                person.getEmail(),
                                person.getActor()
                        ))
                        .toList()
        );
    }
}

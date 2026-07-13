package Di.Pierro.presentation.redflag;

import Di.Pierro.application.dto.redflag.CreateRedFlagInput;
import Di.Pierro.application.port.input.RedFlagUseCases;
import Di.Pierro.domain.model.RedFlag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/red-flag")
public class RedFlagController {
    private final RedFlagUseCases redFlagUseCases;

    public RedFlagController(RedFlagUseCases redFlagUseCases) {
        this.redFlagUseCases = redFlagUseCases;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreateRedFlagInput redFlagInput) {
        redFlagUseCases.createRedFlag(redFlagInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RedFlag> findById(@PathVariable @NotNull UUID id) {
        Optional<RedFlag> optionalRedFlag = redFlagUseCases.findById(id);
        return optionalRedFlag.map(redFlag -> ResponseEntity.ok(optionalRedFlag.get())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RedFlag>> findAll() {
        return ResponseEntity.ok(redFlagUseCases.findAll());
    }
}

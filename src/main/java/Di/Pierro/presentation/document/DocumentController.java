package Di.Pierro.presentation.document;

import Di.Pierro.application.dto.document.CreateDocumentInput;
import Di.Pierro.application.port.input.DocumentUseCases;
import Di.Pierro.domain.model.Document;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/document")
public class DocumentController {
    private final DocumentUseCases documentUseCases;

    public DocumentController(DocumentUseCases documentUseCases) {
        this.documentUseCases = documentUseCases;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreateDocumentInput documentInput) {
        documentUseCases.createDocument(documentInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> findById(@PathVariable @NotNull UUID id) {
        Optional<Document> optionalDocument = documentUseCases.findById(id);
        return optionalDocument.map(document -> ResponseEntity.ok(optionalDocument.get())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Document>> findAll() {
        return ResponseEntity.ok(documentUseCases.findAll());
    }
}

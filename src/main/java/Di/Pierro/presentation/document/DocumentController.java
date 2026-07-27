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

    @GetMapping("/{publicProcurementId}/publicProcurementId")
    public ResponseEntity<List<Document>> findByPublicProcurementId(@PathVariable @NotNull UUID publicProcurementId) {
        return ResponseEntity.ok(documentUseCases.findByPublicProcurementId(publicProcurementId));
    }

    @GetMapping("/{string}/name")
    public ResponseEntity<List<Document>> findByName(@PathVariable String string) {
        return ResponseEntity.ok(documentUseCases.findByName(string));
    }

    @GetMapping("/{string}/type")
    public ResponseEntity<List<Document>> findByType(@PathVariable String string) {
        return ResponseEntity.ok(documentUseCases.findByType(string));
    }

    @GetMapping("/extracted/{extracted}")
    public ResponseEntity<List<Document>> findByExtracted(@PathVariable boolean extracted) {
        return ResponseEntity.ok(documentUseCases.findByExtracted(extracted));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> update(@PathVariable @NotNull UUID id, @RequestBody CreateDocumentInput document) {
        return ResponseEntity.ok(documentUseCases.updateById(id, document));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable @NotNull UUID id) {
        documentUseCases.deleteById(id);
        return ResponseEntity.accepted().build();
    }
}

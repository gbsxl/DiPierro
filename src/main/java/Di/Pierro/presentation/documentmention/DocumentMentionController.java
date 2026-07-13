package Di.Pierro.presentation.documentmention;

import Di.Pierro.application.dto.documentmention.CreateDocumentMentionInput;
import Di.Pierro.application.port.input.DocumentMentionUseCases;
import Di.Pierro.domain.model.DocumentMention;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/document-mention")
public class DocumentMentionController {
    private final DocumentMentionUseCases documentMentionUseCases;

    public DocumentMentionController(DocumentMentionUseCases documentMentionUseCases) {
        this.documentMentionUseCases = documentMentionUseCases;
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreateDocumentMentionInput documentMentionInput) {
        documentMentionUseCases.createDocumentMention(documentMentionInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentMention> findById(@PathVariable @NotNull UUID id) {
        Optional<DocumentMention> optionalDocumentMention = documentMentionUseCases.findById(id);
        return optionalDocumentMention.map(documentMention -> ResponseEntity.ok(optionalDocumentMention.get())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DocumentMention>> findAll() {
        return ResponseEntity.ok(documentMentionUseCases.findAll());
    }
}

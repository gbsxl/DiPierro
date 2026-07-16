package Di.Pierro.application.port.usecase.documentmention;

import Di.Pierro.application.dto.documentmention.CreateDocumentMentionInput;
import Di.Pierro.application.port.input.DocumentMentionUseCases;
import Di.Pierro.application.port.output.DocumentMentionRepository;
import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.model.Document;
import Di.Pierro.domain.model.DocumentMention;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DocumentMentionUseCasesImplementation implements DocumentMentionUseCases {
    private final DocumentMentionRepository documentMentionRepository;

    public DocumentMentionUseCasesImplementation(DocumentMentionRepository documentMentionRepository) {
        this.documentMentionRepository = documentMentionRepository;
    }

    @Override
    public void createDocumentMention(CreateDocumentMentionInput createDocumentMentionInput) {
        DocumentMention documentMention = DocumentMention.createDocumentMention(
                createDocumentMentionInput.role(),
                createDocumentMentionInput.confidence(),
                createDocumentMentionInput.extractedName(),
                new Document(),
                new Actor()
        );
        documentMentionRepository.save(documentMention, createDocumentMentionInput.documentId(), createDocumentMentionInput.actorId());
    }

    @Override
    public List<DocumentMention> findAll() {
        return documentMentionRepository.findAll();
    }

    @Override
    public Optional<DocumentMention> findById(UUID id) {
        return documentMentionRepository.findById(id);
    }
}

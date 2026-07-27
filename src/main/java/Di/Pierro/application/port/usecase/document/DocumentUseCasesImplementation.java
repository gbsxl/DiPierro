package Di.Pierro.application.port.usecase.document;

import Di.Pierro.application.dto.document.CreateDocumentInput;
import Di.Pierro.application.port.input.DocumentUseCases;
import Di.Pierro.application.port.output.DocumentRepository;
import Di.Pierro.domain.model.Document;
import Di.Pierro.domain.model.PublicProcurement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DocumentUseCasesImplementation implements DocumentUseCases {
    private final DocumentRepository documentRepository;

    public DocumentUseCasesImplementation(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public void createDocument(CreateDocumentInput createDocumentInput) {
        Document document = Document.createDocument(
                createDocumentInput.name(),
                createDocumentInput.type(),
                createDocumentInput.filePath(),
                createDocumentInput.hash(),
                createDocumentInput.extracted(),
                new PublicProcurement()
        );
        documentRepository.save(document, createDocumentInput.publicProcurementId());
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Optional<Document> findById(UUID id) {
        return documentRepository.findById(id);
    }

    @Override
    public List<Document> findByPublicProcurementId(UUID id) {
        return documentRepository.findByPublicProcurementId(id);
    }

    @Override
    public List<Document> findByName(String string) {
        return documentRepository.findByName(string);
    }

    @Override
    public List<Document> findByType(String string) {
        return documentRepository.findByType(string);
    }

    @Override
    public List<Document> findByExtracted(boolean extracted) {
        return documentRepository.findByExtracted(extracted);
    }

    @Override
    public Document updateById(UUID id, CreateDocumentInput document) {
        return documentRepository.updateById(id, document);
    }

    @Override
    public void deleteById(UUID id) {
        documentRepository.deleteById(id);
    }
}

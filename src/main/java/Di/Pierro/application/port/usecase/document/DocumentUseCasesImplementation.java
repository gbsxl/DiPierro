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
    DocumentRepository documentRepository;

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
}

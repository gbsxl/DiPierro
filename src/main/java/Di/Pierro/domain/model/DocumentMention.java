package Di.Pierro.domain.model;

import java.util.UUID;

public class DocumentMention {
    private UUID id;
    private String role;
    private Integer confidence;
    private String extractedName;
    private Document document;
    private Actor actor;

    public static DocumentMention createDocumentMention(
            String role,
            Integer confidence,
            String extractedName,
            Document document,
            Actor actor
    ) {
        return new DocumentMention(
                UUID.randomUUID(),
                role,
                confidence,
                extractedName,
                document,
                actor
        );
    }

    public DocumentMention() {
    }

    public DocumentMention(UUID id, String role, Integer confidence, String extractedName, Document document, Actor actor) {
        this.id = id;
        this.role = role;
        this.confidence = confidence;
        this.extractedName = extractedName;
        this.document = document;
        this.actor = actor;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    public String getExtractedName() {
        return extractedName;
    }

    public void setExtractedName(String extractedName) {
        this.extractedName = extractedName;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}

package Di.Pierro.domain.model;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Document {
    private UUID id;
    private String name;
    private String type;
    private String filePath;
    private String hash;
    private boolean extracted;
    private OffsetDateTime createdAt;
    private PublicProcurement publicProcurement;

    public static Document createDocument(
            String name,
            String type,
            String filePath,
            String hash,
            boolean extracted,
            PublicProcurement publicProcurement
    ) {
        return new Document(
                UUID.randomUUID(),
                name,
                type,
                filePath,
                hash,
                extracted,
                OffsetDateTime.now(ZoneOffset.UTC),
                publicProcurement
        );
    }

    public Document() {
    }

    public Document(UUID id, String name, String type, String filePath, String hash, boolean extracted, OffsetDateTime createdAt, PublicProcurement publicProcurement) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.filePath = filePath;
        this.hash = hash;
        this.extracted = extracted;
        this.createdAt = createdAt;
        this.publicProcurement = publicProcurement;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isExtracted() {
        return extracted;
    }

    public void setExtracted(boolean extracted) {
        this.extracted = extracted;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public PublicProcurement getPublicProcurement() {
        return publicProcurement;
    }

    public void setPublicProcurement(PublicProcurement publicProcurement) {
        this.publicProcurement = publicProcurement;
    }
}

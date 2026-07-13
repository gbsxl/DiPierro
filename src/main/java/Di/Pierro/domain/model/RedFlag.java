package Di.Pierro.domain.model;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class RedFlag {
    private UUID id;
    private String type;
    private Integer severity;
    private String description;
    private OffsetDateTime detectedAt;
    private Actor actor;
    private PublicProcurement publicProcurement;
    private UUID transactionId;
    private UUID associationId;

    public static RedFlag createRedFlag(
            String type,
            Integer severity,
            String description,
            Actor actor,
            PublicProcurement publicProcurement,
            UUID transactionId,
            UUID associationId
    ) {
        return new RedFlag(
                UUID.randomUUID(),
                type,
                severity,
                description,
                OffsetDateTime.now(ZoneOffset.UTC),
                actor,
                publicProcurement,
                transactionId,
                associationId
        );
    }

    public RedFlag(UUID id, String type, Integer severity, String description, OffsetDateTime detectedAt, Actor actor, PublicProcurement publicProcurement, UUID transactionId, UUID associationId) {
        this.id = id;
        this.type = type;
        this.severity = severity;
        this.description = description;
        this.detectedAt = detectedAt;
        this.actor = actor;
        this.publicProcurement = publicProcurement;
        this.transactionId = transactionId;
        this.associationId = associationId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(OffsetDateTime detectedAt) {
        this.detectedAt = detectedAt;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public PublicProcurement getPublicProcurement() {
        return publicProcurement;
    }

    public void setPublicProcurement(PublicProcurement publicProcurement) {
        this.publicProcurement = publicProcurement;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getAssociationId() {
        return associationId;
    }

    public void setAssociationId(UUID associationId) {
        this.associationId = associationId;
    }
}

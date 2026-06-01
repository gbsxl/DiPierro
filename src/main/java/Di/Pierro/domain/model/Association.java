package Di.Pierro.domain.model;

import Di.Pierro.domain.enums.AssociationType;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Association {
    private UUID id;
    private AssociationType associationType;
    private String source;
    private int confidenceLevel;
    private boolean associationEnded;
    private OffsetDateTime associationStart;
    private OffsetDateTime associationEnd;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Actor firstActor;
    private Actor secondActor;


    public static Association createAssociation(
            AssociationType associationType,
            String source,
            int confidenceLevel,
            boolean associationEnded,
            OffsetDateTime associationStart
    ){
        return new Association(
                UUID.randomUUID(),
                associationType,
                source,
                confidenceLevel,
                associationEnded,
                associationStart,
                null,
                OffsetDateTime.now(ZoneOffset.UTC),
                OffsetDateTime.now(ZoneOffset.UTC),
                new Actor(),
                new Actor()
        );
    }

    public Association(UUID id, AssociationType associationType, String source, int confidenceLevel, boolean associationEnded, OffsetDateTime associationStart, OffsetDateTime associationEnd, OffsetDateTime createdAt, OffsetDateTime updatedAt, Actor firstActor, Actor secondActor) {
        this.id = id;
        this.associationType = associationType;
        this.source = source;
        this.confidenceLevel = confidenceLevel;
        this.associationEnded = associationEnded;
        this.associationStart = associationStart;
        this.associationEnd = associationEnd;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.firstActor = firstActor;
        this.secondActor = secondActor;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AssociationType getAssociationType() {
        return associationType;
    }

    public void setAssociationType(AssociationType associationType) {
        this.associationType = associationType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(int confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public OffsetDateTime getAssociationStart() {
        return associationStart;
    }

    public void setAssociationStart(OffsetDateTime associationStart) {
        this.associationStart = associationStart;
    }

    public OffsetDateTime getAssociationEnd() {
        return associationEnd;
    }

    public void setAssociationEnd(OffsetDateTime associationEnd) {
        this.associationEnd = associationEnd;
    }

    public boolean isAssociationEnded() {
        return associationEnded;
    }

    public void setAssociationEnded(boolean associationEnded) {
        this.associationEnded = associationEnded;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Actor getFirstActor() {
        return firstActor;
    }

    public void setFirstActor(Actor firstActor) {
        this.firstActor = firstActor;
    }

    public Actor getSecondActor() {
        return secondActor;
    }

    public void setSecondActor(Actor secondActor) {
        this.secondActor = secondActor;
    }
}

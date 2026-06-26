package Di.Pierro.domain.model;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Actor{
    private UUID id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private boolean active;

    public static Actor createActor(){
        return new Actor(
                UUID.randomUUID(),
                OffsetDateTime.now(ZoneOffset.UTC),
                OffsetDateTime.now(ZoneOffset.UTC),
                true
        );
    }

    public Actor(UUID id, OffsetDateTime createdAt, OffsetDateTime updatedAt, boolean active) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public Actor() {
        this.id = null;
        this.createdAt = null;
        this.updatedAt = null;
        this.active = true;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

package Di.Pierro.domain.entity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Actor{
    private UUID id;
    private String address;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private boolean isActive;

    public Actor(String address) {
        this.id = UUID.randomUUID();
        this.address = address;
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.isActive = true;
    }

    public Actor(UUID id, String address, OffsetDateTime createdAt, OffsetDateTime updatedAt, boolean isActive) {
        this.id = id;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
    }

    public UUID getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return isActive;
    }
}

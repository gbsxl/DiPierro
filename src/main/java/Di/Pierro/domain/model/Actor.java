package Di.Pierro.domain.model;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Actor{
    private UUID id;
    private String address;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private boolean active;

    public static Actor createActor(String address){
        return new Actor(
                UUID.randomUUID(),
                address,
                OffsetDateTime.now(ZoneOffset.UTC),
                OffsetDateTime.now(ZoneOffset.UTC),
                true
        );
    }

    public Actor(UUID id, String address, OffsetDateTime createdAt, OffsetDateTime updatedAt, boolean active) {
        this.id = id;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    public Actor() {
        this.id = null;
        this.address = null;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

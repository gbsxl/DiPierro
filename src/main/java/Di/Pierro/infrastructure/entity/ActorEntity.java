package Di.Pierro.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity(name = "actors")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActorEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "address")
    private String address;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "is_active")
    private boolean isActive;
}
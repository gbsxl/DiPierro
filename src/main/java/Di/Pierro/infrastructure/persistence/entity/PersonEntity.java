package Di.Pierro.infrastructure.persistence.entity;

import Di.Pierro.domain.enums.Gender;
import Di.Pierro.domain.model.Actor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "people")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PersonEntity extends Actor {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "complete_name", nullable = false, length = 255)
    private String completeName;

    @Column(name = "cpf", length = 11, unique = true)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "is_alive")
    @Builder.Default
    private Boolean isAlive = true;

    @Column(name = "death_date")
    private LocalDate deathDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "actors_id", nullable = false, unique = true)
    private ActorEntity actor;

    public AddressEntity getAddress() {
        return actor != null ? actor.getAddress() : null;
    }

    public void setAddress(AddressEntity address) {
        if (actor != null) {
            actor.setAddress(address);
            if (address != null) {
                address.setActor(actor);
            }
        }
    }
}

package Di.Pierro.infrastructure.persistence.entity;

import Di.Pierro.domain.enums.State;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddressEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "postal_code", length = 8, nullable = false)
    private String postalCode;

    @Column(name = "street_address", length = 255)
    private String streetAddress;

    @Column(name = "number", length = 5)
    private String number;

    @Column(name = "complement", length = 155)
    private String complement;

    @Column(name = "neighborhood", length = 100)
    private String neighborhood;

    @Column(name = "city", length = 100)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 20)
    private State state;

    @OneToOne
    @JoinColumn(name = "actors_id", nullable = false, unique = true)
    private ActorEntity actor;
}

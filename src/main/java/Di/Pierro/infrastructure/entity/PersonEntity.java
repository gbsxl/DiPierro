package Di.Pierro.infrastructure.entity;

import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "people")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonEntity extends Actor {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "complete_name", nullable = false, length = 70)
    private String completeName;

    @Column(name = "cpf", nullable = false, length = 11, unique = true)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email", length = 150)
    private String email;

    @OneToOne
    @JoinColumn(name = "actors_id", nullable = false, unique = true)
    private ActorEntity actor;
}

package Di.Pierro.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "business")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusinessEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "legal_name", nullable = false)
    private String legalName;

    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Column(name = "fantasy_name", length = 70)
    private String fantasyName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "is_public_company")
    private boolean isPublicCompany;

    @OneToOne
    @JoinColumn(name = "actors_id", nullable = false, unique = true)
    private ActorEntity actor;
}

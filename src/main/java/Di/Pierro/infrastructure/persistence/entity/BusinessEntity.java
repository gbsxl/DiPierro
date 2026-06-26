package Di.Pierro.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "business")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BusinessEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "legal_name", nullable = false, length = 255)
    private String legalName;

    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Column(name = "fantasy_name", length = 255)
    private String fantasyName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "is_public_company")
    private boolean isPublicCompany;

    @Column(name = "address")
    private String address;

    @Column(name = "capital_stock")
    private BigDecimal capitalStock;

    @Column(name = "estimated_net_worth")
    private BigDecimal estimatedNetWorth;

    @OneToOne
    @JoinColumn(name = "actors_id", nullable = false, unique = true)
    private ActorEntity actor;
}

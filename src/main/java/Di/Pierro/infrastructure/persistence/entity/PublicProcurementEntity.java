package Di.Pierro.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "public_procurement")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PublicProcurementEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "public_procurement_number", nullable = false, unique = true, length = 50)
    private String publicProcurementNumber;

    @Column(name = "process_number", unique = true, length = 50)
    private String processNumber;

    @Column(name = "object", nullable = false)
    private String object;

    @Column(name = "modality", nullable = false, length = 100)
    private String modality;

    @Column(name = "situation", length = 100)
    private String situation;

    @Column(name = "legal_instrument", length = 100)
    private String legalInstrument;

    @Column(name = "estimated_value")
    private BigDecimal estimatedValue;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "opening_date")
    private LocalDate openingDate;

    @Column(name = "designated_contact", length = 255)
    private String designatedContact;

    @Column(name = "ibge_city_code", length = 7)
    private String ibgeCityCode;

    @Column(name = "federative_unit_acronym", length = 2)
    private String federativeUnitAcronym;

    @Column(name = "managing_unity_code", length = 20)
    private String managingUnityCode;

    @Column(name = "cnpj_government_agency", length = 14)
    private String cnpjGovernmentAgency;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "actors_id", nullable = false, unique = true)
    private ActorEntity actor;
}

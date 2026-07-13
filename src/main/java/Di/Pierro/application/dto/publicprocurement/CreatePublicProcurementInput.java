package Di.Pierro.application.dto.publicprocurement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreatePublicProcurementInput(
        @NotBlank
        @NotNull
        @Size(max = 50)
        String publicProcurementNumber,

        @Size(max = 50)
        String processNumber,

        @NotBlank
        @NotNull
        String object,

        @NotBlank
        @NotNull
        @Size(max = 100)
        String modality,

        @Size(max = 100)
        String situation,

        @Size(max = 100)
        String legalInstrument,

        BigDecimal estimatedValue,

        LocalDate publicationDate,

        LocalDate openingDate,

        @Size(max = 255)
        String designatedContact,

        @Size(max = 7)
        String ibgeCityCode,

        @Size(min = 2, max = 2)
        String federativeUnitAcronym,

        @Size(max = 20)
        String managingUnityCode,

        @Size(max = 14)
        String cnpjGovernmentAgency,

        @NotNull
        UUID actorId
) {
}

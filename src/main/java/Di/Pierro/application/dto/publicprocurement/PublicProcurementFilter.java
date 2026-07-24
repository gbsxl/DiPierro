package Di.Pierro.application.dto.publicprocurement;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PublicProcurementFilter(

        @Size(max = 50, message = "publicProcurementNumber cannot exceed 50 characters")
        String publicProcurementNumber,

        @Size(max = 50, message = "processNumber cannot exceed 50 characters")
        String processNumber,

        @Size(max = 500, message = "object cannot exceed 500 characters")
        String object,

        @Size(max = 100, message = "modality cannot exceed 100 characters")
        String modality,

        @Size(max = 100, message = "situation cannot exceed 100 characters")
        String situation,

        @Size(max = 100, message = "legalInstrument cannot exceed 100 characters")
        String legalInstrument,

        @DecimalMin(
                value = "0.0",
                inclusive = true,
                message = "estimatedValue cannot be negative"
        )
        BigDecimal estimatedValue,

        LocalDate publicationDate,

        LocalDate openingDate,

        @Size(max = 255, message = "designatedContact cannot exceed 255 characters")
        String designatedContact,

        @Pattern(
                regexp = "^\\d{7}$",
                message = "ibgeCityCode must contain exactly 7 digits"
        )
        String ibgeCityCode,

        @Pattern(
                regexp = "^[A-Z]{2}$",
                message = "federativeUnitAcronym must contain exactly 2 uppercase letters"
        )
        String federativeUnitAcronym,

        @Size(max = 50, message = "managingUnityCode cannot exceed 50 characters")
        String managingUnityCode,

        @Pattern(
                regexp = "^\\d{14}$",
                message = "cnpjGovernmentAgency must contain exactly 14 digits"
        )
        String cnpjGovernmentAgency

) {
}
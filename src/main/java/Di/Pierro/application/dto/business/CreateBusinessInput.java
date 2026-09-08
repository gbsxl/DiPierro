package Di.Pierro.application.dto.business;

import Di.Pierro.application.dto.address.CreateAddressInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateBusinessInput(
        @NotNull
        @NotBlank
        @Size(max = 255)
        String legalName,

        @NotNull
        @NotBlank
        @Size(min = 14, max = 14)
        String cnpj,

        @Size(max = 255)
        String fantasyName,

        @Size(max = 20)
        String phoneNumber,

        @Email
        @Size(max = 20)
        String email,

        boolean isPublicCompany,

        @Valid
        CreateAddressInput address,

        BigDecimal estimatedNetWorth,

        BigDecimal capitalStock,

        LocalDate openDate
) {

}

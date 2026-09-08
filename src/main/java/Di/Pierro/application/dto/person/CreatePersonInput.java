package Di.Pierro.application.dto.person;

import Di.Pierro.application.dto.address.CreateAddressInput;
import Di.Pierro.application.validators.cpf.CPF;
import Di.Pierro.domain.enums.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreatePersonInput(
        @NotBlank
        @NotNull
        String completeName,

        @Size(min = 11, max = 11)
        @CPF
        String cpf,

        @Valid
        CreateAddressInput address,

        Gender gender,

        @Size(max = 20)
        String phoneNumber,

        @Size(max = 150)
        @Email
        String email,

        Boolean isAlive,

        LocalDate deathDate
) {
}

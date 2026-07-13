package Di.Pierro.application.dto.person;

import Di.Pierro.domain.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreatePersonInput(
        @NotBlank
        @NotNull
        String completeName,

        @Size(min = 11, max = 11)
        String cpf,

        String address,

        Gender gender,

        @Size(max = 20)
        String phoneNumber,

        @Size(max = 150)
        @Email
        String email,

        @NotNull
        UUID actorId
) {
}

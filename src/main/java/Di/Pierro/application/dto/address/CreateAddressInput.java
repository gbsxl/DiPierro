package Di.Pierro.application.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAddressInput(
        @NotNull
        @NotBlank
        @Size(min = 8, max = 8)
        String postalCode,

        @Size(max = 255)
        String streetAddress,

        @Size(max = 5)
        String number,

        @Size(max = 155)
        String complement,

        @Size(max = 100)
        String neighborhood,

        @Size(max = 100)
        String city,

        String state
) {
}

package Di.Pierro.application.dto.person;

import Di.Pierro.domain.enums.Gender;

import java.util.UUID;

public record CreatePersonInput(
        String completeName,
        String cpf,
        Gender gender,
        String phoneNumber,
        String email,
        UUID actorId
) {
}

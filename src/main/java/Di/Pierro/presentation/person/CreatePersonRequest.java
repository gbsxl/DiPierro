package Di.Pierro.presentation.person;

import Di.Pierro.domain.enums.Gender;

import java.util.UUID;

public record CreatePersonRequest(
        String completeName,
        String cpf,
        Gender gender,
        String phoneNumber,
        String email,
        UUID actorId
) {
}

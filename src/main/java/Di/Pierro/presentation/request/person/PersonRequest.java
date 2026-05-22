package Di.Pierro.presentation.request.person;

import Di.Pierro.domain.enums.Gender;

import java.util.UUID;

public record PersonRequest(
        String completeName,
        String cpf,
        Gender gender,
        String phoneNumber,
        String email,
        UUID actorId
) {
}

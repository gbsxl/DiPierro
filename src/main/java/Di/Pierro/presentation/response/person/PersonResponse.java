package Di.Pierro.presentation.response.person;

import Di.Pierro.domain.entity.Actor;
import Di.Pierro.domain.enums.Gender;

import java.util.UUID;

public record PersonResponse(
        UUID id,
        String completeName,
        String cpf,
        Gender gender,
        String phoneNumber,
        String email,
        Actor actor
) {
}

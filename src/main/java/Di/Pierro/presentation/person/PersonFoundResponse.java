package Di.Pierro.presentation.person;

import Di.Pierro.domain.model.Actor;
import Di.Pierro.domain.enums.Gender;

import java.util.UUID;

public record PersonFoundResponse(
        UUID id,
        String completeName,
        String cpf,
        Gender gender,
        String phoneNumber,
        String email,
        Actor actor
) {
}

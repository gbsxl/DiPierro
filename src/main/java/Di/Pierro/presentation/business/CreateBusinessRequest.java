package Di.Pierro.presentation.business;

import java.util.UUID;

public record CreateBusinessRequest(
        String legalName,
        String cnpj,
        String fantasyName,
        String phoneNumber,
        String email,
        boolean isPublicCompany,
        UUID actorId
) {
}

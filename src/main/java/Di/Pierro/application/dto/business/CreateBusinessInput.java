package Di.Pierro.application.dto.business;

import java.util.UUID;

public record CreateBusinessInput(
        String legalName,
        String cnpj,
        String fantasyName,
        String phoneNumber,
        String email,
        boolean isPublicCompany,
        UUID actorId
) {

}

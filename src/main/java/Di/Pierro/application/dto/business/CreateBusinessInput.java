package Di.Pierro.application.dto.business;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateBusinessInput(
        String legalName,
        String cnpj,
        String fantasyName,
        String phoneNumber,
        String email,
        boolean isPublicCompany,
        String address,
        BigDecimal estimatedNetWorth,
        BigDecimal capitalStock,
        UUID actorId
) {

}

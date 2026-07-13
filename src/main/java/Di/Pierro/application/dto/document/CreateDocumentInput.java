package Di.Pierro.application.dto.document;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateDocumentInput(
        @Size(max = 255)
        String name,

        @Size(max = 50)
        String type,

        String filePath,

        @Size(max = 64)
        String hash,

        boolean extracted,

        @NotNull
        UUID publicProcurementId
) {
}

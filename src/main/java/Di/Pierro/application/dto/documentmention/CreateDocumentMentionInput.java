package Di.Pierro.application.dto.documentmention;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateDocumentMentionInput(
        @NotNull
        UUID documentId,

        @NotNull
        UUID actorId,

        @Size(max = 50)
        String role,

        @Min(1)
        @Max(10)
        Integer confidence,

        String extractedName
) {
}

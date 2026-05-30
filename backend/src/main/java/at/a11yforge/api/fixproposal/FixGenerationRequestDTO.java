package at.a11yforge.api.fixproposal;

import jakarta.validation.constraints.NotNull;

public record FixGenerationRequestDTO(

        @NotNull(message = "violationId is required")
        Long violationId
) {
}

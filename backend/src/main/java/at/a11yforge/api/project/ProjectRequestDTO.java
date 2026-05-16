package at.a11yforge.api.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProjectRequestDTO(
        @NotBlank String name,
        @NotBlank String baseUrl,
        @Positive Integer crawlMaxPages
) {}

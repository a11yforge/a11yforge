package at.a11yforge.api.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank String identifier,
        @NotBlank String password
) {}

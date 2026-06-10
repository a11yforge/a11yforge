package at.a11yforge.api.auth;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequestDTO(
        @NotBlank
        String refreshToken
) {
}

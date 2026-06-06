package at.a11yforge.api.passwordreset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetConfirmDTO(
        @NotBlank
        String token,

        @NotBlank
        @Size(min = 8)
        String newPassword
) {
}

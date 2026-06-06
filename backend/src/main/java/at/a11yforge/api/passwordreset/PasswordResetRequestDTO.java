package at.a11yforge.api.passwordreset;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetRequestDTO(
        @NotBlank
        @Email
        @Size(max = 255)
        String email
) {
}

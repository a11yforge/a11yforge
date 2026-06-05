package at.a11yforge.api.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeUserNameRequestDTO(
        @NotBlank
        @Size(max = 100)
        String userName
) {
}

package at.a11yforge.api.user;

import java.time.Instant;

public record UserResponseDTO(Long id, String email, String userName, Instant createdAt) {
}

package at.a11yforge.user;

import java.time.Instant;

public record UserResponseDTO(Long id, String email, String userName, Instant createdAt) {
}

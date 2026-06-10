package at.a11yforge.api.auth;

public record TokenResponseDTO(
        String accessToken,
        String refreshToken,
        String email,
        String userName
) {
}
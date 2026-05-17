package at.a11yforge.api.auth;

public record LoginResponseDTO(
        String token,
        String email,
        String userName
) {}

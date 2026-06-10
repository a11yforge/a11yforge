package at.a11yforge.api.auth;

import at.a11yforge.api.security.JwtUtil;
import at.a11yforge.api.user.InvalidCredentialsException;
import at.a11yforge.api.user.User;
import at.a11yforge.api.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;

@Service
public class AuthService {

    private static final int REFRESH_TOKEN_BYTES = 32;

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Duration refreshTokenValidity;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthService(
            UserService userService,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder,
            RefreshTokenRepository refreshTokenRepository,
            @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs
    ) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenValidity = Duration.ofMillis(refreshExpirationMs);
    }

    public User register(String email, String password, String userName) {
        return userService.register(email, password, userName);
    }

    @Transactional
    public TokenPair login(String identifier, String password) {
        User user = userService.findByIdentifier(identifier);

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = issueRefreshToken(user.getId());
        return new TokenPair(accessToken, refreshToken, user);
    }

    @Transactional
    public TokenPair refresh(String rawRefreshToken) {
        String tokenHash = hash(rawRefreshToken);

        RefreshToken stored = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        // Reuse-Detection: bereits revokter Token wird erneut benutzt -> Diebstahlverdacht
        if (stored.getRevokedAt() != null) {
            refreshTokenRepository.revokeAllActiveByUserId(stored.getUserId(), Instant.now());
            throw new InvalidRefreshTokenException("Refresh token reuse detected");
        }

        if (stored.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidRefreshTokenException("Refresh token expired");
        }

        // Rotation: alten Token revoken, neuen ausstellen
        stored.setRevokedAt(Instant.now());
        refreshTokenRepository.save(stored);

        User user = userService.findById(stored.getUserId());

        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String newRefreshToken = issueRefreshToken(user.getId());
        return new TokenPair(accessToken, newRefreshToken, user);
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        String tokenHash = hash(rawRefreshToken);

        refreshTokenRepository.findByTokenHash(tokenHash).ifPresent(token -> {
            if (token.getRevokedAt() == null) {
                token.setRevokedAt(Instant.now());
                refreshTokenRepository.save(token);
            }
        });
    }

    private String issueRefreshToken(Long userId) {
        String rawToken = generateRawToken();
        Instant now = Instant.now();
        RefreshToken token = new RefreshToken(
                userId,
                hash(rawToken),
                now.plus(refreshTokenValidity),
                now
        );
        refreshTokenRepository.save(token);
        return rawToken;
    }

    private String generateRawToken() {
        byte[] bytes = new byte[REFRESH_TOKEN_BYTES];
        secureRandom.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    private String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}

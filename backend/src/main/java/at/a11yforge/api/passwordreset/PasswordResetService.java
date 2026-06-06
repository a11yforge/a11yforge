package at.a11yforge.api.passwordreset;

import at.a11yforge.api.user.User;
import at.a11yforge.api.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class PasswordResetService {

    private static final Duration TOKEN_VALIDITY = Duration.ofMinutes(30);
    private static final int TOKEN_BYTES = 32;

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetMailSender mailSender;
    private final SecureRandom secureRandom = new SecureRandom();

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            PasswordResetMailSender mailSender
    ) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public void requestReset(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return;
        }
        User user = userOpt.get();

        String rawToken = generateRawToken();
        String tokenHash = hash(rawToken);
        Instant now = Instant.now();

        PasswordResetToken token = new PasswordResetToken(
                user.getId(),
                tokenHash,
                now.plus(TOKEN_VALIDITY),
                now
        );
        tokenRepository.save(token);

        mailSender.sendResetMail(user.getEmail(), rawToken);
    }

    @Transactional
    public void confirmReset(String rawToken, String newPassword) {
        String tokenHash = hash(rawToken);

        PasswordResetToken token = tokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new InvalidPasswordResetTokenException("Invalid or unknown token"));

        if (token.getUsedAt() != null) {
            throw new InvalidPasswordResetTokenException("Token has already been used");
        }
        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidPasswordResetTokenException("Token has expired");
        }

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new InvalidPasswordResetTokenException("Invalid token"));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        token.setUsedAt(Instant.now());
        tokenRepository.save(token);
    }

    private String generateRawToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
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

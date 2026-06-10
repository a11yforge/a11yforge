package at.a11yforge.api.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO request) {
        authService.register(request.email(), request.password(), request.userName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        TokenPair pair = authService.login(request.identifier(), request.password());
        return ResponseEntity.ok(toResponse(pair));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(@Valid @RequestBody RefreshRequestDTO request) {
        TokenPair pair = authService.refresh(request.refreshToken());
        return ResponseEntity.ok(toResponse(pair));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequestDTO request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }

    private TokenResponseDTO toResponse(TokenPair pair) {
        return new TokenResponseDTO(
                pair.accessToken(),
                pair.refreshToken(),
                pair.user().getEmail(),
                pair.user().getUserName()
        );
    }
}

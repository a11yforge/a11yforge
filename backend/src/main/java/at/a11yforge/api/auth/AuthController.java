package at.a11yforge.api.auth;

import at.a11yforge.api.user.User;
import at.a11yforge.api.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO request) {
        userService.register(request.email(), request.password(), request.userName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        String token = userService.login(request.identifier(), request.password());
        User user = userService.findByIdentifier(request.identifier());
        return ResponseEntity.ok(new LoginResponseDTO(token, user.getEmail(), user.getUserName()));
    }
}

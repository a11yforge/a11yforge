package at.a11yforge.api.user;

import at.a11yforge.api.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/me/username")
    public ResponseEntity<UserResponseDTO> changeUserName(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody ChangeUserNameRequestDTO request
    ) {
        UserResponseDTO response =
                userService.changeUserName(
                        principal.getId(), request.userName(), request.currentPassword());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/email")
    public ResponseEntity<UserResponseDTO> changeEmail(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody ChangeEmailRequestDTO request
    ) {
        UserResponseDTO response =
                userService.changeEmail(
                        principal.getId(), request.email(), request.currentPassword());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody ChangePasswordRequestDTO request
    ) {
        userService.changePassword(
                principal.getId(), request.currentPassword(), request.newPassword());
        return ResponseEntity.noContent().build();
    }
}

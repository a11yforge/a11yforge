package at.a11yforge.api.user;

import at.a11yforge.api.security.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(List.of())
                .build();
    }

    public User register(String email, String password, String userName) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("email", email);
        }
        if (userRepository.existsByUserName(userName)) {
            throw new UserAlreadyExistsException("userName", userName);
        }

        User user = new User(email, passwordEncoder.encode(password), userName);
        return userRepository.save(user);
    }

    public String login(String identifier, String password) {
        User user = findByIdentifier(identifier);

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        //return jwtUtil.generateToken(user.getEmail());
        return jwtUtil.generateAccessToken(user.getId());
    }

    public User findByIdentifier(String identifier) {
        if (identifier.contains("@")) {
            return userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
        }
        return userRepository.findByUserName(identifier)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public UserResponseDTO changeUserName(Long userId, String newUserName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.getUserName().equals(newUserName)) {
            if (userRepository.existsByUserName(newUserName)) {
                throw new UserAlreadyExistsException("userName", newUserName);
            }
            user.setUserName(newUserName);
            userRepository.save(user);
        }

        return toResponse(user);
    }

    public UserResponseDTO changeEmail(Long userId, String newEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.getEmail().equals(newEmail)) {
            if (userRepository.existsByEmail(newEmail)) {
                throw new UserAlreadyExistsException("email", newEmail);
            }
            user.setEmail(newEmail);
            userRepository.save(user);
        }

        return toResponse(user);
    }

    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new PasswordMismatchException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getUserName(),
                user.getCreatedAt()
        );
    }
}

package at.a11yforge.api.auth;

import at.a11yforge.api.user.User;

public record TokenPair(String accessToken,
                        String refreshToken,
                        User user
) {
}

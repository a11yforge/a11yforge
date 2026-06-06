package at.a11yforge.api.passwordreset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetMailSender {

    private final JavaMailSender mailSender;
    private final String fromAddress;
    private final String frontendBaseUrl;

    public PasswordResetMailSender(
            JavaMailSender mailSender,
            @Value("${a11yforge.mail.from}") String fromAddress,
            @Value("${a11yforge.frontend.base-url}") String frontendBaseUrl
    ) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
        this.frontendBaseUrl = frontendBaseUrl;
    }

    public void sendResetMail(String toEmail, String token) {
        String resetLink = frontendBaseUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("A11yForge - Passwort zurücksetzen");
        message.setText(
                "Du hast eine Zurücksetzung deines Passworts angefordert.\n\n"
                        + "Öffne den folgenden Link, um ein neues Passwort zu vergeben:\n"
                        + resetLink + "\n\n"
                        + "Der Link ist 30 Minuten gültig.\n"
                        + "Wenn du das nicht warst, ignoriere diese E-Mail."
        );

        mailSender.send(message);
    }
}

package pl.edu.agh.sportsApp.emailsender;


import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.config.YAMLConfig;
import pl.edu.agh.sportsApp.dto.ResponseCode;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.ServerMailException;
import pl.edu.agh.sportsApp.model.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmailSender implements IEmailSender {

    JavaMailSender mailSender;
    String CONFIRMATION_LINK;
    String REGISTER_EMAIL_TITLE;
    YAMLConfig config;

    @Autowired
    public EmailSender(JavaMailSender mailSender,
                       YAMLConfig config,
                       @Value("${app.mail.registertitle}") String REGISTER_EMAIL_TITLE) {
        this.mailSender = mailSender;
        this.config = config;
        this.CONFIRMATION_LINK = config.getAppURL() + "/public/users/confirm/";
        this.REGISTER_EMAIL_TITLE = REGISTER_EMAIL_TITLE;
    }

    @Override
    public void send(String destination, String title, String content, Boolean isHtml)
            throws MessagingException, MailAuthenticationException {

        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(destination);
        helper.setReplyTo(config.getReplyTo());
        helper.setFrom(config.getSetFrom());
        helper.setSubject(title);
        helper.setText(content, isHtml);

        mailSender.send(mail);
    }

    @Override
    public void sendRegisterEmail(User user, String registerToken) {
        try {
            send(user.getEmail(), REGISTER_EMAIL_TITLE, MessageFormat.format(registerConfirmHtml,
                    user.getFirstName(), CONFIRMATION_LINK + registerToken), true);
        } catch (MessagingException | MailAuthenticationException e) {
            throw new ServerMailException(ResponseCode.EMAIL_ERROR.name());
        }
    }

    String registerConfirmHtml =
            "<h1><img src=\"https://www.bls.gov/spotlight/2017/sports-and-exercise/images/cover_image.jpg\" " +
            "alt=\"\" width=\"325\" height=\"183\" style=\"display: block; margin-left: auto; " +
            "margin-right: auto;\" /></h1>\n" +
            "<h2 style=\"text-align: center;\">Hi {0} !</h2>\n" +
            "<h1 style=\"text-align: center;\">Welcome to SportsApp !</h1>\n" +
            "<p></p>\n" +
            "<h4 style=\"text-align: center;\">Thanks for signing up in our application! " +
            "Please confirm your email by clicking the link below.</h4>\n" +
            "<br>\n" +
            "<h3 style=\"text-align: center;\"><a href=\"{1}\" " +
            "\">Click to confirm Your account !</a></h3>\n" +
            "<br>\n" +
            "<br>\n" +
            "<hr />";

}
package pl.edu.agh.sportsApp.emailsender;


import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.config.YAMLConfig;
import pl.edu.agh.sportsApp.emailsender.tokengenerator.TokenGenerator;
import pl.edu.agh.sportsApp.model.Account;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmailSender implements IEmailSender {

    JavaMailSender  mailSender;
    TokenGenerator tokenGenerator;
    String REGISTER_LINK = "/public/users/confirm/";
    String REGISTER_EMAIL_TITLE;
    YAMLConfig config;

    @Autowired
    public EmailSender(JavaMailSender mailSender,
                       TokenGenerator tokenGenerator,
                       YAMLConfig config,
                       @Value("${app.mail.registertitle}") String REGISTER_EMAIL_TITLE) {
        this.mailSender = mailSender;
        this.tokenGenerator = tokenGenerator;
        this.config = config;
        this.REGISTER_EMAIL_TITLE = REGISTER_EMAIL_TITLE;
    }

    @Override
    public void send(String destination, String title, String content)
            throws MessagingException, MailAuthenticationException {

        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(destination);
        helper.setReplyTo(config.getReplyTo());
        helper.setFrom(config.getSetFrom());
        helper.setSubject(title);
        helper.setText(content);

        mailSender.send(mail);
    }

    @Override
    public String sendRegisterEmail(Account newAccount) throws MessagingException, MailAuthenticationException {
        String registerToken = tokenGenerator.generate(newAccount);
        send(newAccount.getEmail(), REGISTER_EMAIL_TITLE, config.getAppURL() + REGISTER_LINK + registerToken);
        return registerToken;
    }
}

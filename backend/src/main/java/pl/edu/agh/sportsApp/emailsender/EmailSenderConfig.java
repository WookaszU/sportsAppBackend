package pl.edu.agh.sportsApp.emailsender;


import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import pl.edu.agh.sportsApp.config.YAMLConfig;

import java.util.Properties;

@Configuration
@FieldDefaults(level= AccessLevel.PRIVATE)
public class EmailSenderConfig {

    @Autowired
    Environment env;

    @Autowired
    YAMLConfig yamlConfig;

    @Bean("javaMailSender")
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("spring.mail.port")));

        mailSender.setUsername(yamlConfig.getUsername());
        mailSender.setPassword(yamlConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return mailSender;
    }


}

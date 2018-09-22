package pl.edu.agh.sportsApp.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class YAMLConfig {

    String environment;
    String login;
    String password;
    String replyTo;
    String setFrom;
    String appURL;

}

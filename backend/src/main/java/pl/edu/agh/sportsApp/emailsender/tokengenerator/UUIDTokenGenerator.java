package pl.edu.agh.sportsApp.emailsender.tokengenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.edu.agh.sportsApp.model.Account;

import java.util.UUID;

@Primary
@Configuration
public class UUIDTokenGenerator implements TokenGenerator{

    @Override
    public String generate(Account account) {
        return UUID.randomUUID().toString();
    }

    @Bean
    public UUIDTokenGenerator uUIDTokenGenerator(){
        return new UUIDTokenGenerator();
    }

}

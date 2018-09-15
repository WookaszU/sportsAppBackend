package pl.edu.agh.sportsApp.emailsender.tokengenerator;


import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.model.token.Token;

public interface TokenGenerator {

    String generate(Account account);

}

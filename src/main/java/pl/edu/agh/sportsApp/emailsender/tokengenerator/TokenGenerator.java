package pl.edu.agh.sportsApp.emailsender.tokengenerator;


import pl.edu.agh.sportsApp.model.User;

public interface TokenGenerator {

    String generate(User user);

}

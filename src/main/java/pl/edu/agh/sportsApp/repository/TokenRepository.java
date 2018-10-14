package pl.edu.agh.sportsApp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.agh.sportsApp.model.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Token findByValue(String tokenValue);

    Optional<Token> findByOwnerEmail(String email);
}

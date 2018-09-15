package pl.edu.agh.sportsApp.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.sportsApp.model.token.Token;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface TokenRepository extends CrudRepository<Token, Integer> {

    Token findByValue(String tokenValue);

    void removeTokenById(Integer tokenId);

    List<Token> findAll();

    Optional<Token> findByRelatedAccountEmail(String email);
}

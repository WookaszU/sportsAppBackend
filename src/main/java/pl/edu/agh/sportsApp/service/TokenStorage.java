package pl.edu.agh.sportsApp.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.model.token.Token;
import pl.edu.agh.sportsApp.repository.TokenRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenStorage {

    @NonNull
    private final TokenRepository tokenRepository;

    public Optional<Token> getTokenByValue(String tokenValue){
        return Optional.ofNullable(tokenRepository.findByValue(tokenValue));
    }

    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    public void removeToken(int id) {
        tokenRepository.removeTokenById(id);
    }

    public Optional<Token> findByRelatedAccountEmail(String email){
        return tokenRepository.findByRelatedAccountEmail(email);
    }

}

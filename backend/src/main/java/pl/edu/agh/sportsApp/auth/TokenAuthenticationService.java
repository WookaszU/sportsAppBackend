package pl.edu.agh.sportsApp.auth;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.service.AccountService;
import pl.edu.agh.sportsApp.service.UserActivityService;
import pl.edu.agh.sportsApp.token.TokenService;

import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Primary
@Service
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class TokenAuthenticationService implements AuthenticationService {

    @NonNull
    TokenService tokenService;
    @NonNull
    UserActivityService userActivityService;
    @NonNull
    AccountService accountService;
    @NonNull
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public Optional<String> login(final String email, final String password) {

        Optional<Account> result = accountService.getAccountByEmail(email);

        if(result.isPresent()) {
            Account account = result.get();
            if (passwordEncoder.matches(password, account.getPassword())) {
                userActivityService.addActiveUser(email, result.get());
                return Optional.of(tokenService.expiringToken(ImmutableMap.of("username", email)));
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Account> findByToken(final String token) {
        return Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"))
                .flatMap(userActivityService::findByUsername);
    }

    @Override
    public void logout(final Account account) {
        userActivityService.removeActiveUser(account);
    }

}

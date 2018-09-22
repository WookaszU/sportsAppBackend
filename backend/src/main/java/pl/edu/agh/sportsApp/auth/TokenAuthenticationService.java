package pl.edu.agh.sportsApp.auth;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.model.Account;
import pl.edu.agh.sportsApp.useractivity.UserActivityData;
import pl.edu.agh.sportsApp.useractivity.UserActivityService;
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
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public Optional<String> login(Account account, final String email, final String password) {

        if (passwordEncoder.matches(password, account.getPassword())) {
            String token = tokenService.expiringToken(ImmutableMap.of("username", email));
            userActivityService.addActiveUser(email, account, token);
            return Optional.of(token);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Account> findByToken(final String token) {
        Optional<UserActivityData> userActivityDataOpt = Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"))
                .flatMap(userActivityService::findByUsername);

        if(!userActivityDataOpt.isPresent() || !userActivityDataOpt.get().getToken().equals(token))
            return Optional.empty();

        return Optional.of(userActivityDataOpt.get().getAccount());
    }

    @Override
    public Optional<Account> logout(final Account account) {
        return userActivityService.removeActiveUser(account);
    }

}

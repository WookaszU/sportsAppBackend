package pl.edu.agh.sportsApp.auth;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.dto.*;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.AuthRefusedException;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.ConfirmTokenException;
import pl.edu.agh.sportsApp.exceptionHandler.exceptions.UserAccountException;
import pl.edu.agh.sportsApp.model.Token;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.service.RegisterService;
import pl.edu.agh.sportsApp.service.TokenStorage;
import pl.edu.agh.sportsApp.service.UserService;
import pl.edu.agh.sportsApp.token.TokenService;
import pl.edu.agh.sportsApp.useractivity.UserActivityData;
import pl.edu.agh.sportsApp.useractivity.UserActivityManager;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class TokenAuthenticationService implements AuthenticationService {

    @NonNull
    TokenService tokenService;
    @NonNull
    UserActivityManager userActivityManager;
    @NonNull
    BCryptPasswordEncoder passwordEncoder;
    @NotNull
    RegisterService registerService;
    @NonNull
    TokenStorage tokenStorage;
    @NonNull
    UserService userService;

    @Override
    public void register(RegisterRequestDTO registerRequestDTO) {
        User user = User.builder()
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .firstName(registerRequestDTO.getFirstName())
                .lastName(registerRequestDTO.getLastName())
                .build();
        registerService.register(user);
    }

    @Override
    public void registrationConfirm(String registerToken) {
        Optional<Token> tokenOpt = tokenStorage.getTokenByValue(registerToken);
        if (!tokenOpt.isPresent())
            throw new ConfirmTokenException(ResponseCode.TOKEN_NOT_FOUND.name());

        if (!registerService.confirm(tokenOpt.get()))
            throw new ConfirmTokenException(ResponseCode.TOKEN_EXPIRED.name());
    }

    @Override
    public void resendRegistrationEmail(ResendEmailRequestDTO resendEmailRequestDTO) {
        Optional<Token> previousTokenOpt = tokenStorage.findByRelatedUserEmail(resendEmailRequestDTO.getEmail());

        if (!previousTokenOpt.isPresent())
            throw new ConfirmTokenException(ResponseCode.NOT_REGISTERED.name());

        registerService.resendEmail(previousTokenOpt.get());
    }

    @Override
    public UserTokenState login(LoginRequestDTO requestDTO) {

        Optional<User> userOpt = userService.getUserByEmail(requestDTO.getEmail());
        if (!userOpt.isPresent())
            throw new AuthRefusedException(ResponseCode.WRONG_LOGIN_OR_PASSWORD.name());

        User user = userOpt.get();

        if (!user.isEnabled())
            throw new UserAccountException(ResponseCode.CONFIRM_YOUR_ACCOUNT.name());

        if (passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            String token = tokenService.expiringToken(ImmutableMap.of("username", requestDTO.getEmail()));
            userActivityManager.addActiveUser(requestDTO.getEmail(), user, token);
            return new UserTokenState(token);
        } else {
            throw new AuthRefusedException(ResponseCode.WRONG_LOGIN_OR_PASSWORD.name());
        }
    }

    @Override
    public Optional<User> findByToken(final String token) {
        Optional<UserActivityData> userActivityDataOpt = Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"))
                .flatMap(userActivityManager::findByUsername);

        if (!userActivityDataOpt.isPresent() || !userActivityDataOpt.get().getToken().equals(token))
            return Optional.empty();

        return Optional.of(userActivityDataOpt.get().getUser());
    }

    @Override
    public Optional<User> logout(final User user) {
        return userActivityManager.removeActiveUser(user);
    }

}

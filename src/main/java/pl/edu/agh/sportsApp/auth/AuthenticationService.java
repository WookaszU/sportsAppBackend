package pl.edu.agh.sportsApp.auth;


import pl.edu.agh.sportsApp.dto.LoginRequestDTO;
import pl.edu.agh.sportsApp.dto.RegisterRequestDTO;
import pl.edu.agh.sportsApp.dto.ResendEmailRequestDTO;
import pl.edu.agh.sportsApp.dto.UserTokenState;
import pl.edu.agh.sportsApp.model.User;

import java.util.Optional;

public interface AuthenticationService {

    /**
     * Logs in with the given {@code username} and {@code password}.
     *
     * @param requestDTO
     * @return a {@link UserTokenState} of a user when login succeeds
     */
    UserTokenState login(LoginRequestDTO requestDTO);

    /**
     * Finds a user by its dao-key.
     *
     * @param token user dao key
     * @return user User data
     */
    Optional<User> findByToken(String token);

    /**
     * Logs out the given input {@code user}.
     *
     * @param user the user to logout
     */
    Optional<User> logout(User user);

    void register(RegisterRequestDTO registerRequestDTO);

    void registrationConfirm(String registerToken);

    void resendRegistrationEmail(ResendEmailRequestDTO resendEmailRequestDTO);
}

package pl.edu.agh.sportsApp.auth;


import pl.edu.agh.sportsApp.model.Account;

import java.util.Optional;

public interface AuthenticationService {

    /**
     * Logs in with the given {@code username} and {@code password}.
     *
     * @param account
     * @param email
     * @param password
     * @return an {@link Optional} of a user when login succeeds
     */
    Optional<String> login(Account account, String email, String password);

    /**
     * Finds a user by its dao-key.
     *
     * @param token user dao key
     * @return user Account data
     */
    Optional<Account> findByToken(String token);

    /**
     * Logs out the given input {@code user}.
     *
     * @param account the account to logout
     */
    Optional<Account> logout(Account account);
}

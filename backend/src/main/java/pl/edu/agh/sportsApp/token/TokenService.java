package pl.edu.agh.sportsApp.token;

import java.util.Map;

/**
 * Creates and validates credentials.
 */
public interface TokenService {

    /**
     * Returns new permanent token.
     *
     * @param attributes
     * @return permanentToken token
     */
    String permanentToken(Map<String, String> attributes);

    /**
     * Returns new expiring token.
     *
     * @param attributes
     * @return expiringToken token
     */
    String expiringToken(Map<String, String> attributes);

    /**
     * Checks the validity of the given credentials.
     *
     * @param token
     * @return attributes if verified
     */
    Map<String, String> verify(String token);

}

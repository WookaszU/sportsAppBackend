package pl.edu.agh.sportsApp.token;

import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.agh.sportsApp.dateservice.DateService;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.impl.TextCodec.BASE64;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class JWTTokenService implements Clock, TokenService {

    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    DateService dates;
    String issuer;
    int expirationSec;
    int allowedClockSkewSec;
    String secretKey;

    JWTTokenService(final DateService dates,
                    @Value("${jwt.issuer:sportsapp}") final String issuer,
                    @Value("${jwt.expiration-sec:86400}") final int expirationSec,
                    @Value("${jwt.clock-skew-sec:300}") final int allowedClockSkewSec,
                    @Value("${jwt.secret:secret}") final String secret) {
        super();
        this.dates = requireNonNull(dates);
        this.issuer = requireNonNull(issuer);
        this.expirationSec = requireNonNull(expirationSec);
        this.allowedClockSkewSec = requireNonNull(allowedClockSkewSec);
        this.secretKey = BASE64.encode(requireNonNull(secret));
    }

    @Override
    public String permanentToken(final Map<String, String> attributes) {
        return newToken(attributes, 0);
    }

    @Override
    public String expiringToken(final Map<String, String> attributes) {
        return newToken(attributes, expirationSec);
    }

    private String newToken(final Map<String, String> attributes, final int expiresInSec) {
        final ZonedDateTime now = dates.now();
        final Claims claims = Jwts
                .claims()
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now.toInstant()));

        if (expiresInSec > 0) {
            final ZonedDateTime expiresAt = now.plusSeconds(expiresInSec);
            claims.setExpiration(Date.from(expiresAt.toInstant()));
        }

        claims.putAll(attributes);

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(HS256, secretKey)
                .compressWith(COMPRESSION_CODEC)
                .compact();
    }

    @Override
    public Map<String, String> verify(final String token) {
        final JwtParser parser = Jwts
                .parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(allowedClockSkewSec)
                .setSigningKey(secretKey);
        return parseClaims(() -> parser.parseClaimsJws(token).getBody());
    }

    private static Map<String, String> parseClaims(final Supplier<Claims> toClaims) {
        try {
            final Claims claims = toClaims.get();
            final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            for (final Map.Entry<String, Object> e: claims.entrySet()) {
                builder.put(e.getKey(), String.valueOf(e.getValue()));
            }
            return builder.build();
        } catch (final IllegalArgumentException | JwtException e) {
            return ImmutableMap.of();
        }
    }

    @Override
    public Date now() {
        final ZonedDateTime now = dates.now();
        return Date.from(now.toInstant());
    }
}

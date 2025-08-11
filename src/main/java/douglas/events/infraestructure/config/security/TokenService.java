package douglas.events.infraestructure.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Value("${PRIVATE_KEY}")
    private String privateKey;

    @Value("${ISSUER}")
    private String issuer;

    public String generateToken(String email) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            Instant now = Instant.now();

            long expirationSeconds = 3600L;
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(email)
                    .withIssuedAt(Date.from(now))
                    .withExpiresAt(Date.from(now.plusSeconds(expirationSeconds)))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            logger.error("Error while creating JWT token: {}", e.getMessage());
            throw new RuntimeException("Error while creating JWT token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);

            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            logger.warn("No token found in request");
            return null;
        }
    }
}
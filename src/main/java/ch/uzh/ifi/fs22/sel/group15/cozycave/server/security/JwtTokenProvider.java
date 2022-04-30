package ch.uzh.ifi.fs22.sel.group15.cozycave.server.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${cozycave.security.jwt.secret}")
    private String jwtSecret;

    public String generateToken(UUID uuid) {
        Instant now = Instant.now();
        Instant expiryTime = now.plus(7, ChronoUnit.DAYS);

        return Jwts.builder()
            .setSubject(uuid.toString())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiryTime))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return generateToken(UUID.fromString(user.getUsername()));
    }

    public UUID getUuidFromToken(String token) {
        return UUID.fromString(Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody()
            .getSubject());
    }

    public boolean validateToken(String token) {
        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody()
            .getExpiration()
            .after(Date.from(Instant.now()));
    }
}

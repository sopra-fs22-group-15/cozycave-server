package ch.uzh.ifi.fs22.sel.group15.cozycave.server.security;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
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

    public String generateToken(UUID uuid, Collection<Role> roles) {
        Instant now = Instant.now();
        Instant expiryTime = now.plus(7, ChronoUnit.DAYS);

        return Jwts.builder()
            .setSubject(uuid.toString())
            .setIssuedAt(Date.from(now))
            .setNotBefore(Date.from(now))
            .setExpiration(Date.from(expiryTime))
            .claim("roles", roles)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return generateToken(
            UUID.fromString(user.getUsername()),
            user.getAuthorities().stream().map(authority -> Role.valueOf(authority.getAuthority()))
                .collect(Collectors.toList())
        );
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

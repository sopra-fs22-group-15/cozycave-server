package ch.uzh.ifi.fs22.sel.group15.cozycave.server.security;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${cozycave.security.jwt.secret}")
    private String jwtSecret;

    @Value("${cozycave.security.jwt.expiration_days}")
    private int expiration;

    public String generateToken(UUID uuid, Collection<Role> roles) {
        Instant now = Instant.now();
        Instant expiryTime = now.plus(expiration, ChronoUnit.DAYS);

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
                user.getAuthorities().stream().map(authority -> Role.valueOf(
                        authority.getAuthority().substring(5))).collect(Collectors.toList())
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

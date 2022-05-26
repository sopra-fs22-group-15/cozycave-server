package ch.uzh.ifi.fs22.sel.group15.cozycave.server.security;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class IUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public IUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {
        log.error("loadUserByUsername: " + uuid);
        User user = userRepository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new UsernameNotFoundException("invalid token: user not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getId().toString())
                .password(user.getAuthenticationData().getPassword())
                .authorities(user.getRole().generatePermittedAuthoritiesList())
                .build();
    }
}

package ch.uzh.ifi.fs22.sel.group15.cozycave.server.security;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import java.util.Collections;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public IUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {
        log.error("loadUserByUsername: " + uuid);
        User user = userRepository.findById(UUID.fromString(uuid))
            //TODO: change exception
            .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        return new org.springframework.security.core.userdetails.User(
            user.getId().toString(),
            user.getAuthenticationData().getPassword(),
            Collections.emptyList()
        );
    }
}

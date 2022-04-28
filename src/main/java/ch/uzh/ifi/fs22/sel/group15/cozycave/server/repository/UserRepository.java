package ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    @NotNull Optional<User> findById(UUID uuid);

    @NotNull Optional<User> findByAuthenticationData_Email(String email);
}

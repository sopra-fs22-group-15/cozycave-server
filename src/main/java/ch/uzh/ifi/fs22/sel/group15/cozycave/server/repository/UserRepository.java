package ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, UUID> {

    @NotNull Optional<User> findById(@NotNull UUID id);

    @NotNull Optional<User> findByAuthenticationData_Email(@NotNull String email);

    boolean existsByAuthenticationData_Email(@NotNull String email);

    @NotNull Optional<User> findByDetailsPictureId(@NotNull UUID id);
}

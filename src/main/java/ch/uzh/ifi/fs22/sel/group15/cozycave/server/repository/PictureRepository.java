package ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository(value = "pictureRepository")
public interface PictureRepository extends JpaRepository<Picture, UUID> {

    @NotNull Optional<Picture> findById(@NotNull UUID id);

}
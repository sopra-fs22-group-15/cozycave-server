package ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "pictureRepository")
public interface PictureRepository extends JpaRepository<Picture, UUID> {

    @NotNull Optional<Picture> findById(@NotNull UUID id);

    @NotNull List<Picture> findAllByUploader_Id(@NotNull UUID uploader);

    @NotNull List<Picture> findAllByUploader(@NotNull User uploader);
}
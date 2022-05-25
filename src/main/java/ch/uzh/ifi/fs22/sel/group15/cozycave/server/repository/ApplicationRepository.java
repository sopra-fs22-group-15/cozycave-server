package ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications.Application;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("applicationRepository")
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    @NotNull Optional<Application> findById(UUID uuid);

    @NotNull List<Application> findByApplicant_Id(UUID uuid);


    @NotNull List<Application> findByListing_Id(UUID uuid);

    void deleteAllByApplicant_Id(@NotNull UUID id);

    void deleteAllByListing_Id(@NotNull UUID id);
}
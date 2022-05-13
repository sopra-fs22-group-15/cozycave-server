package ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications.Application;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("applicationRepository")
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    @NotNull Optional<Application> findById(UUID uuid);

    /*@Query("SELECT a.id, a.creation_date, a.user_id, a.listing_id, a.application_status from applications a where user_id = ?1")
    @NotNull List<Application> findByApplicant(UUID uuid);*/

    @NotNull List<Application> findByApplicant_Id(UUID uuid);


    /*@Query("SELECT a.id, a.creation_date, a.user_id, a.listing_id, a.application_status from applications a where listing_id = ?1")
    @NotNull List<Application> findByListing(UUID uuid);*/
    @NotNull List<Application> findByListing_Id(UUID uuid);
}
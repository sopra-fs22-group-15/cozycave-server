package ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("listingRepository")
public interface ListingRepository extends JpaRepository<Listing, UUID> {

    @NotNull Optional<Listing> findById(@NotNull UUID id);

}

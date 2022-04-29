package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listing.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ListingRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

@Service @Transactional public class ListingService {

    private final Logger log = LoggerFactory.getLogger(ListingService.class);

    private final ListingRepository listingRepository;

    @Autowired public ListingService(@Qualifier("listingRepository")ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<Listing> getListings() {
        return this.listingRepository.findAll();
    }

    public @NotNull Listing createListing(Listing newListing) {
        checkIfDataIsValid(newListing, true);

        newListing = listingRepository.save(newListing);
        listingRepository.flush();

        log.debug("Created new Listing: {}", newListing);
        return newListing;
    }

    public @NotNull Optional<Listing> findListingID(UUID id) {
        return listingRepository.findById(id);
    }



    public @NotNull Listing updateListing(Listing listingInput) {
        Listing updatedListing = listingRepository.findById(listingInput.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing not found"));

        // TODO: Use Design Pattern
        // update details
        if (listingInput.getName() != null) {
            updatedListing.setName(listingInput.getName());
        }

        if (listingInput.getDescription() != null) {
            updatedListing.setDescription(listingInput.getDescription());
        }

        if (listingInput.getAddress() != null) {
            updatedListing.setAddress(listingInput.getAddress());
        }

        // TODO: Will cause problems
        if (listingInput.getPictures() != null) {
            updatedListing.addPictures(listingInput.getPictures());
        }

        if (listingInput.getSqm() > 0) {
            updatedListing.setName(listingInput.getName());
        }

        if (!listingInput.getPublished()) {
            updatedListing.setPublished(listingInput.getPublished());
        }

        if (listingInput.getListingtype() != null) {
            updatedListing.setListingtype(listingInput.getListingtype());
        }

        if (!listingInput.getFurnished()) {
            updatedListing.setFurnished(listingInput.getFurnished());
        }

        if (listingInput.getAvailableTo() != null) {
            updatedListing.setAvailableTo(listingInput.getAvailableTo());
        }

        if (!listingInput.getAvailable()) {
            updatedListing.setAvailable(listingInput.getAvailable());
        }

        if (listingInput.getRent() >= 0) {
            updatedListing.setRent(listingInput.getRent());
        }

        if (listingInput.getDeposit() >= 0) {
            updatedListing.setDeposit(listingInput.getDeposit());
        }

        if (listingInput.getRooms() > 0) {
            updatedListing.setRooms(listingInput.getRooms());
        }

        if (listingInput.getPublisher() != null) {
            updatedListing.setPublisher(listingInput.getPublisher());
        }

        return listingRepository.saveAndFlush(updatedListing);
    }

    public void deleteListing(Listing listing) {
        listingRepository.delete(listing);
    }


    private void checkIfDataIsValid(Listing listingToBeCreated, boolean mandatoryFieldsAreFilled) {
        if (!mandatoryFieldsAreFilled) {
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }
}

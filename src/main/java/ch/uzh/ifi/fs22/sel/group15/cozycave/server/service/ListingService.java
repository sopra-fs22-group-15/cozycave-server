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



    public @NotNull Listing updateListing(Listing userInput) {
        Listing updatedListing = listingRepository.findById(userInput.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing not found"));

        // update authentication data
        if (updatedListing.getPublisher().getAuthenticationData() != null) {
            // TODO: confirm email change by sending email with link to confirm
            if (updatedListing.getPublisher().getAuthenticationData().getEmail() != null) {
                // TODO: verify valid email
                updatedListing.getPublisher().getAuthenticationData().setEmail(userInput.getPublisher().getAuthenticationData().getEmail());
            }

            if (userInput.getPublisher().getAuthenticationData().getPassword() != null) {
                // TODO: encrypt password
                updatedListing.getPublisher().getAuthenticationData().setPassword(userInput.getPublisher().getAuthenticationData().getPassword());
            }

            if (userInput.getPublisher().getAuthenticationData().getToken() != null){
                updatedListing.getPublisher().getAuthenticationData().setToken(null);
            }
        }

        // TODO: Use Design Pattern
        // update details
        if (userInput.getName() != null) {
            updatedListing.setName(userInput.getName());
        }

        if (userInput.getDescription() != null) {
            updatedListing.setDescription(userInput.getDescription());
        }

        if (userInput.getAddress() != null) {
            updatedListing.setAddress(userInput.getAddress());
        }

        // TODO: Will cause problems
        if (userInput.getPictures() != null) {
            updatedListing.addPictures(userInput.getPictures());
        }

        if (userInput.getSqm() > 0) {
            updatedListing.setName(userInput.getName());
        }

        if (userInput.getPublished() != null) {
            updatedListing.setPublished(userInput.getPublished());
        }

        if (userInput.getListingtype() != null) {
            updatedListing.setListingtype(userInput.getListingtype());
        }

        if (userInput.getFurnished() != null) {
            updatedListing.setFurnished(userInput.getFurnished());
        }

        if (userInput.getAvailableTo() != null) {
            updatedListing.setAvailableTo(userInput.getAvailableTo());
        }

        if (userInput.getAvailable() != null) {
            updatedListing.setAvailable(userInput.getAvailable());
        }

        if (userInput.getRent() >= 0) {
            updatedListing.setRent(userInput.getRent());
        }

        if (userInput.getDeposit() >= 0) {
            updatedListing.setDeposit(userInput.getDeposit());
        }

        if (userInput.getRooms() > 0) {
            updatedListing.setRooms(userInput.getRooms());
        }

        if (userInput.getPublisher() != null) {
            updatedListing.setPublisher(userInput.getPublisher());
        }

        return listingRepository.saveAndFlush(updatedListing);
    }

    public void deleteUser(Listing listing) {
        listingRepository.delete(listing);
    }


    private void checkIfDataIsValid(Listing listingToBeCreated, boolean mandatoryFieldsAreFilled) {
        if (!mandatoryFieldsAreFilled) {
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }
}

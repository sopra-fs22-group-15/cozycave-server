package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ApplicationStatus;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications.Application;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ApplicationRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ListingRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.PictureRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;


@Service @Transactional
@ToString @EqualsAndHashCode
public class ListingService {

    private final Logger log = LoggerFactory.getLogger(ListingService.class);

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final PictureRepository pictureRepository;

    @Autowired
    public ListingService(
        @Qualifier("listingRepository") ListingRepository listingRepository,
        UserRepository userRepository, ApplicationRepository applicationRepository, PictureRepository pictureRepository) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.pictureRepository = pictureRepository;
    }

    public List<Listing> getListings() {
        return this.listingRepository.findAll();
    }

    public @NotNull Listing createListing(Listing newListing) {
        log.debug("creating listing {}", newListing);

        newListing.setId(null);
        newListing.setCreationDate(new Date());
        newListing.setAvailable(true);

        checkIfDataIsValid(newListing);

        newListing = listingRepository.saveAndFlush(newListing);

        log.info("created listing {}", newListing);
        return newListing;
    }

    public @NotNull Optional<Listing> findListingById(UUID uuid) {
        return listingRepository.findById(uuid);
    }

    public @NotNull Listing updateListing(Listing listingInput) {
        log.debug("updating listing {}", listingInput);

        Listing listing = listingRepository.findById(listingInput.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "listing not found"));

        Listing mergedListing = mergeListing(listing, listingInput);

        checkIfDataIsValid(mergedListing);

        Listing updatedListing = listingRepository.saveAndFlush(mergedListing);

        log.info("updated listing {}", updatedListing);

        return listingRepository.saveAndFlush(updatedListing);
    }

    public void deleteListing(Listing listing) {
        log.debug("deleting listing {}", listing);

        applicationRepository.deleteAllByListing_Id(listing.getId());

        pictureRepository.deleteAll(listing.getPictures());

        pictureRepository.deleteAll(listing.getFloorplan());

        listingRepository.delete(listing);

        log.info("deleted listing {}", listing);
    }

    public void deleteListing(UUID uuid) {
        log.debug("deleting listing with id {}", uuid);

        applicationRepository.deleteAllByListing_Id(uuid);

        listingRepository.deleteById(uuid);

        log.info("deleted listing with id {}", uuid);
    }

    public boolean existsListing(UUID uuid) {
        return listingRepository.existsById(uuid);
    }

    private @NotNull Listing mergeListing(@NotNull Listing listing, @NotNull Listing listingInput) {
        listing = listing.clone();

        if (!listing.getId().equals(listingInput.getId())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error when merging listings");
        }

        if (StringUtils.hasText(listingInput.getTitle())) {
            listing.setTitle(listingInput.getTitle());
        }

        if (StringUtils.hasText(listingInput.getDescription())) {
            listing.setDescription(listingInput.getDescription());
        }

        if (listingInput.getAddress() != null) {
            listing.setAddress(listingInput.getAddress());
        }

        if (listingInput.getPublished() != null) {
            listing.setPublished(listingInput.getPublished());
        }

        if (listingInput.getSqm() != null) {
            listing.setSqm(listingInput.getSqm());
        }

        if (listingInput.getListingType() != null) {
            listing.setListingType(listingInput.getListingType());
        }

        if (listingInput.getFurnished() != null) {
            listing.setFurnished(listingInput.getFurnished());
        }

        if (listingInput.getAvailable() != null) {
            listing.setAvailable(listingInput.getAvailable());
        }

        if (listingInput.getAvailable() != null) {
            listing.setAvailable(listingInput.getAvailable());
        }

        if (listingInput.getRent() != null) {
            listing.setRent(listingInput.getRent());
        }

        if (listingInput.getDeposit() != null) {
            listing.setDeposit(listingInput.getDeposit());
        }

        if (listingInput.getRooms() != null) {
            listing.setRooms(listingInput.getRooms());
        }

        if (listingInput.getPublisher() != null) {
            listing.setPublisher(listingInput.getPublisher());
        }

        return listing;
    }

    private void checkIfDataIsValid(Listing listing) {
        if (!StringUtils.hasText(listing.getTitle())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title is required");
        }

        if (listing.getPublished() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "published is required");
        }

        if (listing.getPublisher() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "publisher is required");
        }

        // check if listing will be published -> mandatory fields must be filled
        if (listing.getPublished()) {
            if (!listing.isReadyToPublish()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "mandatory fields must be filled when listing is / will be published");
            }
        }

        // check if data is valid
        if (listing.getTitle() != null) {
            if (!StringUtils.hasText(listing.getTitle())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid title");
            }

            if (listing.getTitle().length() > 255) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title too long");
            }

            listing.setTitle(listing.getTitle().trim());
        }

        if (listing.getDescription() != null) {
            if (!StringUtils.hasText(listing.getDescription())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid description");
            }

            if (listing.getDescription().length() > 65535) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description too long");
            }
        }

        if (listing.getAddress() != null) {
            if (!listing.getAddress().isValid()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid address");
            }
        }

        if (listing.getSqm() != null) {
            if (listing.getSqm() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid sqm; sqm must be greater than 0");
            }
        }

        if (listing.getAvailableTo() != null) {
            if (listing.getAvailableTo().size() == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "invalid available to; at least one gender must be selected");
            }
        }

        if (listing.getRent() != null) {
            if (listing.getRent() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid rent; rent must be greater than 0");
            }
        }

        if (listing.getDeposit() != null) {
            if (listing.getDeposit() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "invalid deposit; deposit must be greater than 0");
            }
        }

        if (listing.getRooms() != null) {
            if (listing.getRooms() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "invalid rooms; rooms must be greater than 0");
            }
        }
    }
}

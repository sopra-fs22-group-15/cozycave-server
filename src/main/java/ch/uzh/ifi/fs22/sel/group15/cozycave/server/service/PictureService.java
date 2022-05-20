package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ApplicationStatus;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications.Application;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ApplicationRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ListingRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.PictureRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service @Transactional
@Slf4j
public class PictureService {

    private final ApplicationRepository applicationRepository;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;

    @Autowired public PictureService(@Qualifier("pictureRepository") PictureRepository pictureRepository, ApplicationRepository applicationRepository, ListingRepository listingRepository, UserRepository userRepository) {
        this.pictureRepository = pictureRepository;
        this.applicationRepository = applicationRepository;
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }

    // get all Pictures to a specific listing
    public List<Picture> getListingPictures(Listing inputListing) {
        List<Picture> listingPicture = new ArrayList<>();

        for (Picture picture : inputListing.getPictures()) {
            if (picture.getId() != null) {
                listingPicture.add(this.pictureRepository.findById(picture.getId()).get());
            }
        }
        return listingPicture;
    }

    // get all floorplan Pictures to a specific listing
    public List<Picture> getListingFloorplan(Listing inputListing) {
        List<Picture> listingFloorplan = new ArrayList<>();

        for (Picture picture : inputListing.getFloorplan()) {
            if (picture.getId() != null) {
                listingFloorplan.add(this.pictureRepository.findById(picture.getId()).get());
            }
        }
        return listingFloorplan;
    }

    // get all Pictures of a user
    public List<Picture> getUserPictures(User inputUser) {
        List<Picture> userPicture = new ArrayList<>();

        for (Picture picture : inputUser.getDetails().getPicture()) {
            if (picture.getId() != null) {
                userPicture.add(this.pictureRepository.findById(picture.getId()).get());
            }
        }
        return userPicture;
    }

    // get concrete Picture > id = picture id
    public Optional<Picture> findPictureById(UUID id) {
        return this.pictureRepository.findById(id);
    }

    // get concrete Picture > id = picture id
    public Optional<Picture> findPictureById(Picture picture) {
        return this.pictureRepository.findById(picture.getId());
    }



    public @NotNull Application createApplication(Application newApplication) {
        log.debug("create Application {}", newApplication);
        checkIfDataIsValid(newApplication);

        newApplication.setId(UUID.randomUUID());
        newApplication.setCreationDate(new Date());
        // manually set application to pending regardless of how application was sent
        newApplication.setApplicationStatus(ApplicationStatus.PENDING);
        newApplication.setId(UUID.randomUUID());

        try {
            User applicant = userRepository.getOne(newApplication.getApplicant().getId());
            if (!applicant.getRole().greaterEquals(Role.STUDENT)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "only students can apply to a listing");
            }
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Listing not available for new applications");
        }

        try {
            Listing listing = listingRepository.getOne(newApplication.getListing().getId());

            if (listing.getAvailable() == false) {
                log.debug("listing with id: {} not available anymore", listing.getId());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Listing not available for new applications");
            }
            if (listing.getPublished() == false) {
                log.debug("listing with id: {} not published", listing.getId());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Listing not available for new applications");
            }
        } catch (EntityNotFoundException e) {
            log.debug("listing with id: {} not found", newApplication);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Listing not available for new applications");
        }

        newApplication = applicationRepository.saveAndFlush(newApplication);
        log.debug("Created new Application: {}", newApplication);

        return newApplication;
    }

    public boolean existsPicture(UUID uuid) {
        return pictureRepository.existsById(uuid);
    }

    public void deletePicture(Picture picture) {
        //TODO: delete all applications once a user is deleted
        log.debug("delete Picture {}", picture);
        this.pictureRepository.delete(picture);
    }

    public void deletePicture(UUID id) {
        log.debug("delete Picture {}", id);
        this.pictureRepository.deleteById(id);
    }


    private void checkIfDataIsValid(Application applicationToBeCreated) {
        // check if application has empty fiels
        if (applicationToBeCreated.getApplicant() == null
                || applicationToBeCreated.getListing() == null
                || applicationToBeCreated.getApplicationStatus() == null) {
            log.debug("values are not properly filled, applicant: {}, listing: {}, application_Status: {} ",
                    applicationToBeCreated.getApplicant(), applicationToBeCreated.getListing(), applicationToBeCreated.getApplicationStatus());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "mandatory fields must be filled before publishing");
        }
        // check if application has even sensible values
        userRepository.findById(applicationToBeCreated.getApplicant().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        listingRepository.findById(applicationToBeCreated.getListing().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "listing not found"));
    }
}
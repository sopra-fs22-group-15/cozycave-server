package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
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

@Service
@Transactional
@Slf4j
public class PictureService {

    private final ApplicationRepository applicationRepository;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;

    @Autowired
    public PictureService(@Qualifier("pictureRepository") PictureRepository pictureRepository, ApplicationRepository applicationRepository, ListingRepository listingRepository, UserRepository userRepository) {
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
    public @NotNull Optional<Picture> getUserPictures(User inputUser) {
        return this.pictureRepository.findById(inputUser.getDetails().getPicture().getId());
    }

    // get concrete Picture > id = picture id
    public Optional<Picture> findPictureById(UUID id) {
        return this.pictureRepository.findById(id);
    }

    // get concrete Picture > id = picture id
    public Optional<Picture> findPictureById(Picture picture) {
        return this.pictureRepository.findById(picture.getId());
    }


    public @NotNull Picture uploadUserPicture(Picture picture) {
        log.debug("upload Picture {}", picture);
        checkIfDataIsValid(picture);

        picture.setId(UUID.randomUUID());
        picture.setCreationDate(new Date());

        try {
            User uploader = userRepository.getOne(picture.getUploader().getId());

            picture = pictureRepository.saveAndFlush(picture);
            log.debug("Uploaded new Picture: {}", picture);

            if (uploader.getDetails().getPicture() != null) {
                deletePicture(uploader.getDetails().getPicture());
            }

            uploader.getDetails().setPicture(picture);

            uploader = userRepository.saveAndFlush(uploader);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User deos not exist");
        }

        return picture;
    }


    public @NotNull Picture uploadListingPicture(Picture picture, Listing listing) {
        log.debug("upload Picture {}", picture);
        checkIfDataIsValid(picture);

        picture.setId(UUID.randomUUID());
        picture.setCreationDate(new Date());

        try {
            User uploader = userRepository.getOne(picture.getUploader().getId());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User deos not exist");
        }
        try {
            Listing uploadListing = listingRepository.getOne(listing.getId());

            picture = pictureRepository.saveAndFlush(picture);
            log.debug("Uploaded new Picture: {}", picture);

            List<Picture> pictures = uploadListing.getPictures();
            pictures.add(picture);

            uploadListing.setPictures(pictures);

            uploadListing = listingRepository.saveAndFlush(uploadListing);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Listing deos not exist");
        }

        return picture;
    }

    public @NotNull Picture uploadListingFloorplanPicture(Picture picture, Listing listing) {
        log.debug("upload Picture {}", picture);
        checkIfDataIsValid(picture);

        picture.setId(UUID.randomUUID());
        picture.setCreationDate(new Date());

        try {
            User uploader = userRepository.getOne(picture.getUploader().getId());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User deos not exist");
        }
        try {
            Listing uploadListing = listingRepository.getOne(listing.getId());

            picture = pictureRepository.saveAndFlush(picture);
            log.debug("Uploaded new Picture: {}", picture);

            List<Picture> floorplanPictures = uploadListing.getFloorplan();
            floorplanPictures.add(picture);

            uploadListing.setFloorplan(floorplanPictures);

            uploadListing = listingRepository.saveAndFlush(uploadListing);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Listing deos not exist");
        }

        return picture;
    }

    public boolean existsPicture(UUID uuid) {
        return pictureRepository.existsById(uuid);
    }

    public void deletePicture(Picture picture) {
        log.debug("delete Picture {}", picture);
        this.pictureRepository.delete(picture);
    }

    public void deletePicture(UUID id) {
        log.debug("delete Picture {}", id);
        this.pictureRepository.deleteById(id);
    }


    private void checkIfDataIsValid(Picture pictureToBeUploaded) {
        // check if application has empty fiels
        if (pictureToBeUploaded.getUploader() == null
                || pictureToBeUploaded.getPictureUrl() == null) {
            log.debug("values are not properly filled for picture, uploader: {}, url: {}",
                    pictureToBeUploaded.getUploader(), pictureToBeUploaded.getPictureUrl());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "mandatory fields must be filled before publishing");
        }
        // check if application has even sensible values
        userRepository.findById(pictureToBeUploaded.getUploader().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }
}
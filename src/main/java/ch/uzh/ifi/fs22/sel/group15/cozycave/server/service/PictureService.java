package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.FTPUploader;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@Transactional
@Slf4j
public class PictureService {

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;

    @Autowired
    public PictureService(@Qualifier("pictureRepository") PictureRepository pictureRepository, ListingRepository listingRepository, UserRepository userRepository) {
        this.pictureRepository = pictureRepository;
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }

    public List<Picture> getPictures() {
        return this.pictureRepository.findAll();
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


    public @NotNull Picture uploadUserPicture(User user, Picture picture, MultipartFile file) {
        log.debug("upload Picture {}", picture);

        picture.setId(UUID.randomUUID());
        picture.setCreationDate(new Date());
        String filename = picture.getId().toString() + "." + file.getOriginalFilename().split("\\.")[1];
        picture.setPictureUrl(Picture.ROOT_PATH + filename);

        checkIfDataIsValid(picture);

        try {
            User uploader = userRepository.getOne(user.getId());

            picture = pictureRepository.saveAndFlush(picture);
            log.debug("Uploaded new Picture: {}", picture);

            if (uploader.getDetails().getPicture() != null) {
                deletePicture(uploader.getDetails().getPicture());
            }

            uploader.getDetails().setPicture(picture);

            uploader = userRepository.saveAndFlush(uploader);

            //TODO: Future Feature Asynchronous upload for better user experience and less latency
            FTPUploader ftpUploader = new FTPUploader("database.cozycave.ch", "cozyserver", "cozyserver!!??");
            ftpUploader.uploadFile(file, filename);
            ftpUploader.disconnect();


        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User deos not exist");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return picture;
    }

    public User setGravatarPicture(User userToUpdate) {

        try {
            userToUpdate = userRepository.getOne(userToUpdate.getId());
            Picture profilePicture = new Picture();
            profilePicture.setId(UUID.randomUUID());
            profilePicture.setCreationDate(new Date());
            profilePicture.setUploader(userToUpdate);

            //create md5 hash from user email
            byte[] email = userToUpdate.getAuthenticationData().getEmail().toLowerCase().getBytes();
            byte[] hash = null;

            if (userToUpdate.getDetails().getPicture() != null) {
                String oldPictureUrl = userToUpdate.getDetails().getPicture().getPictureUrl();
                deletePictureFromStorageServer(oldPictureUrl);
            }
            MessageDigest md = MessageDigest.getInstance("MD5");
            hash = md.digest(email);

            StringBuilder strEmailHashedBuilder = new StringBuilder();
            for (byte b : hash) {
                strEmailHashedBuilder.append(String.format("%02x", b));
            }
            String strEmailHashed = strEmailHashedBuilder.toString();

            profilePicture.setPictureUrl(
                    Picture.GRAVATAR_PATH +
                            strEmailHashed +
                            ".jpg");

            profilePicture = pictureRepository.saveAndFlush(profilePicture);
            userToUpdate.getDetails().setPicture(profilePicture);
            userToUpdate = userRepository.saveAndFlush(userToUpdate);
            return userToUpdate;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (EntityNotFoundException e) {
            log.debug("user: {} not found", userToUpdate);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Listing not available for new applications");
        }
        return userToUpdate;
    }


    public @NotNull Picture uploadListingPicture(Picture picture, MultipartFile file, Listing listing) {
        log.debug("upload Picture {}", picture);

        picture.setId(UUID.randomUUID());
        picture.setCreationDate(new Date());
        String filename = picture.getId().toString() + "." + file.getOriginalFilename().split("\\.")[1];
        picture.setPictureUrl(Picture.ROOT_PATH + filename);

        checkIfDataIsValid(picture);

        try {

            Listing uploadListing = listingRepository.getOne(listing.getId());

            picture = pictureRepository.saveAndFlush(picture);
            log.debug("Uploaded new Picture: {}", picture);

            List<Picture> pictures = uploadListing.getPictures();
            pictures.add(picture);

            uploadListing.setPictures(pictures);

            uploadListing = listingRepository.saveAndFlush(uploadListing);

            FTPUploader ftpUploader = new FTPUploader("database.cozycave.ch", "cozyserver", "cozyserver!!??");
            ftpUploader.uploadFile(file, filename);
            ftpUploader.disconnect();

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Listing deos not exist");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return picture;
    }

    public @NotNull Picture uploadListingFloorplanPicture(Picture picture, MultipartFile file, Listing listing) {
        log.debug("upload Picture {}", picture);

        picture.setId(UUID.randomUUID());
        picture.setCreationDate(new Date());
        String filename = picture.getId().toString() + "." + file.getOriginalFilename().split("\\.")[1];
        picture.setPictureUrl(Picture.ROOT_PATH + filename);

        checkIfDataIsValid(picture);

        try {

            Listing uploadListing = listingRepository.getOne(listing.getId());

            picture = pictureRepository.saveAndFlush(picture);
            log.debug("Uploaded new Picture: {}", picture);

            List<Picture> floorplanPictures = uploadListing.getFloorplan();
            floorplanPictures.add(picture);

            uploadListing.setFloorplan(floorplanPictures);

            uploadListing = listingRepository.saveAndFlush(uploadListing);

            FTPUploader ftpUploader = new FTPUploader("database.cozycave.ch", "cozyserver", "cozyserver!!??");
            ftpUploader.uploadFile(file, filename);
            ftpUploader.disconnect();

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Listing deos not exist");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return picture;
    }

    public boolean existsPicture(UUID uuid) {
        return pictureRepository.existsById(uuid);
    }

    public void deletePictureFromStorageServer(String url) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        try {
            FTPUploader ftpUploader = new FTPUploader("database.cozycave.ch", "cozyserver", "cozyserver!!??");
            ftpUploader.deleteFile(filename);
            ftpUploader.disconnect();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Connection couldn't be established to storage");
        }
    }

    public void deletePicture(Picture picture) {
        log.debug("delete Picture {}", picture);
        String url = picture.getPictureUrl();

        this.pictureRepository.delete(picture);

        deletePictureFromStorageServer(url);

    }

    public void deletePicture(UUID id) {
        log.debug("delete Picture {}", id);
        String url = findPictureById(id).get().getPictureUrl();

        this.pictureRepository.deleteById(id);

        deletePictureFromStorageServer(url);

    }

    public void deleteAll(List<Picture> picturesToBeDeleted) {

        for (Picture picture : picturesToBeDeleted) {
            this.deletePicture(picture);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        pictureRepository.deleteAll(picturesToBeDeleted);
    }


    private void checkIfDataIsValid(Picture pictureToBeUploaded) {
        // check if application has empty fiels
        if (pictureToBeUploaded.getPictureUrl() == null) {
            log.debug("values are not properly filled for picture, url: {}",
                    pictureToBeUploaded.getPictureUrl());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "mandatory fields must be filled before uploading");
        }
        // check if application has even sensible values
        userRepository.findById(pictureToBeUploaded.getUploader().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

    }
}
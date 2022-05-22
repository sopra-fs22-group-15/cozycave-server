package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PictureGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PicturePostDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.PictureMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ApplicationService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ListingService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.PictureService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v1")
@Slf4j
public class PictureController {

    private final PictureService pictureService;
    private final UserService userService;
    private final ApplicationService applicationService;
    private final ListingService listingService;

    PictureController(PictureService pictureService, UserService userService,
                      ApplicationService applicationService, ListingService listingService) {
        this.pictureService = pictureService;
        this.userService = userService;
        this.applicationService = applicationService;
        this.listingService = listingService;
    }

    // get all pictures of a listing in a list
    @GetMapping("/pictures/listings/{listingId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PictureGetDto> getAllPicturesListing(@PathVariable UUID listingId) {

        Listing inputListing = listingService.findListingById(listingId)
                .orElseThrow(() -> {
                            log.debug("listing with id {} not found while finding listing", listingId);
                            return new ResponseStatusException(HttpStatus.NOT_FOUND, "listing with id " + listingId + " not found");
                        });

        return pictureService.getListingPictures(inputListing).stream()
                .map(PictureMapper.INSTANCE::pictureToPictureGetDto)
                .collect(Collectors.toList());
    }

    // get all floorplan pictures of a listing in a list
    @GetMapping("/pictures/listings/{listingId}/floorplan")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PictureGetDto> getFloorplanListing(@PathVariable UUID listingId) {

        Listing inputListing = listingService.findListingById(listingId)
                .orElseThrow(() -> {
                    log.debug("listing with id {} not found while finding listing", listingId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "listing with id " + listingId + " not found");
                });

        return pictureService.getListingFloorplan(inputListing).stream()
                .map(PictureMapper.INSTANCE::pictureToPictureGetDto)
                .collect(Collectors.toList());
    }

    // create pictures for listing
    @PostMapping("/pictures/listings/{listingId}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PictureGetDto uploadListingPicture(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID listingId,
            @RequestParam MultipartFile file) {

        if (file.isEmpty()) {
            log.error("no Picture provided");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Picture provided");
        }

        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.error("user (authenticated user) with id {} not found while creating user", authUserId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "error finding authenticated user");
                });

        Listing listing = listingService.findListingById(listingId)
                .orElseThrow(() -> {
                    log.error("Listing with id {} not found while uploading picture", listingId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "error finding listing");
                });

        if (authUser.getId() != listing.getPublisher().getId()) {
            log.error("Listing with id {} has a different publisher {} than {}", listingId, listing.getPublisher().getId(), authUserId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Publisher of listing and logged in user are not the same");
        }

        Picture pictureInput = new Picture();
        pictureInput.setUploader(authUser);

        Picture uploadedPicture = pictureService.uploadListingPicture(pictureInput, file, listing);

        return PictureMapper.INSTANCE.pictureToPictureGetDto(uploadedPicture);
    }

    @PostMapping("/pictures/listings/{listingId}/floorplan")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PictureGetDto uploadListingFloorplanPicture(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID listingId,
            @RequestParam MultipartFile file) {

        if (file.isEmpty()) {
            log.error("no Picture provided");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Picture provided");
        }

        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.error("user (authenticated user) with id {} not found while creating user", authUserId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding authenticated user");
                });

        Listing listing = listingService.findListingById(listingId)
                .orElseThrow(() -> {
                    log.error("Listing with id {} not found while uploading picture", listingId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding listing");
                });

        if (authUser.getId() != listing.getPublisher().getId()) {
            log.error("Listing with id {} has a different publisher {} than {}", listingId, listing.getPublisher().getId(), authUserId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Publisher of listing and logged in user are not the same");
        }

        Picture pictureInput = new Picture();

        pictureInput.setUploader(authUser);

        Picture uploadedPicture = pictureService.uploadListingFloorplanPicture(pictureInput, file, listing);

        return PictureMapper.INSTANCE.pictureToPictureGetDto(uploadedPicture);
    }

    // get profile picture of a user in a list
    @GetMapping("/pictures/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PictureGetDto> getProfilePicture(@PathVariable UUID userId) {

        User inputUser = userService.findUserID(userId)
                .orElseThrow(() -> {
                    log.debug("user with id {} not found", userId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found");
                });

        //TODO: Implement GRAVATAR API -> simply hash the lowercase email to an md5 and get the pictureUrl like
        // https://www.gravatar.com/avatar/hash.jpg -> Easiest way would be when a user is created, it automatically
        // always uses the gravatar picture

        return pictureService.getUserPictures(inputUser).stream()
                .map(PictureMapper.INSTANCE::pictureToPictureGetDto)
                .collect(Collectors.toList());
    }

    // create pictures for users
    @PostMapping("/pictures/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PictureGetDto uploadUserPicture(
            @AuthenticationPrincipal String authUserId,
            @RequestParam MultipartFile file){

        if (file.isEmpty()) {
            log.error("no Picture provided");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Picture provided");
        }

        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.error("user (authenticated user) with id {} not found while creating user", authUserId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding authenticated user");
                });

        //Picture pictureInput = PictureMapper.INSTANCE.picturePostDtoToPicture(picturePostDto);
        Picture pictureInput = new Picture();
        pictureInput.setUploader(authUser);

        Picture uploadedPicture = pictureService.uploadUserPicture(pictureInput, file);

        return PictureMapper.INSTANCE.pictureToPictureGetDto(uploadedPicture);

    }

    // get specific picture
    @GetMapping("/pictures/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PictureGetDto findPicture(@PathVariable UUID id) {
        return pictureService.findPictureById(id)
                .map(PictureMapper.INSTANCE::pictureToPictureGetDto)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "picture with id " + id + " not found"));
    }

    // delete a specific picture
    @DeleteMapping("/pictures/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePicture(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id) {
        if (!pictureService.existsPicture(id)) {
            log.debug("picture with id {} not found while deleting picture", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "picture with id " + id + " not found");
        }

        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.error("user (authenticated user) with id {} not found while creating user", authUserId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding authenticated user");
                });

        if (authUser.getId() != pictureService.findPictureById(id).get().getUploader().getId()) {
            log.error("picture with id {} has a different uploader than {}", id, authUserId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Uploader of picture and logged in user are not the same");
        }

        // fallback profile picture should be gravatar
        pictureService.deletePicture(id);
        if (authUser.getDetails().getPicture() == null) {
            User user = pictureService.setGravatarPicture(authUser);
        }

        log.info("picture with id {} deleted", id);
    }

    // get specific picture url
    @GetMapping("/pictures/{id}/view")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getPictureView(@PathVariable UUID id) {
        if (!pictureService.existsPicture(id)) {
            log.debug("picture with id {} not found while deleting picture", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "picture with id " + id + " picture found");
        }
        Optional<Picture> picture = pictureService.findPictureById(id);
        String url = picture.get().getPictureUrl();
        return url;
    }
}

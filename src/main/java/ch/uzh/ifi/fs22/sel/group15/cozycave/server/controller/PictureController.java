package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PictureGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.PictureMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.UserMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ApplicationService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ListingService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.PictureService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    //TODO: Add authorization to all endpoints

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

    // get all pictures of a listing in a list
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

        //TODO: Implement GRAVATAR API

        return pictureService.getUserPictures(inputUser).stream()
                .map(PictureMapper.INSTANCE::pictureToPictureGetDto)
                .collect(Collectors.toList());
    }

    // TODO: Picture upload for listings and users
    // create pictures
    /*
    @PostMapping("/pictures")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDto uploadPicture(
            @AuthenticationPrincipal String authUserId,
            @RequestBody UserPostPutDto userPostPutDto
    ) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.error("user (authenticated user) with id {} not found while creating user", authUserId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding authenticated user");
                });

        User userInput = UserMapper.INSTANCE.userPostPutDtoToUser(userPostPutDto);

        User createdUser = userService.createUser(userInput, authUser);

        return UserMapper.INSTANCE.userToUserGetDto(createdUser);
    }*/

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
    public void deletePicture(@PathVariable UUID id) {
        if (!pictureService.existsPicture(id)) {
            log.debug("picture with id {} not found while deleting picture", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "picture with id " + id + " not found");
        }

        pictureService.deletePicture(id);

        log.info("listing with id {} deleted", id);
    }

    // get specific userprofile
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

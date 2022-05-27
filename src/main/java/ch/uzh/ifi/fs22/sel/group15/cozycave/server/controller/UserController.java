package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications.Application;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.LocationDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.applications.ApplicationGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.ApplicationMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.LocationMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.UserMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ApplicationService;
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
//TODO: add role filtering
public class UserController {

    private final UserService userService;
    private final ApplicationService applicationService;
    private final PictureService pictureService;

    UserController(UserService userService, ApplicationService applicationService, PictureService pictureService) {
        this.userService = userService;
        this.applicationService = applicationService;
        this.pictureService = pictureService;
    }

    // get all users in a list
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDto> getAllUsers() {
        return userService.getUsers().stream()
                .map(UserMapper.INSTANCE::userToUserGetDto)
                .collect(Collectors.toList());
    }

    // create user
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDto createUser(
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

        // set default picture to gravatar profile picture
        if (createdUser.getDetails().getPicture() == null) {
            //createdUser.getDetails().setPicture(setGravatarPicture(newUser));
            userInput = pictureService.setGravatarPicture(userInput);
        }

        return UserMapper.INSTANCE.userToUserGetDto(createdUser);
    }

    // get specific userprofile
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDto findUser(@PathVariable UUID id) {
        return userService.findUserID(id)
                .map(UserMapper.INSTANCE::userToUserGetDto)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id " + id + " not found"));
    }

    // update specific user
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDto updateUser(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id,
            @RequestBody UserPostPutDto userPostPutDto
    ) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.error("user (authenticated user) with id {} not found while updating user", authUserId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding authenticated user");
                });

        if (!userService.existsUser(id)) {
            log.debug("user with id {} not found while updating user", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id " + id + " not found");
        }

        User userInput = UserMapper.INSTANCE.userPostPutDtoToUser(userPostPutDto);
        userInput.setId(id);

        User updatedUser = userService.updateUser(userInput, authUser);

        log.info("application with id {} updated", updatedUser.getId());

        return UserMapper.INSTANCE.userToUserGetDto(updatedUser);
    }

    // delete a specific user
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        if (!userService.existsUser(id)) {
            log.debug("user with id {} not found while deleting user", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id " + id + " not found");
        }

        pictureService.deletePictureFromStorageServer(userService.findUserID(id).get().getDetails().getPicture().getPictureUrl());
        userService.deleteUser(id);

        log.info("listing with id {} deleted", id);
    }

    // get specific special location userprofile
    @GetMapping("/users/{id}/specialaddress")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LocationDto> findSpecialLocations(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.debug("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "error finding authenticated user");
                });

        return userService.getUsersSpecialAddress(id).stream()
                .map(LocationMapper.INSTANCE::locationToLocationDto)
                .collect(Collectors.toList());
    }

    // get specific special location userprofile
    @PostMapping("/users/{id}/specialaddress")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LocationDto addSpecialLocation(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id,
            @RequestBody LocationDto locationDto) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.debug("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "error finding authenticated user");
                });

        Location specialAddressInput = LocationMapper.INSTANCE.locationDtoToLocation(locationDto);
        Location specialAddress = userService.addSpecialLocation(id, specialAddressInput);
        return LocationMapper.INSTANCE.locationToLocationDto(specialAddress);
    }

    // get a specific special location
    @GetMapping("/users/{id}/specialaddress/{specialaddressID}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LocationDto getSpecificSpecialLocation(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id,
            @PathVariable UUID specialaddressID) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.debug("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "error finding authenticated user");
                });

        Optional<Location> specialAddress = userService.getUsersSpecialAddressById(id, specialaddressID);

        return LocationMapper.INSTANCE.locationToLocationDto(specialAddress.get());
    }

    // update a specific special location
    @PutMapping("/users/{id}/specialaddress/{specialaddressID}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LocationDto updateSpecialLocation(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id,
            @PathVariable UUID specialaddressID,
            @RequestBody LocationDto locationDto) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.debug("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "error finding authenticated user");
                });

        Location specialAddressInput = LocationMapper.INSTANCE.locationDtoToLocation(locationDto);
        specialAddressInput.setId(specialaddressID);
        Location specialAddressUpdated = userService.updateSpecialLocation(id, specialAddressInput);
        return LocationMapper.INSTANCE.locationToLocationDto(specialAddressUpdated);
    }

    // update a specific special location
    @DeleteMapping("/users/{id}/specialaddress/{specialaddressID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteSpecialLocation(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id,
            @PathVariable UUID specialaddressID) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.debug("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "error finding authenticated user");
                });

        userService.deleteSpecialLocation(id, specialaddressID);
    }


    // get all applications of a user
    @GetMapping("/users/{id}/applications")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ApplicationGetDto> findApplications(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.debug("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "error finding authenticated user");
                });
        if (!userService.existsUser(id)) {
            log.debug("user with id {} not found while updating user", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id " + id + " not found");
        }

        return applicationService.findApplicationsOfUser(id).stream()
                .map(ApplicationMapper.INSTANCE::applicationToApplicationGetDto)
                .collect(Collectors.toList());
    }

    // get specific applications of a user
    @GetMapping("/users/{id}/applications/{applicationID}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ApplicationGetDto findApplication(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id,
            @PathVariable UUID applicationID) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.debug("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "error finding authenticated user");
                });

        if (!userService.existsUser(id)) {
            log.debug("user with id {} not found while updating user", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id " + id + " not found");
        }

        return applicationService.findApplicationById(applicationID)
                .map(ApplicationMapper.INSTANCE::applicationToApplicationGetDto)
                .orElseThrow(() -> {
                    log.debug("application with id {} not found", applicationID);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "application with id " + applicationID + " not found");
                });

    }

    // delete a specific user
    @DeleteMapping("/users/{id}/applications/{applicationID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApplication(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id,
            @PathVariable UUID applicationID) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.debug("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "error finding authenticated user");
                });

        Application applicationToBeDeleted = applicationService.findApplicationById(applicationID)
                .orElseThrow(() -> {
                    log.debug("application with id {} not found", applicationID);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "error finding application");
                });

        if (!userService.existsUser(id)) {
            log.debug("user with id {} not found while updating user", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id " + id + " not found");
        }

        if ((applicationToBeDeleted.getApplicant().getId() != authUser.getId()) &&
                (!authUser.getRole().greaterEquals(Role.ADMIN))) {
            log.debug("not applicant of application with id {} nor an admin", applicationToBeDeleted.getApplicant().getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You're not allowed to delete the application");
        }

        applicationService.deleteApplication(applicationID);

        log.info("application with id {} deleted", applicationID);
    }

}

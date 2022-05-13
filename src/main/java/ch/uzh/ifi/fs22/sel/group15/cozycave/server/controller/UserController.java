package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications.Application;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.applications.ApplicationGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.applications.ApplicationPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.ApplicationMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.UserMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ApplicationService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/v1")
@Slf4j
//TODO: add role filtering
public class UserController {

    private final UserService userService;
    private final ApplicationService applicationService;

    UserController(UserService userService, ApplicationService applicationService) {
        this.userService = userService;
        this.applicationService = applicationService;
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

        userService.deleteUser(id);

        log.info("listing with id {} deleted", id);
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
                    log.error("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
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
                    log.error("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding authenticated user");
                });

        if (!userService.existsUser(id)) {
            log.debug("user with id {} not found while updating user", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id " + id + " not found");
        }

        return applicationService.findApplicationById(applicationID)
                .map(ApplicationMapper.INSTANCE::applicationToApplicationGetDto)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "application with id " + applicationID + " not found"));

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
                    log.error("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding authenticated user");
                });

        Application applicationToBeDeleted = applicationService.findApplicationById(applicationID)
                .orElseThrow(() -> {
                    log.error("application with id {} not found", applicationID);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding application");
                });

        if (!userService.existsUser(id)) {
            log.debug("user with id {} not found while updating user", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user with id " + id + " not found");
        }

        if ((applicationToBeDeleted.getApplicant().getId() != authUser.getId()) &&
                (!authUser.getRole().greaterEquals(Role.ADMIN))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You're not allowed to delete the application");
        }

        applicationService.deleteApplication(applicationID);

        log.info("application with id {} deleted", applicationID);
    }

}

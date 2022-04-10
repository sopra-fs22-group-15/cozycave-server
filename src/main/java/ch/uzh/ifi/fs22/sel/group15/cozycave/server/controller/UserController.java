package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.*;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.DTOMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users in a list
    @GetMapping("/v1/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    // create user
    @PostMapping("/v1/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserPostDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = null;
        createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserPostDTO(createdUser);
    }

    // get specific userprofile
    @GetMapping("/v1/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<UserGetDTO> findUser(@PathVariable Long id) {
        User foundUser;

        try {
            foundUser = userService.findUserID(id);
            return ResponseEntity.ok(DTOMapper.INSTANCE.convertEntityToUserGetDTO(foundUser));
        }
        catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User couldn't be found with that user ID.");
        }
    }

    // update specific user
    @PutMapping("/v1/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public UserPutDTO updateUser(@PathVariable Long id, @RequestBody UserPutDTO userPutDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        try {
            // update user
            //TODO: depending on how to authenticate this has to be changed
            User updatedUser = null;
            updatedUser = userService.updateUser(userInput);
            // convert internal representation of user back to API
            return DTOMapper.INSTANCE.convertEntityToUserPutDTO(updatedUser);
        }
        catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User couldn't be found with that user ID.");
        }
    }

    // delete a specific user
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public UserPutDTO deleteUser(@PathVariable Long id, @RequestBody UserPutDTO userPutDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        try {
            // update user
            //TODO: depending on how to authenticate this has to be changed
            User updatedUser = null;
            //updatedUser = userService.updateUser(userInput.getId(), userInput.getToken(), userInput.getUsername(), userInput.getBirthday());
            // convert internal representation of user back to API
            return DTOMapper.INSTANCE.convertEntityToUserPutDTO(updatedUser);
        }
        catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User couldn't be found with that user ID.");
        }
    }

}

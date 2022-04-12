package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserGetDTO;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserPostDTO;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserPutDTO;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.DTOMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
        } catch (EntityNotFoundException e) {
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
        } catch (EntityNotFoundException e) {
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
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User couldn't be found with that user ID.");
        }
    }

}

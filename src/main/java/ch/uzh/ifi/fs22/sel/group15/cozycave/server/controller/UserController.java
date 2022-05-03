package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.UserMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/v1")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
//TODO: add role filtering
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users in a list
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
    //TODO: update
    public UserGetDto createUser(@RequestBody UserPostPutDto userPostPutDto) {
        User userInput = UserMapper.INSTANCE.userPostPutDtoToUser(userPostPutDto);

        User createdUser = userService.createUser(userInput, userInput.getDetails().getAddress(),null);

        return UserMapper.INSTANCE.userToUserGetDto(createdUser);
    }

    // get specific userprofile
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDto findUser(@PathVariable UUID id) {
        return userService.findUserID(id)
            .map(UserMapper.INSTANCE::userToUserGetDto)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User couldn't be found with that user ID."));
    }

    // update specific user
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDto updateUser(@PathVariable UUID id, @RequestBody UserPostPutDto userPostPutDto) {
        User user = userService.findUserID(id)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User couldn't be found with that user ID."));

        User userInput = UserMapper.INSTANCE.userPostPutDtoToUser(userPostPutDto);

        // TODO: change to updating user by using token
        return UserMapper.INSTANCE.userToUserGetDto(userService.updateUser(user, userInput));
    }

    // delete a specific user
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        User user = userService.findUserID(id)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User couldn't be found with that user ID."));

        userService.deleteUser(user);
    }

}

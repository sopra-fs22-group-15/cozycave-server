package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service @Transactional public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public @NotNull User createUser(User newUser) {
        newUser.setCreationDate(new Date());

        // global checks
        checkIfUserAlreadyExists(newUser);
        checkIfDataIsValid(newUser, true);

        newUser = userRepository.saveAndFlush(newUser);

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public @NotNull Optional<User> findUserID(UUID uuid) {
        return userRepository.findById(uuid);
    }

    public @NotNull User updateUser(User userInput, User updatedBy) {
        User updatedUser = userRepository.findById(userInput.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        // update authentication data
        if (updatedUser.getAuthenticationData() != null) {
            // TODO: confirm email change by sending email with link to confirm
            if (userInput.getAuthenticationData().getEmail() != null) {
                // TODO: verify valid email
                updatedUser.getAuthenticationData().setEmail(userInput.getAuthenticationData().getEmail());
            }

            if (userInput.getAuthenticationData().getPassword() != null) {
                // TODO: encrypt password
                updatedUser.getAuthenticationData().setPassword(userInput.getAuthenticationData().getPassword());
            }

            if (userInput.getAuthenticationData().getToken() != null
                && updatedBy.getRole().greaterEquals(Role.TEAM)) {
                updatedUser.getAuthenticationData().setToken(null);
            }
        }

        // update role
        if ((updatedBy.getRole().equals(Role.TEAM)
            && userInput.getRole().lessThan(Role.TEAM))
            || updatedBy.getRole().equals(Role.ADMIN)) {
            updatedUser.setRole(userInput.getRole());
        }

        // update details
        if (userInput.getDetails() != null) {
            if (userInput.getDetails().getFirstname() != null) {
                updatedUser.getDetails().setFirstname(userInput.getDetails().getFirstname());
            }

            if (userInput.getDetails().getLastname() != null) {
                updatedUser.getDetails().setLastname(userInput.getDetails().getLastname());
            }

            if (userInput.getDetails().getGender() != null) {
                updatedUser.getDetails().setGender(userInput.getDetails().getGender());
            }

            if (userInput.getDetails().getBirthday() != null) {
                updatedUser.getDetails().setBirthday(userInput.getDetails().getBirthday());
            }

            if (userInput.getDetails().getAddress() != null) {
                // TODO: check if address has to be created first
                // TODO: verify address
                updatedUser.getDetails().setAddress(userInput.getDetails().getAddress());
            }

            if (userInput.getDetails().getBiography() != null) {
                updatedUser.getDetails().setBiography(userInput.getDetails().getBiography());
            }

            if (userInput.getDetails().getPicture() != null) {
                updatedUser.getDetails().setPicture(userInput.getDetails().getPicture());
            }
        }

        return userRepository.saveAndFlush(updatedUser);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void deleteUser(UUID uuid) {
        userRepository.delete(userRepository.findById(uuid)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")));
    }

    private void checkIfUserAlreadyExists(User userToBeCreated) {
        Optional<User> userByEmail = userRepository.findByAuthenticationData_Email(
            userToBeCreated.getAuthenticationData().getEmail());

        if (userByEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "a user with this email already exists");
        }
    }

    //TODO
    private void checkIfDataIsValid(User userToBeCreated, boolean mandatoryFieldsAreFilled) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

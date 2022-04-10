package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());

        Date dNow = new Date();
        newUser.setCreation(dNow);
        checkIfUserExists(newUser);

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User findUserID(Long inputUser) throws EntityNotFoundException {
        //find user
        return userRepository.getOne(inputUser);
    }

    public User updateUser(User userInput) {


        if (!userInput.getToken().equals(userRepository.getOne(userInput.getId()).getToken())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token does not match");
        }
        User updatedUser = userRepository.getOne(userInput.getId());

        // change firstname
        if (userInput.getFirstname() != null) {
            updatedUser.setFirstname(userInput.getFirstname());
            log.debug("Firstname updated on: {}", updatedUser);
        }

        // change name
        if (userInput.getName() != null) {
            updatedUser.setName(userInput.getName());
            log.debug("Name updated on: {}", updatedUser);
        }

        // change email
        // TODO: might cause a problem between Roles <-> depending on implementation
        if (userInput.getEmail() != null) {
            updatedUser.setEmail(userInput.getEmail());
            // TODO: Student verfication must be called to change Role
            log.debug("Email updated on: {}", updatedUser);
        }

        // change password
        if (userInput.getPassword() != null) {
            updatedUser.setPassword(userInput.getPassword());
            log.debug("Password updated on: {}", updatedUser);
        }

        // change Address
        if (userInput.getAddress() != null) {
            updatedUser.setAddress(userInput.getAddress());
            log.debug("Address updated on: {}", updatedUser);
        }
        // change gender
        if (userInput.getGender() != null) {
            updatedUser.setGender(userInput.getGender());
            log.debug("Gender updated on: {}", updatedUser);
        }

        // change birthday
        if (userInput.getBirthday() != null) {
            updatedUser.setBirthday(userInput.getBirthday());
            log.debug("Birthday updated on: {}", updatedUser);
        }

        // update profile details
        if (userInput.getDetails() != null) {
            updatedUser.setDetails(userInput.getDetails());
            log.debug("Details updated on: {}", updatedUser);
        }

        return updatedUser;
    }

    public void deleteUser() {

    }

    /**
     * This is a helper method that will check the uniqueness criteria of the
     * username and the name
     * defined in the User entity. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByEmail = userRepository.findByEmail(userToBeCreated.getEmail());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";

        //TODO: only makes sense when more is being checked later on
        if (userByEmail != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format(baseErrorMessage, "username and the name", "are"));
        }
        else if (userByEmail != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
        }
    }
}

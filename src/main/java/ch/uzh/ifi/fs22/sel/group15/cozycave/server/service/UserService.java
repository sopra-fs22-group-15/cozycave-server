package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.Utils;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.UniversityDomains;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.UserDetails;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

@Service @Transactional public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UniversityDomains universityDomains;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository,
        PasswordEncoder passwordEncoder, UniversityDomains universityDomains) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.universityDomains = universityDomains;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public @NotNull User createUser(User newUser, User createdBy) {
        newUser.setId(null);
        newUser.setCreationDate(new Date());

        AuthenticationData ad = newUser.getAuthenticationData();
        ad.setId(null);
        ad.setSalt(Utils.generateSalt());

        if (StringUtils.hasText(ad.getPassword())
            && ad.getPassword().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password is too short");
        }

        System.out.println("Hi");

        ad.setPassword(passwordEncoder.encode(ad.getPassword() + ad.getSalt()));

        if (createdBy == null || newUser.getRole() == null) {
            newUser.setRole(universityDomains
                .matchesEmail(newUser.getAuthenticationData().getEmail()) ? Role.STUDENT : Role.LANDLORD);
        } else if (createdBy.getRole().lessEquals(Role.TEAM)
            || createdBy.getRole().equals(Role.TEAM)
            && newUser.getRole().greaterEquals(Role.TEAM)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not permitted to set this role");
        }

        System.out.println("Hi2");

        UserDetails ud = newUser.getDetails();
        System.out.println("Hi2.1");
        ud.setId(null);
        System.out.println("Hi2.2");

        // global checks
        checkIfUserAlreadyExists(newUser);
        System.out.println("Hi3");
        checkIfDataIsValid(newUser, true);
        System.out.println("Hi4");

        newUser = userRepository.saveAndFlush(newUser);

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public @NotNull Optional<User> findUserID(UUID uuid) {
        return userRepository.findById(uuid);
    }

    public @NotNull Optional<User> findUserByEmail(String email) {
        Optional<User> foundUser;
        try {
            foundUser = userRepository.findByAuthenticationData_Email(email);
            return foundUser;
        }
        catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
        }
    }

    public @NotNull User updateUser(User userInput, User updatedBy) {
        User updatedUser = userRepository.findById(userInput.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        // update authentication data
        if (updatedUser.getAuthenticationData() != null) {
            // TODO: confirm email change by sending email with link to confirm
            if (userInput.getAuthenticationData().getEmail() != null) {

                if (!Utils.checkValidEmail(userInput.getAuthenticationData().getEmail())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email is not valid");
                }

                updatedUser.getAuthenticationData().setEmail(userInput.getAuthenticationData().getEmail());
            }

            if (userInput.getAuthenticationData().getPassword() != null) {
                // TODO: encrypt password, check password length
                updatedUser.getAuthenticationData().setPassword(userInput.getAuthenticationData().getPassword());
            }

            //TODO: check
//            if (userInput.getAuthenticationData().getToken() != null
//                && updatedBy.getRole().greaterEquals(Role.TEAM)) {
//                updatedUser.getAuthenticationData().setToken(null);
//            }
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
                // TODO: verify address, change role
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

    //TODO: add more checks if required
    private void checkIfDataIsValid(User userToBeCreated, boolean mandatoryFieldsAreFilled) {
        System.out.println("Hi5");
        if (userToBeCreated.getAuthenticationData() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "authentication data is missing");
        }
        AuthenticationData ad = userToBeCreated.getAuthenticationData();
        System.out.println("Hi6");

        if (userToBeCreated.getDetails() == null) {
            System.out.println("Hi7");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "details are missing");
        }
        UserDetails details = userToBeCreated.getDetails();
        System.out.println("Hi8");
        if (mandatoryFieldsAreFilled) {
            if (!Utils.checkValidEmail(ad.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email is not valid");
            }
            if (!StringUtils.hasText(ad.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password is missing");
            }
            if (!StringUtils.hasText(details.getFirstname())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "firstname is missing");
            }
            System.out.println("Hi9");
            if (!StringUtils.hasText(details.getLastname())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "lastname is missing");
            }
        }

        System.out.println("Hi11");
        if (details.getBirthday() != null && details.getBirthday().after(new Date())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "birthday is invalid");
        }
        System.out.println("Hi12");
        System.out.println("Hi13");
    }
}

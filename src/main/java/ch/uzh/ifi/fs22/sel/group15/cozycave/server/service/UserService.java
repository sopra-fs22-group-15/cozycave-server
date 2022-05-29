package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.Utils;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.config.SecurityConfig;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.UniversityDomains;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ApplicationRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.PictureRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional
@ToString
@EqualsAndHashCode
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final PictureRepository pictureRepository;
    private final PasswordEncoder passwordEncoder;
    private final UniversityDomains universityDomains;

    @Autowired
    public UserService(
            @Qualifier("userRepository") UserRepository userRepository,
            ApplicationRepository applicationRepository,
            PictureRepository pictureRepository,
            PasswordEncoder passwordEncoder,
            UniversityDomains universityDomains) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.pictureRepository = pictureRepository;
        this.passwordEncoder = passwordEncoder;
        this.universityDomains = universityDomains;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }


    public List<Location> getUsersSpecialAddress(UUID id) {
        @NotNull Optional<User> user = findUserID(id);
        return Collections.unmodifiableList(user.get().getDetails().getSpecialAddress());
    }

    public Optional<Location> getUsersSpecialAddressById(UUID userId, UUID specialaddressID) {
        @NotNull Optional<User> user = findUserID(userId);

        Optional<Location> specialAddress = null;

        for (Location address : user.get().getDetails().getSpecialAddress()) {
            if (address.getId().equals(specialaddressID)) {
                specialAddress = Optional.of(address);
            }
        }
        return specialAddress;
    }


    public @NotNull User createUser(@NotNull User newUser, @Nullable User createdBy) {
        log.debug("creating user {}", newUser);

        newUser.setId(null);
        newUser.setCreationDate(new Date());

        AuthenticationData ad = newUser.getAuthenticationData();
        ad.setId(null);
        ad.setSalt(Utils.generateSalt());

        newUser.getDetails().setId(null);

        checkIfDataIsValid(newUser, createdBy);
        ad.setPassword(passwordEncoder.encode(ad.getPassword() + ad.getSalt()));

        newUser = userRepository.saveAndFlush(newUser);

        log.info("created user {}", newUser);
        return newUser;
    }

    public @NotNull Optional<User> findUserID(UUID uuid) {
        return userRepository.findById(uuid);
    }

    public @NotNull Optional<User> findUserByEmail(String email) {
        return userRepository.findByAuthenticationData_Email(email);
    }

    public @NotNull User updateUser(@NotNull User userInput, @Nullable User updatedBy) {
        log.debug("updating user with id: {}", userInput.getId());

        User user = userRepository.findById(userInput.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        System.out.println(user.getId());
        System.out.println(userInput.getId());

        User mergedUser = mergeUser(user, userInput);

        checkIfDataIsValid(mergedUser, updatedBy);

        User updatedUser = userRepository.saveAndFlush(mergedUser);

        log.info("updated user with id: {}", updatedUser.getId());

        return updatedUser;
    }

    public Location addSpecialLocation(UUID id, Location specialAddressInput) {
        log.debug("adding special Location: {} for user with id: {}", specialAddressInput, id);

        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        userToUpdate.getDetails().getSpecialAddress().add(specialAddressInput);
        userToUpdate = userRepository.saveAndFlush(userToUpdate);

        List<Location> specialAddresses = findUserID(id).get().getDetails().getSpecialAddress();

        log.info("added special Location: {} for user with id: {}", specialAddressInput, id);

        return specialAddresses.get(specialAddresses.size() - 1);
    }

    public Location updateSpecialLocation(UUID id, Location specialAddressInput) {
        log.debug("updating special Location: {} for user with id: {}", specialAddressInput, id);
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        Location locationToBeUpdated = getUsersSpecialAddressById(id, specialAddressInput.getId()).get();
        System.out.println();

        if (specialAddressInput.getName() != null) locationToBeUpdated.setName(specialAddressInput.getName());

        if (specialAddressInput.getDescription() != null)
            locationToBeUpdated.setDescription(specialAddressInput.getName());

        if (specialAddressInput.getStreet() != null) locationToBeUpdated.setStreet(specialAddressInput.getStreet());

        if (specialAddressInput.getHouseNumber() != null)
            locationToBeUpdated.setHouseNumber(specialAddressInput.getHouseNumber());

        if (specialAddressInput.getApartmentNumber() != null)
            locationToBeUpdated.setApartmentNumber(specialAddressInput.getApartmentNumber());

        if (specialAddressInput.getApartmentNumber() != null)
            locationToBeUpdated.setApartmentNumber(specialAddressInput.getApartmentNumber());

        if (specialAddressInput.getZipCode() != null) locationToBeUpdated.setZipCode(specialAddressInput.getZipCode());

        if (specialAddressInput.getCity() != null) locationToBeUpdated.setCity(specialAddressInput.getCity());

        if (specialAddressInput.getState() != null) locationToBeUpdated.setState(specialAddressInput.getState());

        if (specialAddressInput.getCountry() != null) locationToBeUpdated.setCountry(specialAddressInput.getCountry());

        userToUpdate = userRepository.saveAndFlush(userToUpdate);
        log.info("updated special Location: {} for user with id: {}", specialAddressInput, id);
        return locationToBeUpdated;
    }

    public void deleteSpecialLocation(UUID userid, UUID specialaddressID) {
        log.debug("deleted special Location: {} for user with id: {}", specialaddressID, userid);
        User userToUpdate = userRepository.findById(userid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        userToUpdate.getDetails().getSpecialAddress().remove(getUsersSpecialAddressById(userid, specialaddressID));

        log.info("removed special Location: {} for user with id: {}", specialaddressID, userid);
        userRepository.saveAndFlush(userToUpdate);
    }


    public void deleteUser(User user) {
        log.debug("deleting user with id: {}", user.getId());

        applicationRepository.deleteAllByApplicant_Id(user.getId());

        pictureRepository.delete(user.getDetails().getPicture());

        userRepository.delete(user);

        log.info("deleted user with id: {}", user.getId());
    }

    public void deleteUser(UUID uuid) {
        log.debug("deleting user with id: {}", uuid);

        applicationRepository.deleteAllByApplicant_Id(uuid);

        pictureRepository.delete(findUserID(uuid).get().getDetails().getPicture());

        userRepository.deleteById(uuid);

        log.info("deleted user with id: {}", uuid);
    }

    public boolean existsUser(UUID uuid) {
        return userRepository.existsById(uuid);
    }

    public boolean existsUser(String email) {
        return userRepository.existsByAuthenticationData_Email(email);
    }

    private @NotNull User mergeUser(@NotNull User user, @NotNull User userInput) {
        user = user.clone();

        if (!user.getId().equals(userInput.getId())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error when merging users");
        }

        if (userInput.getAuthenticationData() != null) {
            if (StringUtils.hasText(userInput.getAuthenticationData().getEmail())) {
                user.getAuthenticationData().setEmail(userInput.getAuthenticationData().getEmail());
            }

            if (StringUtils.hasText(userInput.getAuthenticationData().getPassword())) {
                user.getAuthenticationData().setPassword(userInput.getAuthenticationData().getPassword());
            }
        }

        if (userInput.getRole() != null) {
            user.setRole(userInput.getRole());
        }

        if (userInput.getDetails() != null) {
            if (StringUtils.hasText(userInput.getDetails().getFirstName())) {
                user.getDetails().setFirstName(userInput.getDetails().getFirstName());
            }

            if (StringUtils.hasText(userInput.getDetails().getLastName())) {
                user.getDetails().setLastName(userInput.getDetails().getLastName());
            }

            if (userInput.getDetails().getGender() != null) {
                user.getDetails().setGender(userInput.getDetails().getGender());
            }

            if (userInput.getDetails().getBirthday() != null) {
                user.getDetails().setBirthday(userInput.getDetails().getBirthday());
            }

            if (userInput.getDetails().getAddress() != null) {
                if (StringUtils.hasText(userInput.getDetails().getAddress().getName())) {
                    user.getDetails().getAddress().setName(userInput.getDetails().getAddress().getName());
                }

                if (StringUtils.hasText(userInput.getDetails().getAddress().getDescription())) {
                    user.getDetails().getAddress().setDescription(userInput.getDetails().getAddress().getDescription());
                }

                if (StringUtils.hasText(userInput.getDetails().getAddress().getStreet())) {
                    user.getDetails().getAddress().setStreet(userInput.getDetails().getAddress().getStreet());
                }

                if (StringUtils.hasText(userInput.getDetails().getAddress().getHouseNumber())) {
                    user.getDetails().getAddress().setHouseNumber(userInput.getDetails().getAddress().getHouseNumber());
                }

                if (StringUtils.hasText(userInput.getDetails().getAddress().getApartmentNumber())) {
                    user.getDetails().getAddress()
                            .setApartmentNumber(userInput.getDetails().getAddress().getApartmentNumber());
                }

                if (StringUtils.hasText(userInput.getDetails().getAddress().getZipCode())) {
                    user.getDetails().getAddress().setZipCode(userInput.getDetails().getAddress().getZipCode());
                }

                if (StringUtils.hasText(userInput.getDetails().getAddress().getCity())) {
                    user.getDetails().getAddress().setCity(userInput.getDetails().getAddress().getCity());
                }

                if (StringUtils.hasText(userInput.getDetails().getAddress().getCountry())) {
                    user.getDetails().getAddress().setCountry(userInput.getDetails().getAddress().getCountry());
                }
            }

            if (StringUtils.hasText(userInput.getDetails().getBiography())) {
                user.getDetails().setBiography(userInput.getDetails().getBiography());
            }
        }

        return user;
    }

    private void checkIfDataIsValid(@NotNull User user, @Nullable User executedBy) {
        log.error("User {} is trying to update user {}", executedBy, user);

        Optional<User> userWithSameEmail = userRepository.findByAuthenticationData_Email(
                user.getAuthenticationData().getEmail());
        Optional<User> userWithSameId = user.getId() != null ? userRepository.findById(user.getId()) : Optional.empty();
        boolean userAlreadyExists = userWithSameId.isPresent();

        if (executedBy != null) {
            if (!executedBy.getId().equals(user.getId())
                    && !executedBy.getRole().isTeam()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you are not allowed to edit this user");
            }
        }

        if (!userAlreadyExists) {
            if (user.getAuthenticationData().getEmail() == null
                    || user.getAuthenticationData().getPassword() == null
                    || user.getDetails().getFirstName() == null
                    || user.getDetails().getLastName() == null
                    || user.getDetails().getGender() == null
                    || user.getDetails().getBirthday() == null
                    || user.getDetails().getAddress() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mandatory fields must be filled");
            }
        }

        // AUTHENTICATION DATA MANAGEMENT
        if (StringUtils.hasText(user.getAuthenticationData().getEmail())) {
            if (!Utils.checkValidEmail(user.getAuthenticationData().getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email is not valid");
            }

            if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "email already in use");
            }

            if (userAlreadyExists) {
                if (!user.getRole().isTeam()) {
                    user.setRole(universityDomains.matchesEmail(
                            user.getAuthenticationData().getEmail()) ? Role.STUDENT : Role.LANDLORD);
                }
            }
        }

        if (StringUtils.hasText(user.getAuthenticationData().getPassword())) {
            if (user.getAuthenticationData().getPassword().length() < SecurityConfig.PASSWORD_MIN_LENGTH) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password is too short");
            }
        }

        // ROLE MANAGEMENT
        // internal execution or same user
        if (executedBy == null
                || executedBy != null && executedBy.getId() == user.getId()) {
            // user already exists
            if (userAlreadyExists) {
                // user has new role
                if (userWithSameId.get().getRole() != user.getRole()) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you cannot set your own role");
                }
            } else {
                // user does not exist
                user.setRole(universityDomains.matchesEmail(
                        user.getAuthenticationData().getEmail()) ? Role.STUDENT : Role.LANDLORD);
            }
        } else if (executedBy != null && !executedBy.getId().equals(user.getId())) {
            // other user executing
            // executor is a user
            if (!executedBy.getRole().isTeam()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you are not allowed to edit this user");
            } else if (executedBy.getRole().isTeam()) {
                // executor is a team
                // executor is not an admin
                if (executedBy.getRole().lessThan(Role.ADMIN)) {
                    // new role is a team role
                    if (user.getRole().isTeam()) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you can not set this role");
                    }
                }
            }
        }

        // DETAILS MANAGEMENT
        if (user.getDetails().getFirstName() != null) {
            if (Utils.stripNames(user.getDetails().getFirstName()).length() == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid first name");
            }

            if (Utils.stripNames(user.getDetails().getFirstName()).length() > 255) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "first name is too long");
            }

            user.getDetails().setFirstName(user.getDetails().getFirstName().trim());
        }

        if (user.getDetails().getLastName() != null) {
            if (Utils.stripNames(user.getDetails().getLastName()).length() == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid last name");
            }

            if (Utils.stripNames(user.getDetails().getLastName()).length() > 255) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "last name is too long");
            }

            user.getDetails().setLastName(user.getDetails().getLastName().trim());
        }

        if (user.getDetails().getBirthday() != null) {
            if (user.getDetails().getBirthday().after(
                    Date.from(LocalDate.now().minusYears(16).atStartOfDay(ZoneId.systemDefault()).toInstant())
            )) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you are too young");
            }
        }

        if (user.getDetails().getAddress() != null) {
            if (!user.getDetails().getAddress().isValid()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "address is not valid");
            }
        }

        if (user.getDetails().getPhoneNumber() != null) {
            if (!Utils.isPhoneNumberValid(user.getDetails().getPhoneNumber())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid phone number");
            }

            user.getDetails().setPhoneNumber(Utils.stripPhoneNumber(user.getDetails().getPhoneNumber()));
        }

        if (StringUtils.hasText(user.getDetails().getBiography())) {
            if (user.getDetails().getBiography().length() > 65535) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "about is too long");
            }
        }
    }
}

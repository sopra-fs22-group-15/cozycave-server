package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.*;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private AuthenticationData testAuthenticationData;
    private UserDetails testUserDetails;
    private Location testLocation;

    // create test user before each setup
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Date dNow = new Date();
        UUID userId = UUID.randomUUID();
        UUID locationId = UUID.randomUUID();
        testLocation = new Location(locationId, "Test", "TestAddress", "Mainstreet", "1", 8000, "Zurich", "Switzerland");
        testAuthenticationData = new AuthenticationData(userId, "testUser@uzh.ch", "Test1234!?", "Test1234!?", "Test1234!?");
        testUserDetails = new UserDetails(userId, "Test", "User", Gender.OTHER, dNow, testLocation, "Simple Bioagraphy of the TestUser");
        testUser = new User(userId, dNow, testAuthenticationData, Role.STUDENT, testUserDetails);

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void createUserSuccess() {
        User createdUser = userService.createUser(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
        // compare emails of user
        assertEquals(testUser.getAuthenticationData().getEmail(), createdUser.getAuthenticationData().getEmail());
    }

    @Test
    public void createUserEmpty() {

    }

    @Test
    public void createUserDuplicateEmailException() {
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByAuthenticationData_Email(Mockito.any())).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(userRepository.findByAuthenticationData_Email(Mockito.any())).thenReturn(null);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void deleteUserSuccess() throws ResponseStatusException {
        User createdUser = userService.createUser(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.when(userService.findUserEmail(Mockito.any())).thenReturn(Optional.of(createdUser));

        userService.deleteUser(createdUser);

        given(userService.findUserEmail(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        assertThrows(ResponseStatusException.class, () -> userService.findUserEmail(testUser.getAuthenticationData().getEmail()));

    }

    @Test
    public void deleteUserEmpty() {

    }

    @Test
    public void deleteUserException() throws ResponseStatusException {
        /*User createdUser = userService.createUser(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.when(userService.findUserEmail(Mockito.any())).thenReturn(Optional.of(createdUser));

        given(userService.deleteUser(createdUser)).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        assertThrows(ResponseStatusException.class, () -> userService.deleteUser(testUser));*/

    }

    @Test
    public void getUsersSuccess() {

    }
    @Test
    public void getUsersEmpty() {

    }

    @Test
    public void getUsersException(){

    }

    @Test
    public void findUserIDSuccess() {

    }

    @Test
    public void findUserIDEmpty() {

    }

    @Test
    public void findUserIDNonSuccess() {

    }

    @Test
    public void findUserEmailSuccess() {

    }

    @Test
    public void findUserEmailEmpty() {

    }

    @Test
    public void findUserEmailNonSuccess() {

    }

    @Test
    public void updateUserSuccess() {

    }

    @Test
    public void updateUserException() {

    }

    @Test
    public void updateUserNonSuccess() {

    }


}
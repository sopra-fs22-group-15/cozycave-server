package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import static org.mockito.BDDMockito.doAnswer;
import static org.mockito.BDDMockito.doReturn;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.UniversityDomains;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.UserDetails;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ApplicationRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ListingRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.PictureRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationEntryPoint;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationFilter;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtTokenProvider;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

@TestPropertySource("classpath:application-test.properties")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private PictureRepository pictureRepository;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UniversityDomains universityDomains;


    @InjectMocks
    private UserService userService;

    @InjectMocks
    private ListingService listingService;

    @InjectMocks
    private ApplicationService applicationService;

    @InjectMocks
    private PictureService pictureService;
    @MockBean
    private CommandLineRunner stupidMock;


    private User insertedUser;
    private User testUser1;
    private User testUser2;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        insertedUser = new User(
            UUID.randomUUID(),
            Date.from(Instant.now()),
            new AuthenticationData(
                UUID.randomUUID(),
                "test@uzh.ch",
                new BCryptPasswordEncoder().encode("password" + "SALT"),
                "SALT"
            ),
            Role.STUDENT,
            new UserDetails(
                UUID.randomUUID(),
                "Erika",
                "Mustermann",
                Gender.FEMALE,
                Date.from(Instant.now().minus(10000, ChronoUnit.DAYS)),
                new Location(
                    null,
                    "work",
                    "uni addresss",
                    "Universitätsstrasse",
                    "78",
                    "2C",
                    "8001",
                    "Zürich",
                    "Zürich",
                    "Switzerland"
                ),
                null,
                "+411234567",
                "im erika :)",
                null
            )
        );
        doReturn(Optional.of(insertedUser))
            .when(userRepository).findById(insertedUser.getId());
        doReturn(Optional.of(insertedUser))
            .when(userRepository).findByAuthenticationData_Email(insertedUser.getAuthenticationData().getEmail());
        doReturn(true).when(userRepository).existsById(insertedUser.getId());
        doReturn(true).when(userRepository)
            .existsByAuthenticationData_Email(insertedUser.getAuthenticationData().getEmail());
        Mockito.when(userRepository.findAll()).thenReturn(List.of(insertedUser));

        testUser1 = new User(
            UUID.randomUUID(),
            Date.from(Instant.now()),
            new AuthenticationData(
                UUID.randomUUID(),
                "test2@uzh.ch",
                new BCryptPasswordEncoder().encode("password" + "SALT2"),
                "SALT2"
            ),
            Role.STUDENT,
            new UserDetails(
                UUID.randomUUID(),
                "Erika2",
                "Mustermann",
                Gender.FEMALE,
                Date.from(Instant.now().minus(10000, ChronoUnit.DAYS)),
                new Location(
                    null,
                    null,
                    null,
                    "Universitätsstrasse",
                    "78",
                    null,
                    "8001",
                    "Zürich",
                    "Zürich",
                    "Switzerland"
                ),
                null,
                "+411234567",
                "im erika :)",
                null
            )
        );

        testUser2 = new User(
            UUID.randomUUID(),
            Date.from(Instant.now()),
            new AuthenticationData(
                UUID.randomUUID(),
                "test3@uzh.ch",
                new BCryptPasswordEncoder().encode("password" + "SALT3"),
                "SALT3"
            ),
            Role.STUDENT,
            new UserDetails(
                UUID.randomUUID(),
                "Erika3",
                "Mustermann",
                Gender.FEMALE,
                Date.from(Instant.now().minus(10000, ChronoUnit.DAYS)),
                new Location(
                    null,
                    null,
                    null,
                    "Universitätsstrasse",
                    "78",
                    null,
                    "8001",
                    "Zürich",
                    "Zürich",
                    "Switzerland"
                ),
                null,
                "+411234567",
                "im erika :)",
                null
            )
        );

        doReturn(testUser2).when(userRepository).saveAndFlush(testUser2);

        doAnswer(invocation -> {
            doReturn(testUser1).when(userRepository).saveAndFlush(testUser1);
            doReturn(Optional.of(testUser1))
                .when(userRepository).findById(testUser1.getId());
            doReturn(Optional.of(testUser1))
                .when(userRepository).findByAuthenticationData_Email(testUser1.getAuthenticationData().getEmail());
            Mockito.when(userRepository.findAll()).thenReturn(List.of(insertedUser, testUser1));

            doReturn(Optional.of(testUser2))
                .when(userRepository).findById(testUser2.getId());
            doReturn(Optional.of(testUser2))
                .when(userRepository).findByAuthenticationData_Email(testUser2.getAuthenticationData().getEmail());
            Mockito.when(userRepository.findAll()).thenReturn(List.of(insertedUser, testUser1, testUser2));
            return null;
        }).when(userRepository).saveAndFlush(testUser2);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void getUsers() {
        Assertions.assertEquals(1, userRepository.findAll().size());

        Assertions.assertEquals(1, userService.getUsers().size());
        Assertions.assertEquals(insertedUser, userService.getUsers().get(0));
    }

    @Test
    void getUsersSpecialAddress() {
    }

    @Test
    void getUsersSpecialAddressById() {
    }

    @Test
    void createUser_successful() {
        Assertions.assertDoesNotThrow(() -> userService.createUser(testUser1, null));
    }

    @Test
    void createUser_unauthorized() {
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser1, insertedUser));
    }

    @Test
    void createUser_duplicate() {
        Assertions.assertDoesNotThrow(() -> userService.createUser(testUser1, null));
        Assertions.assertThrows(ResponseStatusException.class,
            () -> userService.createUser(testUser1, null));
    }

    @Test
    void createUser_wrongData() {
        testUser2.getDetails().setGender(null);
        Assertions.assertThrows(ResponseStatusException.class,
            () -> userService.createUser(testUser2, null));
    }

    @Test
    void findUserID() {
        Assertions.assertNotNull(userService.findUserID(insertedUser.getId()));
        Assertions.assertEquals(Optional.of(insertedUser), userService.findUserID(insertedUser.getId()));
        Assertions.assertTrue(userService.findUserID(UUID.randomUUID()).isEmpty());
    }

    @Test
    void findUserByEmail() {
        Assertions.assertNotNull(userService.findUserByEmail(insertedUser.getAuthenticationData().getEmail()));
        Assertions.assertEquals(Optional.of(insertedUser), userService.findUserByEmail(
            insertedUser.getAuthenticationData().getEmail()));
        Assertions.assertTrue(userService.findUserByEmail("random@email.lol").isEmpty());
    }

    @Test
    void updateUser() {
        doReturn(true).when(universityDomains).matchesEmail(Mockito.any());

        Mockito.when(userRepository.saveAndFlush(Mockito.any())).thenReturn(User.builder()
            .id(insertedUser.getId())
            .build());

        Assertions.assertDoesNotThrow(() -> userService.updateUser(
            User.builder()
                .id(insertedUser.getId())
                .authenticationData(AuthenticationData.builder()
                    .email("sada@uzh.ch")
                    .password("asdadasdsadasd")
                    .build()
                )
                .details(UserDetails.builder()
                    .firstName("Gustav")
                    .lastName("Toll")
                    .biography("hey hey hey")
                    .birthday(Date.from(Instant.now().minus(61851, ChronoUnit.DAYS)))
                    .gender(Gender.MALE)
                    .phoneNumber("+41999999")
                    .address(Location.builder()
                        .name("wor2")
                        .description("my second work")
                        .apartmentNumber("dsa")
                        .state("AG")
                        .city("Aargau")
                        .houseNumber("22")
                        .street("Jutestrasse")
                        .zipCode("5451")
                        .country("Swiss")
                        .build()
                    )
                    .build())
                .build(), insertedUser));
    }

    @Test
    void addSpecialLocation() {
    }

    @Test
    void updateSpecialLocation() {
    }

    @Test
    void deleteSpecialLocation() {
    }

    @Test
    void deleteUser() {
        userService.deleteUser(insertedUser);

        doReturn(List.of()).when(userRepository).findAll();
        Assertions.assertEquals(0, userService.getUsers().size());
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(insertedUser.getId());

        doReturn(List.of()).when(userRepository).findAll();
        Assertions.assertEquals(0, userService.getUsers().size());
    }

    @Test
    void existsUser() {
        Assertions.assertTrue(userService.existsUser(insertedUser.getId()));
    }

    @Test
    void testExistsUser() {
        Assertions.assertTrue(userService.existsUser(insertedUser.getAuthenticationData().getEmail()));
    }
}
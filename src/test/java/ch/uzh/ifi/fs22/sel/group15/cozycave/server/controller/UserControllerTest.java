package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.UniversityDomains;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.UserDetails;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationEntryPoint;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationFilter;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ApplicationService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ListingService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private ListingService listingService;
    @MockBean
    ApplicationService applicationService;

    private User permittedTestUser;
    private User unpermittedTestUser;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UniversityDomains universityDomains;

    @BeforeEach
    void setUp() {
        permittedTestUser = new User(
            UUID.randomUUID(),
            new Date(),
            new AuthenticationData(
                UUID.randomUUID(),
                "erika.mustermann@uzh.ch",
                "password",
                "rand0m"
            ),
            Role.TEAM,
            new UserDetails(
                UUID.randomUUID(),
                "Erika",
                "Mustermann",
                Gender.FEMALE,
                Date.from(Instant.now().minus(50 * 365, ChronoUnit.DAYS)),
                null,
                "+41 79 123 45 67",
                "bio"
            )
        );

        unpermittedTestUser = new User(
            UUID.randomUUID(),
            new Date(),
            new AuthenticationData(
                UUID.randomUUID(),
                "max.mustermann@uzh.ch",
                "password",
                "rand0m"
            ),
            Role.STUDENT,
            new UserDetails(
                UUID.randomUUID(),
                "Max",
                "Mustermann",
                Gender.MALE,
                Date.from(Instant.now().minus(45 * 365, ChronoUnit.DAYS)),
                null,
                "+41 76 123 45 67",
                "bio"
            )
        );

        Mockito.when(userService.getUsers()).thenReturn(List.of(permittedTestUser, unpermittedTestUser));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllUsers() throws Exception {
//        mockMvc.perform(
//            MockMvcRequestBuilders.get("/v1/users")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createUser() {
    }

    @Test
    void findUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void findApplications() {

    }

    @Test
    void findApplication() {

    }

    @Test
    void deleteApplication() {

    }
}
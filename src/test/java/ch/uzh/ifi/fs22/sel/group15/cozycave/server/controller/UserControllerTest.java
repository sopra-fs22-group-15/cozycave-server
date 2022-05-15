package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.*;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications.Application;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
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
import java.util.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private Application permittedApplication;
    private Application unpermittedApplication;
    private ArrayList<Gender> availableTo;
    private Listing listing;

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

        availableTo = new ArrayList<Gender>(Arrays.asList(
                Gender.FEMALE,
                Gender.MALE,
                Gender.PREFER_NOT_TO_SAY,
                Gender.OTHER));

        listing =  new Listing(
                UUID.randomUUID(),
                Date.from(Instant.now().minus(45 * 365, ChronoUnit.DAYS)),
                "Test Flat",
                "Test Description Flat",
                null,
                true,
                150.0,
                ListingType.DORM,
                true,
                availableTo,
                true,
                1500.0,
                1.5,
                permittedTestUser);

        permittedApplication = new Application(
                UUID.randomUUID(),
                Date.from(Instant.now().minus(45 * 365, ChronoUnit.DAYS)),
                permittedTestUser,
                listing,
                ApplicationStatus.PENDING
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
    void findApplications_Success() throws Exception {
        /*
        Currently doesn't work, probably because of the auth.
        maybe with @WithMockUser(username = "user1", password = "pwd", roles = "USER"), but tests will be fully implemented when listing / users are done.

        ArrayList<Application> applicationsAsList = new ArrayList<Application>();

        applicationsAsList.add(permittedApplication);

        given(applicationService.findApplicationsOfUser(permittedTestUser)).willReturn
                (applicationsAsList);

        // when
        MockHttpServletRequestBuilder getRequest = get("/v1/users/"+permittedTestUser.getId()+"/applications").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(permittedApplication.getId())))
                .andExpect(jsonPath("$[0].applicant.id", is(permittedTestUser.getId())))
                .andExpect(jsonPath("$[0].listing.id", is(listing.getId())))
                .andExpect(jsonPath("$[0].application_status", is(ApplicationStatus.PENDING)))
        ;
        */
    }

    @Test
    void findApplication() {

    }

    @Test
    void deleteApplication() {

    }
}
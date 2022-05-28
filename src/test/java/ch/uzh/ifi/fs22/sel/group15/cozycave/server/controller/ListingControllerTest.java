package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.UniversityDomains;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.UserDetails;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationEntryPoint;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationFilter;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ApplicationService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ListingService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.PictureService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@WebMvcTest(UserController.class)
class ListingControllerTest {

    @MockBean
    ApplicationService applicationService;
    @MockBean
    PictureService pictureService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private ListingService listingService;
    private User permittedTestUser;
    private User unpermittedTestUser;
    private Listing validListing;
    private Listing unpermittedListing;

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
                Role.LANDLORD,
                new UserDetails(
                        UUID.randomUUID(),
                        "Erika",
                        "Mustermann",
                        Gender.FEMALE,
                        Date.from(Instant.now().minus(50 * 365, ChronoUnit.DAYS)),
                        new Location(
                                null,
                                null,
                                null,
                                "Bahnhofstrasse",
                                "1",
                                "10a",
                                "8000",
                                "Zürich",
                                "Zürich",
                                "Switzerland"),
                        null,
                        "+41 79 123 45 67",
                        "bio",
                        null
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
                        new Location(
                                null,
                                "Max Mustermann Address",
                                "Max Mustermann Address Description",
                                "Bahnhofstrasse",
                                "1",
                                "10a",
                                "8000",
                                "Zürich",
                                "Zürich",
                                "Switzerland"),
                        null,
                        "+41 76 123 45 67",
                        "bio",
                        null
                )
        );

        List<Gender> availableTo = new ArrayList<Gender>(Arrays.asList(
                Gender.FEMALE,
                Gender.MALE,
                Gender.PREFER_NOT_TO_SAY,
                Gender.OTHER));

        validListing = new Listing(
                UUID.randomUUID(),
                new Date(),
                "Test Flat",
                "Test Description Flat",
                new Location(
                        UUID.randomUUID(),
                        "Test Flat Location",
                        "Test Flat Location Description",
                        "Bahnhofstrasse",
                        "1a",
                        "10",
                        "8005",
                        "Zürich",
                        "Zürich",
                        "Switzerland"
                ),
                true,
                150.0,
                ListingType.DORM,
                true,
                new ArrayList<Gender>(Arrays.asList(
                        Gender.FEMALE,
                        Gender.MALE,
                        Gender.PREFER_NOT_TO_SAY,
                        Gender.OTHER)
                ),
                true,
                700.0,
                1500.0,
                1.5,
                permittedTestUser,
                null,
                null);


        Mockito.when(listingService.getListings()).thenReturn(List.of(validListing));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllListingsFiltered() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void getAllListingsFilteredEmpty() {
//        mockMvc.perform(
//            MockMvcRequestBuilders.get("/v1/users")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//        ).andExpect(MockMvcResultMatchers.status().isOk());
    }



}
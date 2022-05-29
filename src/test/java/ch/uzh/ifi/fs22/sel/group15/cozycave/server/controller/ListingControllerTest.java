package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.UniversityDomains;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ListingController.class)
public class ListingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PictureService pictureService;

    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationController authenticationController;

    @MockBean
    private ListingService listingService;

    private User permittedTestUser;
    private User unpermittedTestUser;
    private Listing validListing;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UniversityDomains universityDomains;

    @BeforeEach
    public void setUp() {

        permittedTestUser = new User(
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
                                UUID.randomUUID(),
                                "Max Mustermann Address",
                                "Max Mustermann Address Description",
                                "Bahnhofstrasse",
                                "1",
                                "10a",
                                "8000",
                                "Zürich",
                                "Zürich",
                                "Switzerland"
                        ),
                        null,
                        "+41 76 123 45 67",
                        "bio",
                        null
                )
        );

        List<Gender> availableTo = new ArrayList<>();
        availableTo.add(Gender.MALE);

        List<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture(UUID.randomUUID(),
                        new Date(),
                        permittedTestUser,
                        "https://google.ch/"));

        List<Picture> floorplan = new ArrayList<>();
        floorplan.add(new Picture(UUID.randomUUID(),
                        new Date(),
                        permittedTestUser,
                        "https://google.ch/"));

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
                availableTo,
                true,
                700.0,
                1500.0,
                1.5,
                permittedTestUser,
                pictures,
                floorplan);
    }

    /*@AfterEach
    void tearDown() {
    }*/

    @Test
    public void getAllListingsFiltered() throws Exception {

        given(listingService.getListings()).willReturn(List.of(validListing));

        MockHttpServletRequestBuilder getRequest = get("/v1/listings/").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(validListing.getTitle())))
                .andExpect(jsonPath("$[0].description", is(validListing.getDescription())))
                .andExpect(jsonPath("$[0].address", is(validListing.getAddress())))
                .andExpect(jsonPath("$[0].published", is(validListing.getPublished())))
                .andExpect(jsonPath("$[0].available", is(validListing.getAvailable())))
                .andExpect(jsonPath("$[0].rent", is(validListing.getRent())))
                .andExpect(jsonPath("$[0].deposit", is(validListing.getDeposit())))
                .andExpect(jsonPath("$[0].rooms", is(validListing.getRooms())))
                .andExpect(jsonPath("$[0].publisher", is(validListing.getPublisher())))
                .andExpect(jsonPath("$[0].pictures", is(validListing.getPictures())))
                .andExpect(jsonPath("$[0].floorplan", is(validListing.getFloorplan())))
        ;
    }

    @Test
    public void getAllListingsFilteredEmpty() {

    }

    @Test
    public void getSpecificListing() throws Exception{

        given(listingService.findListingById(Mockito.any())).willReturn(Optional.ofNullable(validListing));

        UUID listingId = validListing.getId();
        MockHttpServletRequestBuilder getRequest = get("/v1/listings/{listingId}").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.title", is(validListing.getTitle())))
                .andExpect(jsonPath("$.description", is(validListing.getDescription())))
                .andExpect(jsonPath("$.address", is(validListing.getAddress())))
                .andExpect(jsonPath("$.published", is(validListing.getPublished())))
                .andExpect(jsonPath("$.available", is(validListing.getAvailable())))
                .andExpect(jsonPath("$.rent", is(validListing.getRent())))
                .andExpect(jsonPath("$.deposit", is(validListing.getDeposit())))
                .andExpect(jsonPath("$.rooms", is(validListing.getRooms())))
                .andExpect(jsonPath("$.publisher", is(validListing.getPublisher())))
                .andExpect(jsonPath("$.pictures", is(validListing.getPictures())))
                .andExpect(jsonPath("$.floorplan", is(validListing.getFloorplan())))
        ;
    }



}
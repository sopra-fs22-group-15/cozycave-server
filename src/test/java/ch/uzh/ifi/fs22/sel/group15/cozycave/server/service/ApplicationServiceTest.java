package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ApplicationStatus;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications.Application;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.UserDetails;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ApplicationRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.ListingRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.PictureRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApplicationServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private PictureRepository pictureRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private ListingService listingService;

    @InjectMocks
    private ApplicationService applicationService;

    @InjectMocks
    private PictureService pictureService;

    private User testUserStudent;
    private User testUserLandlord;
    private Application testapplication;
    private Listing testlisting;
    private AuthenticationData testAuthenticationData;
    private UserDetails testUserDetails;
    private Location testLocation;

    // create test user before each setup
    @BeforeEach
    public void setup() {
        testUserStudent = new User(
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
                        "+41 76 123 45 67",
                        "bio",
                        null
                )
        );

        testUserLandlord = new User(
                UUID.randomUUID(),
                new Date(),
                new AuthenticationData(
                        UUID.randomUUID(),
                        "max.mustermann@uzh.ch",
                        "password",
                        "rand0m"
                ),
                Role.LANDLORD,
                new UserDetails(
                        UUID.randomUUID(),
                        "Max",
                        "Mustermann",
                        Gender.MALE,
                        Date.from(Instant.now().minus(45 * 365, ChronoUnit.DAYS)),
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
                        testUserLandlord,
                        "https://google.ch/"));

        List<Picture> floorplan = new ArrayList<>();
        floorplan.add(new Picture(UUID.randomUUID(),
                        new Date(),
                        testUserLandlord,
                        "https://google.ch/"));

        testlisting = new Listing(
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
                testUserLandlord,
                pictures,
                floorplan);

        testapplication = new Application(
                UUID.randomUUID(),
                new Date(),
                testUserStudent,
                testlisting,
                ApplicationStatus.PENDING
        );


    }

    @Test
    public void findApplicationsOfUser_success() {
        Mockito.when(applicationRepository.findByApplicant_Id(testUserStudent.getId())).then(List.of(testapplication));


    }

    @Test
    public void findApplicationsOfUser_none() {

    }

    @Test
    public void findApplicationsToListing_success() {

    }

    @Test
    public void findApplicationsToListing_none() {

    }

    @Test
    public void findApplicationById_success() {

    }

    @Test
    public void findApplicationById_none() {

    }

    @Test
    public void createApplication_success() {

    }

    @Test
    public void createApplication_throws() {

    }

    @Test
    public void createApplication_none() {

    }

    @Test
    public void updateApplication_success() {

    }

    @Test
    public void updateApplication_none() {

    }

    @Test
    public void updateApplication_throw() {

    }

    @Test
    public void deleteApplication_success() {

    }

    @Test
    public void deleteApplication_none() {

    }

    @Test
    public void decideOnApplication_success() {

    }

    @Test
    public void decideOnApplication_throw() {

    }



}
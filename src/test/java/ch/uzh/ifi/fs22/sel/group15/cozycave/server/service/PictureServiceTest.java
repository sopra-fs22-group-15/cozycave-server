package ch.uzh.ifi.fs22.sel.group15.cozycave.server.service;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PictureServiceTest {


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


    private Listing testListing;
    private User testUser;
    private Picture testPicture;
    private List<Picture> testPicturesListing;
    private List<Picture> testPicturesListingFloorplan;

    // create test listing before each setup
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);


        testUser = new User(
                UUID.randomUUID(),
                new Date(),
                new AuthenticationData(
                        UUID.randomUUID(),
                        "matthias.imhof@uzh.ch",
                        "password",
                        "rand0m"
                ),
                Role.STUDENT,
                new UserDetails(
                        UUID.randomUUID(),
                        "Matthias",
                        "Imhof",
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
        testPicture = new Picture(
                UUID.randomUUID(),
                new Date(),
                testUser,
                "http://database.cozycave.ch/CozyCave/fdc8f8fd-df5d-449c-9da0-49ccbca0757a.jpg"
        );
        testUser.getDetails().setPicture(testPicture);

        testPicturesListing = new ArrayList<>();
        testPicturesListing.add(testPicture);

        testPicturesListingFloorplan = new ArrayList<>();
        testPicturesListingFloorplan.add(testPicture);

        testListing = new Listing(
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
                testUser,
                testPicturesListing,
                testPicturesListingFloorplan);
    }

    @Test
    public void getAllPictures_success() {
        Mockito.when(pictureService.getPictures()).thenReturn(List.of(testPicture,testPicture,testPicture));

        List<Picture> allPictures = pictureService.getPictures();
        assertEquals(allPictures.size(), 3);
    }

    @Test
    public void getAllPictures_empty() {
        Mockito.when(pictureService.getPictures()).thenReturn(List.of());

        List<Picture> allPictures = pictureService.getPictures();
        assertEquals(allPictures.size(), 0);
    }

    @Test
    public void getListingPictures_success() {
        Mockito.when(pictureRepository.findById(Mockito.any())).thenReturn(Optional.of(testPicture));

        assertEquals(pictureService.getListingPictures(testListing), testPicturesListing);

    }

    @Test
    public void getListingFloorplan_success() {
        Mockito.when(pictureRepository.findById(Mockito.any())).thenReturn(Optional.of(testPicture));

        assertEquals(pictureService.getListingFloorplan(testListing), testPicturesListing);

    }


    @Test
    public void getUserPictures_success() {
        Mockito.when(pictureService.getUserPictures(testUser)).thenReturn(Optional.of(testPicture));
        Mockito.when(pictureRepository.findById(testUser.getDetails().getId())).thenReturn(Optional.of(testPicture));

        Optional<Picture> userPicture = pictureService.getUserPictures(testUser);

        assertEquals(userPicture, Optional.of(testPicture));
    }

    @Test
    public void findPictureById_I_success() {
        Mockito.when(pictureRepository.findById(testPicture.getId())).thenReturn(Optional.of(testPicture));

        Optional<Picture> foundPicture = pictureService.findPictureById(testPicture.getId());

        assertEquals(Optional.of(testPicture), foundPicture);

    }

    @Test
    public void findPictureById_II_success() {
        Mockito.when(pictureRepository.findById(testPicture.getId())).thenReturn(Optional.of(testPicture));

        Optional<Picture> foundPicture = pictureService.findPictureById(testPicture);

        assertEquals(Optional.of(testPicture), foundPicture);
    }

    @Test
    public void setGravatarPicture_success() {
        Mockito.when(userRepository.getOne(testUser.getId())).thenReturn(testUser);

        Picture gravatarPicture = new Picture(
                UUID.randomUUID(),
                new Date(),
                testUser,
                "https://www.gravatar.com/avatar/52cf7cd6ab512f90c817ef1de24118f6.jpg"
        );
        User mockedUser = new User(
                UUID.randomUUID(),
                new Date(),
                new AuthenticationData(
                        UUID.randomUUID(),
                        "matthias.imhof@uzh.ch",
                        "password",
                        "rand0m"
                ),
                Role.STUDENT,
                new UserDetails(
                        UUID.randomUUID(),
                        "Matthias",
                        "Imhof",
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
                        gravatarPicture
                )
        );

        Mockito.when(pictureService.setGravatarPicture(testUser)).thenReturn(mockedUser);

        User updatedUser = pictureService.setGravatarPicture(testUser);

        assertEquals(updatedUser.getDetails().getPicture().getPictureUrl(), gravatarPicture.getPictureUrl());

    }

    @Test
    public void deletePicture_success() {
        Mockito.when(pictureService.findPictureById(testPicture.getId())).thenReturn(Optional.of(testPicture));

        assertEquals(Optional.of(testPicture), pictureService.findPictureById(testPicture.getId()));

        pictureService.deletePicture(testPicture.getId());

        Mockito.when(pictureService.findPictureById(testPicture.getId())).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), pictureService.findPictureById(testPicture.getId()));

        pictureService.deletePicture(testPicture);

        assertEquals(Optional.empty(), pictureService.findPictureById(testPicture.getId()));

    }

    @Test
    public void deleteAll_success() {
        Mockito.when(pictureRepository.findById(Mockito.any())).thenReturn(Optional.of(testPicture));

        assertEquals(pictureService.getListingPictures(testListing), testPicturesListing);

        pictureService.deleteAll(testPicturesListing);

        Mockito.when(pictureService.findPictureById(testPicturesListing.get(0).getId())).thenReturn(Optional.empty());

        for (Picture picture : testPicturesListing) {
            assertEquals(Optional.empty(), pictureService.findPictureById(picture.getId()));
        }

    }

    @Test
    public void existsPicture() {
        Mockito.when(pictureRepository.existsById(testPicture.getId())).thenReturn(true);

        assertTrue(pictureService.existsPicture(testPicture.getId()));
    }

    @Test
    public void existsPicture_empty() {
        Mockito.when(pictureRepository.existsById(testPicture.getId())).thenReturn(false);

        assertFalse(pictureService.existsPicture(testPicture.getId()));
    }

    @Test
    public void clonePictureTest() {
        Picture picture = testPicture.clone();
        assertEquals(picture.getPictureUrl(), testPicture.getPictureUrl());
        assertEquals(picture.getId(), testPicture.getId());
        assertEquals(picture.getUploader(), testPicture.getUploader());
        assertEquals(picture.getCreationDate(), testPicture.getCreationDate());
    }


}
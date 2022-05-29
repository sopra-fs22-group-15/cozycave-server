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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ListingServiceTest {


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

    // create test listing before each setup
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testUser = new User(
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

        List<Gender> availableTo = new ArrayList<>();
        availableTo.add(Gender.MALE);

        List<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture(UUID.randomUUID(),
                        new Date(),
                        testUser,
                        "https://google.ch/"));

        List<Picture> floorplan = new ArrayList<>();
        floorplan.add(new Picture(UUID.randomUUID(),
                        new Date(),
                        testUser,
                        "https://google.ch/"));

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
                pictures,
                floorplan);

        //Mockito.when(listingRepository.save(Mockito.any())).thenReturn(testListing);
    }

    @Test
    public void createListing_validInputs() {

        Mockito.when(listingService.createListing(testListing)).thenReturn(testListing);

        Listing createdListing = listingService.createListing(testListing);

        assertEquals(testListing.getId(), createdListing.getId());
        assertEquals(testListing.getCreationDate(), createdListing.getCreationDate());
        assertEquals(testListing.getTitle(), createdListing.getTitle());
        assertEquals(testListing.getDescription(), createdListing.getDescription());
        assertEquals(testListing.getAddress(), createdListing.getAddress());
        assertEquals(testListing.getPublished(), createdListing.getPublished());
        assertEquals(testListing.getSqm(), createdListing.getSqm());
        assertEquals(testListing.getListingType(), createdListing.getListingType());
        assertEquals(testListing.getFurnished(), createdListing.getFurnished());
        assertEquals(testListing.getPublisher(), createdListing.getPublisher());
        assertEquals(testListing.getPictures(), createdListing.getPictures());
        assertEquals(testListing.getFloorplan(), createdListing.getFloorplan());
    }

    @Test
    public void createListing_invalidInputs() {
        assertThrows(ResponseStatusException.class, () -> listingService.createListing(new Listing()));
    }

    @Test
    public void getListings_list() {

        Mockito.when(listingService.getListings()).thenReturn(List.of(testListing, testListing));

        assertEquals(listingService.getListings().get(0).getId(), testListing.getId());
        assertEquals(listingService.getListings().size(), 2);
    }

    @Test
    public void getListings_empty() {

        Mockito.when(listingService.getListings()).thenReturn(List.of());

        assertEquals(listingService.getListings().size(), 0);

    }

    @Test
    public void updateListing_validInputs() {
        Mockito.when(listingRepository.findById(testListing.getId())).thenReturn(Optional.of(testListing));
        Mockito.when(listingService.updateListing(testListing)).thenReturn(testListing);

        Listing updatedListing = listingService.updateListing(testListing);

        assertEquals(testListing.getId(), updatedListing.getId());
        assertEquals(testListing.getCreationDate(), updatedListing.getCreationDate());
        assertEquals(testListing.getTitle(), updatedListing.getTitle());
        assertEquals(testListing.getDescription(), updatedListing.getDescription());
        assertEquals(testListing.getAddress(), updatedListing.getAddress());
        assertEquals(testListing.getPublished(), updatedListing.getPublished());
        assertEquals(testListing.getSqm(), updatedListing.getSqm());
        assertEquals(testListing.getListingType(), updatedListing.getListingType());
        assertEquals(testListing.getFurnished(), updatedListing.getFurnished());
        assertEquals(testListing.getPublisher(), updatedListing.getPublisher());
        assertEquals(testListing.getPictures(), updatedListing.getPictures());
        assertEquals(testListing.getFloorplan(), updatedListing.getFloorplan());

    }

    @Test
    public void updateListing_invalidInputs_Exceptions() {
        assertThrows(ResponseStatusException.class, () -> listingService.updateListing(new Listing()));
    }

    @Test
    public void updateListing_not_found() {
        Mockito.when(listingRepository.findById(testListing.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> listingService.updateListing(testListing));
    }

    @Test
    public void updateListing_invalidInputs() {
        Mockito.when(listingRepository.findById(testListing.getId())).thenReturn(Optional.of(testListing));
        testListing.setId(UUID.randomUUID());

        assertThrows(ResponseStatusException.class, () -> listingService.updateListing(testListing));
    }


    @Test
    public void deleteListing_validInputs() {
        Mockito.when(listingRepository.findById(testListing.getId())).thenReturn(Optional.of(testListing));

        applicationRepository.deleteAllByListing_Id(testListing.getId());

        assertEquals(listingService.findListingById(testListing.getId()), Optional.of(testListing));

        listingService.deleteListing(testListing);

        Mockito.when(listingRepository.findById(testListing.getId())).thenReturn(Optional.empty());

        assertFalse(listingService.findListingById(testListing.getId()).isPresent());
    }

    @Test
    public void deleteListing_Id_validInputs() {
        Mockito.when(listingRepository.findById(testListing.getId())).thenReturn(Optional.of(testListing));

        applicationRepository.deleteAllByListing_Id(testListing.getId());

        assertEquals(listingService.findListingById(testListing.getId()), Optional.of(testListing));

        listingService.deleteListing(testListing.getId());

        Mockito.when(listingRepository.findById(testListing.getId())).thenReturn(Optional.empty());

        assertFalse(listingService.findListingById(testListing.getId()).isPresent());
    }

    @Test
    public void existsListing_success() {
        Mockito.when(listingRepository.existsById(testListing.getId())).thenReturn(true);
        assertTrue(listingService.existsListing(testListing.getId()));
    }

    @Test
    public void existsListing_not_found() {
        Mockito.when(listingRepository.existsById(testListing.getId())).thenReturn(false);
        assertFalse(listingService.existsListing(testListing.getId()));
    }

}
package ch.uzh.ifi.fs22.sel.group15.cozycave.server.old.service;

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
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
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

        MockitoAnnotations.openMocks(this);

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
        Mockito.when(applicationRepository.findByApplicant_Id(testUserStudent.getId())).thenReturn(List.of(testapplication));

        List<Application> foundApplicationsOfUserId = applicationService.findApplicationsOfUser(testUserStudent.getId());

        assertEquals(foundApplicationsOfUserId.get(0).getId(),testapplication.getId());
        assertEquals(foundApplicationsOfUserId.get(0).getCreationDate(),testapplication.getCreationDate());
        assertEquals(foundApplicationsOfUserId.get(0).getApplicant(),testapplication.getApplicant());
        assertEquals(foundApplicationsOfUserId.get(0).getApplicant(),testapplication.getApplicant());
        assertEquals(foundApplicationsOfUserId.get(0).getListing(),testapplication.getListing());
        assertEquals(foundApplicationsOfUserId.get(0).getApplicationStatus(),testapplication.getApplicationStatus());

        List<Application> foundApplicationsOfUser = applicationService.findApplicationsOfUser(testUserStudent);

        assertEquals(foundApplicationsOfUser.get(0).getId(),testapplication.getId());
        assertEquals(foundApplicationsOfUser.get(0).getCreationDate(),testapplication.getCreationDate());
        assertEquals(foundApplicationsOfUser.get(0).getApplicant(),testapplication.getApplicant());
        assertEquals(foundApplicationsOfUser.get(0).getApplicant(),testapplication.getApplicant());
        assertEquals(foundApplicationsOfUser.get(0).getListing(),testapplication.getListing());
        assertEquals(foundApplicationsOfUser.get(0).getApplicationStatus(),testapplication.getApplicationStatus());
    }

    @Test
    public void findApplicationsOfUser_none() {
        Mockito.when(applicationRepository.findByApplicant_Id(testUserStudent.getId())).thenReturn(List.of());

        List<Application> foundApplicationsOfUser = applicationService.findApplicationsOfUser(testUserStudent);

        assertEquals(foundApplicationsOfUser.size(), 0);

    }

    @Test
    public void findApplicationsToListing_success() {
        Mockito.when(applicationRepository.findByListing_Id(testlisting.getId())).thenReturn(List.of(testapplication));

        List<Application> foundApplicationsToListingId = applicationService.findApplicationsToListing(testlisting.getId());

        assertEquals(foundApplicationsToListingId.get(0).getId(),testapplication.getId());
        assertEquals(foundApplicationsToListingId.get(0).getCreationDate(),testapplication.getCreationDate());
        assertEquals(foundApplicationsToListingId.get(0).getApplicant(),testapplication.getApplicant());
        assertEquals(foundApplicationsToListingId.get(0).getApplicant(),testapplication.getApplicant());
        assertEquals(foundApplicationsToListingId.get(0).getListing(),testapplication.getListing());
        assertEquals(foundApplicationsToListingId.get(0).getApplicationStatus(),testapplication.getApplicationStatus());

        List<Application> foundApplicationsToListing = applicationService.findApplicationsToListing(testlisting);

        assertEquals(foundApplicationsToListing.get(0).getId(),testapplication.getId());
        assertEquals(foundApplicationsToListing.get(0).getCreationDate(),testapplication.getCreationDate());
        assertEquals(foundApplicationsToListing.get(0).getApplicant(),testapplication.getApplicant());
        assertEquals(foundApplicationsToListing.get(0).getApplicant(),testapplication.getApplicant());
        assertEquals(foundApplicationsToListing.get(0).getListing(),testapplication.getListing());
        assertEquals(foundApplicationsToListing.get(0).getApplicationStatus(),testapplication.getApplicationStatus());

    }

    @Test
    public void findApplicationsToListing_none() {
        Mockito.when(applicationRepository.findByListing_Id(testlisting.getId())).thenReturn(List.of());

        List<Application> foundApplicationsToListing = applicationService.findApplicationsToListing(testlisting.getId());

        assertEquals(foundApplicationsToListing.size(), 0);
    }

    @Test
    public void findApplicationById_success() {
        Mockito.when(applicationRepository.findById(testapplication.getId())).thenReturn(Optional.of(testapplication));

        Optional<Application> foundApplicationId = applicationService.findApplicationById(testapplication.getId());

        assertEquals(foundApplicationId.get().getId(),testapplication.getId());
        assertEquals(foundApplicationId.get().getCreationDate(),testapplication.getCreationDate());
        assertEquals(foundApplicationId.get().getApplicant(),testapplication.getApplicant());
        assertEquals(foundApplicationId.get().getApplicant(),testapplication.getApplicant());
        assertEquals(foundApplicationId.get().getListing(),testapplication.getListing());
        assertEquals(foundApplicationId.get().getApplicationStatus(),testapplication.getApplicationStatus());

        Optional<Application> foundApplication = applicationService.findApplicationById(testapplication);

        assertEquals(foundApplication.get().getId(),testapplication.getId());
        assertEquals(foundApplication.get().getCreationDate(),testapplication.getCreationDate());
        assertEquals(foundApplication.get().getApplicant(),testapplication.getApplicant());
        assertEquals(foundApplication.get().getApplicant(),testapplication.getApplicant());
        assertEquals(foundApplication.get().getListing(),testapplication.getListing());
        assertEquals(foundApplication.get().getApplicationStatus(),testapplication.getApplicationStatus());

    }

    @Test
    public void findApplicationById_none() {
        Mockito.when(applicationRepository.findById(testapplication.getId())).thenReturn(Optional.empty());

        Optional<Application> foundApplication = applicationService.findApplicationById(testapplication.getId());

        assertFalse(applicationService.findApplicationById(testapplication.getId()).isPresent());

    }

    @Test
    public void createApplication_success() {
        Mockito.when(applicationRepository.saveAndFlush(testapplication)).thenReturn(testapplication);
        Mockito.when(userRepository.getOne(testUserStudent.getId())).thenReturn(testUserStudent);
        Mockito.when(userRepository.findById(testUserStudent.getId())).thenReturn(Optional.of(testUserStudent));
        Mockito.when(listingRepository.getOne(testlisting.getId())).thenReturn(testlisting);
        Mockito.when(listingRepository.findById(testlisting.getId())).thenReturn(Optional.of(testlisting));

        Application createdApplication = applicationService.createApplication(testapplication);

        assertEquals(createdApplication.getId(),testapplication.getId());
        assertEquals(createdApplication.getCreationDate(),testapplication.getCreationDate());
        assertEquals(createdApplication.getApplicant(),testapplication.getApplicant());
        assertEquals(createdApplication.getApplicant(),testapplication.getApplicant());
        assertEquals(createdApplication.getListing(),testapplication.getListing());
        assertEquals(createdApplication.getApplicationStatus(),testapplication.getApplicationStatus());
    }

    @Test
    public void createApplication_throws_user_not_found() {
        Mockito.when(listingRepository.getOne(testlisting.getId())).thenReturn(testlisting);
        Mockito.when(listingRepository.findById(testlisting.getId())).thenReturn(Optional.of(testlisting));

        assertThrows(ResponseStatusException.class, () -> applicationService.createApplication(testapplication));

    }

    @Test
    public void createApplication_throws_user_not_student() {
        Mockito.when(listingRepository.getOne(testlisting.getId())).thenReturn(testlisting);
        Mockito.when(listingRepository.findById(testlisting.getId())).thenReturn(Optional.of(testlisting));
        Mockito.when(userRepository.getOne(testUserStudent.getId())).thenReturn(testUserStudent);
        Mockito.when(userRepository.findById(testUserStudent.getId())).thenReturn(Optional.of(testUserStudent));

        testUserStudent.setRole(Role.LANDLORD);
        testapplication.setApplicant(testUserStudent);

        assertThrows(ResponseStatusException.class, () -> applicationService.createApplication(testapplication));

    }

    @Test
    public void createApplication_throws_listing_not_found() {
        Mockito.when(userRepository.getOne(testUserStudent.getId())).thenReturn(testUserStudent);
        Mockito.when(userRepository.findById(testUserStudent.getId())).thenReturn(Optional.of(testUserStudent));

        assertThrows(ResponseStatusException.class, () -> applicationService.createApplication(testapplication));

    }

    @Test
    public void createApplication_throws_invalid_not_available() {
        Mockito.when(applicationRepository.saveAndFlush(testapplication)).thenReturn(testapplication);
        Mockito.when(userRepository.getOne(testUserStudent.getId())).thenReturn(testUserStudent);
        Mockito.when(userRepository.findById(testUserStudent.getId())).thenReturn(Optional.of(testUserStudent));
        Mockito.when(listingRepository.getOne(testlisting.getId())).thenReturn(testlisting);
        Mockito.when(listingRepository.findById(testlisting.getId())).thenReturn(Optional.of(testlisting));

        testlisting.setAvailable(false);

        assertThrows(ResponseStatusException.class, () -> applicationService.createApplication(testapplication));
    }

    @Test
    public void createApplication_throws_invalid_not_published() {
        Mockito.when(applicationRepository.saveAndFlush(testapplication)).thenReturn(testapplication);
        Mockito.when(userRepository.getOne(testUserStudent.getId())).thenReturn(testUserStudent);
        Mockito.when(userRepository.findById(testUserStudent.getId())).thenReturn(Optional.of(testUserStudent));
        Mockito.when(listingRepository.getOne(testlisting.getId())).thenReturn(testlisting);
        Mockito.when(listingRepository.findById(testlisting.getId())).thenReturn(Optional.of(testlisting));

        testlisting.setPublished(false);

        assertThrows(ResponseStatusException.class, () -> applicationService.createApplication(testapplication));
    }

    @Test
    public void createApplication_throws_invalid_not_for_gender() {
        Mockito.when(applicationRepository.saveAndFlush(testapplication)).thenReturn(testapplication);
        Mockito.when(userRepository.getOne(testUserStudent.getId())).thenReturn(testUserStudent);
        Mockito.when(userRepository.findById(testUserStudent.getId())).thenReturn(Optional.of(testUserStudent));
        Mockito.when(listingRepository.getOne(testlisting.getId())).thenReturn(testlisting);
        Mockito.when(listingRepository.findById(testlisting.getId())).thenReturn(Optional.of(testlisting));

        List<Gender> availableToFemale = new ArrayList<>();
        availableToFemale.add(Gender.FEMALE);

        testlisting.setAvailableTo(availableToFemale);

        assertThrows(ResponseStatusException.class, () -> applicationService.createApplication(testapplication));
    }

    @Test
    public void createApplication_throws_invalid_already_open() {
        Mockito.when(applicationRepository.saveAndFlush(testapplication)).thenReturn(testapplication);
        Mockito.when(userRepository.getOne(testUserStudent.getId())).thenReturn(testUserStudent);
        Mockito.when(userRepository.findById(testUserStudent.getId())).thenReturn(Optional.of(testUserStudent));
        Mockito.when(listingRepository.getOne(testlisting.getId())).thenReturn(testlisting);
        Mockito.when(listingRepository.findById(testlisting.getId())).thenReturn(Optional.of(testlisting));

        Mockito.when(applicationRepository.findByApplicant_Id(testUserStudent.getId())).thenReturn(List.of(testapplication,testapplication));

        assertThrows(ResponseStatusException.class, () -> applicationService.createApplication(testapplication));
    }

    @Test
    public void createApplication_none_already_open() {
        Mockito.when(applicationRepository.saveAndFlush(testapplication)).thenReturn(testapplication);
        Mockito.when(userRepository.getOne(testUserStudent.getId())).thenReturn(testUserStudent);
        Mockito.when(userRepository.findById(testUserStudent.getId())).thenReturn(Optional.of(testUserStudent));
        Mockito.when(listingRepository.getOne(testlisting.getId())).thenReturn(testlisting);
        Mockito.when(listingRepository.findById(testlisting.getId())).thenReturn(Optional.of(testlisting));

        Mockito.when(applicationRepository.findByApplicant_Id(testUserStudent.getId())).thenReturn(List.of());

        Application createdApplication = applicationService.createApplication(testapplication);

        assertEquals(createdApplication.getId(),testapplication.getId());
        assertEquals(createdApplication.getCreationDate(),testapplication.getCreationDate());
        assertEquals(createdApplication.getApplicant(),testapplication.getApplicant());
        assertEquals(createdApplication.getApplicant(),testapplication.getApplicant());
        assertEquals(createdApplication.getListing(),testapplication.getListing());
        assertEquals(createdApplication.getApplicationStatus(),testapplication.getApplicationStatus());
    }


    @Test
    public void updateApplication_throws() {
        Mockito.when(applicationRepository.saveAndFlush(testapplication)).thenReturn(testapplication);
        Mockito.when(userRepository.getOne(testUserStudent.getId())).thenReturn(testUserStudent);
        Mockito.when(userRepository.findById(testUserStudent.getId())).thenReturn(Optional.of(testUserStudent));
        Mockito.when(listingRepository.getOne(testlisting.getId())).thenReturn(testlisting);
        Mockito.when(listingRepository.findById(testlisting.getId())).thenReturn(Optional.of(testlisting));

        assertThrows(ResponseStatusException.class, () -> applicationService.updateApplication(testapplication));
    }

    @Test
    public void updateApplication_success() {
        Mockito.when(applicationRepository.saveAndFlush(testapplication)).thenReturn(testapplication);
        Mockito.when(userRepository.getOne(testUserStudent.getId())).thenReturn(testUserStudent);
        Mockito.when(userRepository.findById(testUserStudent.getId())).thenReturn(Optional.of(testUserStudent));
        Mockito.when(listingRepository.getOne(testlisting.getId())).thenReturn(testlisting);
        Mockito.when(listingRepository.findById(testlisting.getId())).thenReturn(Optional.of(testlisting));
        Mockito.when(applicationRepository.findById(testapplication.getId())).thenReturn(Optional.of(testapplication));

        Application updatedApplication = applicationService.updateApplication(testapplication);

        assertEquals(updatedApplication.getId(),testapplication.getId());
        assertEquals(updatedApplication.getCreationDate(),testapplication.getCreationDate());
        assertEquals(updatedApplication.getApplicant(),testapplication.getApplicant());
        assertEquals(updatedApplication.getApplicant(),testapplication.getApplicant());
        assertEquals(updatedApplication.getListing(),testapplication.getListing());
        assertEquals(updatedApplication.getApplicationStatus(),testapplication.getApplicationStatus());
    }

    @Test
    public void deleteApplicationId_success() {
        Mockito.when(applicationService.findApplicationById(testapplication.getId())).thenReturn(Optional.of(testapplication));
        applicationService.deleteApplication(testapplication.getId());
        applicationRepository.deleteById(testapplication.getId());

        Mockito.when(applicationService.findApplicationById(testapplication.getId())).thenReturn(Optional.empty());

        assertFalse(applicationService.findApplicationById(testapplication.getId()).isPresent());
    }

    @Test
    public void deleteApplication_success() {
        Mockito.when(applicationService.findApplicationById(testapplication.getId())).thenReturn(Optional.of(testapplication));
        applicationService.deleteApplication(testapplication);
        applicationRepository.delete(testapplication);

        Mockito.when(applicationService.findApplicationById(testapplication.getId())).thenReturn(Optional.empty());

        assertFalse(applicationService.findApplicationById(testapplication.getId()).isPresent());
    }

    @Test
    public void decideOnApplication_success() {

        Application applicationInput = new Application(
                testapplication.getId(),
                new Date(),
                testUserStudent,
                testlisting,
                ApplicationStatus.ACCEPTED
        );

        Mockito.when(userRepository.findById(applicationInput.getApplicant().getId())).thenReturn(Optional.of(testUserStudent));
        Mockito.when(listingRepository.findById(applicationInput.getListing().getId())).thenReturn(Optional.of(testlisting));
        Mockito.when(applicationRepository.findById(applicationInput.getId())).thenReturn(Optional.of(testapplication));
        Mockito.when(userRepository.getOne(applicationInput.getListing().getPublisher().getId())).thenReturn(testUserLandlord);

        Mockito.when(applicationRepository.saveAndFlush(Mockito.any())).thenReturn(testapplication);

        Application updatedApplication = applicationService.decideOnApplication(applicationInput, testUserLandlord);

        testapplication.setApplicationStatus(ApplicationStatus.ACCEPTED);

        assertEquals(updatedApplication.getId(),testapplication.getId());
        assertEquals(updatedApplication.getCreationDate(),testapplication.getCreationDate());
        assertEquals(updatedApplication.getApplicant(),testapplication.getApplicant());
        assertEquals(updatedApplication.getApplicant(),testapplication.getApplicant());
        assertEquals(updatedApplication.getListing(),testapplication.getListing());
        assertEquals(updatedApplication.getApplicationStatus(),testapplication.getApplicationStatus());

    }



    @Test
    public void decideOnApplication_throw_user_not_found() {
        Mockito.when(applicationRepository.findById(testapplication.getId())).thenReturn(Optional.of(testapplication));

        testapplication.setApplicationStatus(ApplicationStatus.ACCEPTED);

        assertThrows(ResponseStatusException.class, () -> applicationService.decideOnApplication(testapplication,testUserLandlord));
    }



}
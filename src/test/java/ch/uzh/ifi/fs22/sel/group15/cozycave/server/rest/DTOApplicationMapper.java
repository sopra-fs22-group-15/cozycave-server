package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest;


import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ApplicationStatus;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications.Application;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.UserDetails;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PictureGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.applications.ApplicationGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.applications.ApplicationPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.ApplicationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOApplicationMapper {

    private UserGetDto applicantGetDto;
    private User applicant;
    private ListingGetDto listingGetDto;
    private Listing listing;
    private List<PictureGetDto> picturesGetDto;
    private List<PictureGetDto> floorplanGetDto;
    private List<Picture> pictures;
    private List<Picture> floorplan;


    @BeforeEach
    public void setup() {
        applicantGetDto = new UserGetDto(
                UUID.randomUUID(),
                new Date(),
                new UserGetDto.AuthenticationDataDto(
                        "test@uzh.ch",
                        "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZTk5NTlkNS1lZTBmLTQwODUtOGYzOC0yNDk5MzY2MjM4YWYiLCJpYXQiOjE2NTI1MTk1MTMsIm5iZiI6MTY1MjUxOTUxMywiZXhwIjoxNjUzMTI0MzEzLCJyb2xlcyI6WyJMQU5ETE9SRCJdfQ.ITu0tTJd29s6qnX27LY5cPn-rQv10Id3WFkMHIUErfe5k8uIXk7IdBNYyU70F1kvruvVZGAkLn5BE4UpmavVhw"
                ),
                Role.STUDENT,
                new UserGetDto.UserDetailsDto(
                        "Test",
                        "uzh",
                        Gender.FEMALE,
                        null,
                        null,
                        null,
                        "+41123456789",
                        "BIO",
                        null
                )
        );

        picturesGetDto = new ArrayList<>();
        floorplanGetDto = new ArrayList<>();

        listingGetDto = new ListingGetDto(
                UUID.randomUUID(),
                new Date(),
                "Test WG",
                "Test WG Description",
                null,
                true,
                150.0,
                ListingType.HOUSE,
                true,
                null,
                true,
                1600.0,
                3000.0,
                4.5,
                null,
                picturesGetDto,
                floorplanGetDto
        );

        applicant = new User(
                UUID.randomUUID(),
                new Date(),
                new AuthenticationData(
                        UUID.randomUUID(),
                        "test@uzh.ch",
                        "Rand0m!",
                        "SALT!"
                ),
                Role.STUDENT,
                new UserDetails(
                        UUID.randomUUID(),
                        "Test",
                        "uzh",
                        Gender.FEMALE,
                        null,
                        null,
                        null,
                        "+41123456789",
                        "BIO",
                        null
                )
        );

        pictures = new ArrayList<>();
        floorplan = new ArrayList<>();

        listing = new Listing(
                UUID.randomUUID(),
                new Date(),
                "Test WG",
                "Test WG Description",
                null,
                true,
                150.0,
                ListingType.HOUSE,
                true,
                null,
                true,
                1600.0,
                3000.0,
                4.5,
                null,
                pictures,
                floorplan
        );
    }

    @Test
    public void applicationGetDto_To_Application_success() {

        ApplicationGetDto applicationGetDto = new ApplicationGetDto();

        applicationGetDto.setId(UUID.randomUUID());
        applicationGetDto.setCreationDate(new Date());
        applicationGetDto.setApplicant(applicantGetDto);
        applicationGetDto.setListing(listingGetDto);
        applicationGetDto.setApplicationStatus(ApplicationStatus.PENDING);

        Application application = ApplicationMapper.INSTANCE.applicationGetDtoToApplication(applicationGetDto);


        assertEquals(applicationGetDto.getId(), application.getId());
        assertEquals(applicationGetDto.getCreationDate(), application.getCreationDate());
        assertEquals(applicationGetDto.getApplicationStatus(), application.getApplicationStatus());
    }

    @Test
    public void application_To_ApplicationGetDto_success() {

        Application application = new Application();

        application.setId(UUID.randomUUID());
        application.setCreationDate(new Date());
        application.setApplicant(applicant);
        application.setListing(listing);
        application.setApplicationStatus(ApplicationStatus.PENDING);

        ApplicationGetDto applicationGetDto = ApplicationMapper.INSTANCE.applicationToApplicationGetDto(application);

        assertEquals(applicationGetDto.getId(), application.getId());
        assertEquals(applicationGetDto.getCreationDate(), application.getCreationDate());
        assertEquals(applicationGetDto.getApplicationStatus(), application.getApplicationStatus());

    }

    @Test
    public void applicationPostPutDto_To_Application_success() {
        ApplicationPostPutDto applicationPostPutDto = new ApplicationPostPutDto();

        applicationPostPutDto.setId(UUID.randomUUID());
        applicationPostPutDto.setCreationDate(new Date());
        applicationPostPutDto.setApplicant(applicantGetDto);
        applicationPostPutDto.setListing(listingGetDto);
        applicationPostPutDto.setApplicationStatus(ApplicationStatus.PENDING);

        Application application = ApplicationMapper.INSTANCE.applicationPostPutDtoToApplication(applicationPostPutDto);

        assertEquals(applicationPostPutDto.getId(), application.getId());
        assertEquals(applicationPostPutDto.getCreationDate(), application.getCreationDate());
        assertEquals(applicationPostPutDto.getApplicationStatus(), application.getApplicationStatus());

    }

    @Test
    public void application_To_ApplicationPostPutDto_success() {

        Application application = new Application();

        application.setId(UUID.randomUUID());
        application.setCreationDate(new Date());
        application.setApplicant(applicant);
        application.setListing(listing);
        application.setApplicationStatus(ApplicationStatus.PENDING);

        ApplicationPostPutDto applicationPostPutDto = ApplicationMapper.INSTANCE.applicationToApplicationPostPutDto(application);

        assertEquals(applicationPostPutDto.getId(), application.getId());
        assertEquals(applicationPostPutDto.getCreationDate(), application.getCreationDate());
        assertEquals(applicationPostPutDto.getApplicationStatus(), application.getApplicationStatus());

    }


}



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
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.*;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.ApplicationMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOUserMapper {

    private User user;
    private UserGetDto userGetDto;
    private UserPostPutDto userPostPutDto;
    private UserGetPublicDto userGetPublicDto;
    private UserGetGTDto userGetGTDto;
    private UserGetGTPublicDto userGetGTPublicDto;


    @BeforeEach
    public void setup() {

        user = new User(
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

        userGetDto = new UserGetDto(
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
        userPostPutDto = new UserPostPutDto(
                new UserPostPutDto.AuthenticationDataDto(
                        "test@uzh.ch",
                        "Rand0m!"
                ),
                Role.STUDENT,
                new UserPostPutDto.UserDetailsDto(
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
        userGetPublicDto = new UserGetPublicDto(
                UUID.randomUUID(),
                new UserGetPublicDto.UserDetailsDto(
                        "Test",
                        "uzh",
                        "BIO",
                        null
                )
        );

        userGetGTDto = new UserGetGTDto(
                UUID.randomUUID(),
                new UserGetGTDto.UserDetailsDto(
                        "Test",
                        "uzh",
                        "BIO",
                        Gender.FEMALE,
                        null,
                        null,
                        "+41123456789"
                ),
                new UserGetGTDto.AuthenticationDataDto(
                        "test@uzh.ch"
                )
        );

        userGetGTPublicDto = new UserGetGTPublicDto(
                UUID.randomUUID(),
                new UserGetGTPublicDto.UserDetailsDto(
                        "Test",
                        "uzh",
                        "BIO",
                        Gender.FEMALE,
                        null,
                        null
                )
        );
    }

    @Test
    public void userGetDto_To_User() {

        User user = UserMapper.INSTANCE.userGetDtoToUser(userGetDto);

        assertEquals(user.getId(), userGetDto.getId());
        assertEquals(user.getCreationDate(), userGetDto.getCreationDate());
        assertEquals(user.getRole(), userGetDto.getRole());

    }

    @Test
    public void user_To_UserGetDto() {
        UserGetDto userGetDto = UserMapper.INSTANCE.userToUserGetDto(user);

        assertEquals(user.getId(), userGetDto.getId());
        assertEquals(user.getCreationDate(), userGetDto.getCreationDate());
        assertEquals(user.getRole(), userGetDto.getRole());
    }

    @Test
    public void userGetPublicDto_To_User() {
        User user = UserMapper.INSTANCE.userGetPublicDtoToUser(userGetPublicDto);

        assertEquals(user.getId(), userGetPublicDto.getId());

    }

    @Test
    public void user_To_UserGetPublicDto() {

        UserGetPublicDto userGetPublicDto = UserMapper.INSTANCE.userToUserGetPublicDto(user);

        assertEquals(user.getId(), userGetPublicDto.getId());
        assertEquals(user.getDetails().getFirstName(), userGetPublicDto.getDetails().getFirstName());
        assertEquals(user.getDetails().getLastName(), userGetPublicDto.getDetails().getLastName());

    }

    @Test
    public void userPostPutDto_To_User() {

        User user = UserMapper.INSTANCE.userPostPutDtoToUser(userPostPutDto);

        assertEquals(user.getRole(), userPostPutDto.getRole());
        assertEquals(user.getDetails().getLastName(), userPostPutDto.getDetails().getLastName());
        assertEquals(user.getDetails().getFirstName(), userPostPutDto.getDetails().getFirstName());

    }

    @Test
    public void user_To_UserPostPutDto() {

        UserPostPutDto userPostPutDto = UserMapper.INSTANCE.userToUserPostPutDto(user);

        assertEquals(user.getRole(), userPostPutDto.getRole());
        assertEquals(user.getDetails().getFirstName(), userPostPutDto.getDetails().getFirstName());
        assertEquals(user.getDetails().getLastName(), userPostPutDto.getDetails().getLastName());

    }

    @Test
    public void user_To_UserGetGTDto() {

        UserGetGTDto userGetGTDto = UserMapper.INSTANCE.userToUserGetGTDto(user);

        assertEquals(user.getId(), userGetGTDto.getId());
        assertEquals(user.getDetails().getFirstName(), userGetGTDto.getDetails().getFirstName());
        assertEquals(user.getDetails().getLastName(), userGetGTDto.getDetails().getLastName());
        assertEquals(user.getAuthenticationData().getEmail(), userGetGTDto.getAuthenticationData().getEmail());

    }

    @Test
    public void user_To_UserGetGTPublicDto() {

        UserGetGTPublicDto userGetGTPublicDto = UserMapper.INSTANCE.userToUserGetGTPublicDto(user);

        assertEquals(user.getId(), userGetGTPublicDto.getId());
        assertEquals(user.getDetails().getFirstName(), userGetGTPublicDto.getDetails().getFirstName());
        assertEquals(user.getDetails().getLastName(), userGetGTPublicDto.getDetails().getLastName());
        assertEquals(user.getDetails().getBiography(), userGetGTPublicDto.getDetails().getBiography());
        assertEquals(user.getDetails().getGender(), userGetGTPublicDto.getDetails().getGender());


    }

    @Test
    public void ageConversion() {

        assertEquals(UserMapper.ageConversion(new Date()), 0);

    }

}



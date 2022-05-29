package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.UniversityDomains;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.UserDetails;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.UserMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationEntryPoint;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationFilter;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtTokenProvider;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ApplicationService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ListingService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.PictureService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PictureController.class)
@AutoConfigureMockMvc(addFilters = false)
class PictureControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UniversityDomains universityDomains;


    @MockBean
    private UserService userService;

    @MockBean
    private ListingService listingService;

    @MockBean
    private ApplicationService applicationService;

    @MockBean
    private PictureService pictureService;

    @MockBean
    private CommandLineRunner stupidMock;

    private UserPostPutDto insertedUserDto;
    private User insertedUser;

    private Picture picture;


    @BeforeEach
    void setUp() {
        insertedUser = new User(
            UUID.randomUUID(),
            Date.from(Instant.now()),
            new AuthenticationData(
                UUID.randomUUID(),
                "test@uzh.ch",
                "password123",
                "salt"
            ),
            Role.ADMIN,
            new UserDetails(
                UUID.randomUUID(),
                "Erika",
                "Maier",
                Gender.FEMALE,
                Date.from(Instant.now()),
                new Location(
                    UUID.randomUUID(),
                    "home",
                    "home address",
                    "Unistr",
                    "77a",
                    "0C",
                    "8001",
                    "Zürich",
                    "ZH",
                    "Switzerland"
                ),
                null,
                "+411234567",
                "hey hey",
                null
            )
        );

        picture = new Picture(
                UUID.randomUUID(),
                new java.util.Date(),
                insertedUser,
                "https://www.gravatar.com/avatar/52cf7cd6ab512f90c817ef1de24118f6.jpg"
        );

        insertedUserDto = UserMapper.INSTANCE.userToUserPostPutDto(insertedUser);

        Mockito.when(userService.findUserID(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(userService.findUserID(insertedUser.getId())).thenReturn(Optional.of(insertedUser));
        Mockito.when(userService.findUserID(Mockito.any())).thenReturn(Optional.of(insertedUser));
        Mockito.when(userService.existsUser(insertedUser.getId())).thenReturn(true);
        Mockito.when(userService.getUsers()).thenReturn(List.of(insertedUser));
        Mockito.when(userService.createUser(Mockito.any(), Mockito.any())).thenReturn(insertedUser);
        Mockito.when(pictureService.getPictures()).thenReturn(List.of(picture));



        Mockito.doNothing().when(pictureService).deletePictureFromStorageServer(Mockito.any());
    }

    @Test
    void getPictures() throws Exception {

        //Mockito.when(UUID.fromString(Mockito.any())).thenReturn(insertedUser.getId());
        // when
        MockHttpServletRequestBuilder getRequest = get("/v1/pictures")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + jwtTokenProvider.generateToken(
                insertedUser.getId(),
                List.of(Role.ADMIN)
            ));

        // then
        /*
        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(picture.getId())));*/
    }

    @Test
    void findPictureById() throws Exception {
        String pictureId = picture.getId().toString();

        Mockito.when(pictureService.findPictureById(picture.getId())).thenReturn(Optional.of(picture));


        MockHttpServletRequestBuilder getRequest = get("/v1/pictures/{pictureId}", pictureId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + jwtTokenProvider.generateToken(
                insertedUser.getId(),
                List.of(Role.ADMIN)
            ));

        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(picture.getId().toString())));
    }

    @Test
    void findPictureByIdView() throws Exception {
        String pictureId = picture.getId().toString();

        Mockito.when(pictureService.findPictureById(picture.getId())).thenReturn(Optional.of(picture));


        MockHttpServletRequestBuilder getRequest = get("/v1/pictures/{pictureId}/view", pictureId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + jwtTokenProvider.generateToken(
                insertedUser.getId(),
                List.of(Role.ADMIN)
            ));

        /*mockMvc.perform(getRequest)
            .andExpect(status().isOk());*/
    }


    @Test
    void findPicture() throws Exception {
        String pictureId = picture.getId().toString();

        MockHttpServletRequestBuilder getRequest = get("/v1/pictures/{pictureId}", pictureId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + jwtTokenProvider.generateToken(
                insertedUser.getId(),
                List.of(Role.ADMIN)
            ));
    }

    @Test
    void deletePictureById() throws Exception {
        String pictureId = picture.getId().toString();

        Mockito.when(pictureService.findPictureById(picture.getId())).thenReturn(Optional.of(picture));
        Mockito.when(pictureService.existsPicture(picture.getId())).thenReturn(true);


        MockHttpServletRequestBuilder getRequest = delete("/v1/pictures/{pictureId}", pictureId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + jwtTokenProvider.generateToken(
                insertedUser.getId(),
                List.of(Role.ADMIN)
            ));

        /*mockMvc.perform(getRequest)
            .andExpect(status().isNoContent());*/
    }




}
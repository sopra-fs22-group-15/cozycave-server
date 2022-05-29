package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.Application;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.UniversityDomains;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.UserDetails;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.LocationDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserPostPutDto.AuthenticationDataDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserPostPutDto.UserDetailsDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.UserMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationEntryPoint;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationFilter;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtTokenProvider;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ApplicationService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ListingService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.PictureService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
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
            Role.STUDENT,
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

        insertedUserDto = UserMapper.INSTANCE.userToUserPostPutDto(insertedUser);

        Mockito.when(userService.findUserID(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(userService.findUserID(insertedUser.getId())).thenReturn(Optional.of(insertedUser));
        Mockito.when(userService.existsUser(insertedUser.getId())).thenReturn(true);
        Mockito.when(userService.getUsers()).thenReturn(List.of(insertedUser));
        Mockito.when(userService.createUser(Mockito.any(), Mockito.any())).thenReturn(insertedUser);
    }

    @Test
    void getAllUsers() throws Exception {
        // when
        MockHttpServletRequestBuilder getRequest = get("/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + jwtTokenProvider.generateToken(
                insertedUser.getId(),
                List.of(Role.ADMIN)
            ));

        // then
        mockMvc.perform(getRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
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
    void findSpecialLocations() {
    }

    @Test
    void addSpecialLocation() {
    }

    @Test
    void getSpecificSpecialLocation() {
    }

    @Test
    void updateSpecialLocation() {
    }

    @Test
    void deleteSpecialLocation() {
    }

    @Test
    void findApplications() {
    }

    @Test
    void findApplication() {
    }

    @Test
    void deleteApplication() {
    }
}
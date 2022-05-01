package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.UserDetails;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User permittedTestUser;
    private User unpermittedTestUser;

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
            Role.TEAM,
            new UserDetails(
                UUID.randomUUID(),
                "Erika",
                "Mustermann",
                Gender.FEMALE,
                Date.from(Instant.now().minus(50, ChronoUnit.YEARS)),
                null,
                "bio"
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
                Date.from(Instant.now().minus(45, ChronoUnit.YEARS)),
                null,
                "bio"
            )
        );

        Mockito.when(userService.getUsers()).thenReturn(List.of(permittedTestUser, unpermittedTestUser));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllUsers() throws Exception {
        /*mockMvc.perform(
            MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());*/
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
}
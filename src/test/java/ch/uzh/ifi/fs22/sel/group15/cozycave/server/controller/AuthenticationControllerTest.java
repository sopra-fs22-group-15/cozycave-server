package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.UniversityDomains;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.LocationDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserPostPutDto.AuthenticationDataDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserPostPutDto.UserDetailsDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationEntryPoint;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtAuthenticationFilter;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtTokenProvider;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ListingService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.PictureService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(AuthenticationController.class)
@TestPropertySource("classpath:application-test.properties")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;


    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UniversityDomains universityDomains;
    @MockBean
    private PictureService pictureService;
    @MockBean
    private ListingService listingService;


    private UserPostPutDto userFullFeaturedDto;
    private User userFullFeatured;

    @BeforeEach
    void setUp() {
        userFullFeaturedDto = new UserPostPutDto();
        userFullFeaturedDto.setAuthenticationData(new AuthenticationDataDto("example@test.uzh.ch", "p4ssw0rd"));
        userFullFeaturedDto.setDetails(new UserDetailsDto(
            "Erika",
            "Mustermann",
            Gender.FEMALE,
            Date.from(Instant.now()),
            new LocationDto(
                null,
                "home",
                null,
                "Universitätsstrasse",
                "78",
                null,
                "8001",
                "Zürich",
                "Zürich",
                "CH"
            ),
            null,
            "+41 123 45 67",
            "hi, im erika :)",
            null
        ));


    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void register_validData() throws Exception {
        // when
        MockHttpServletRequestBuilder getRequest = post("/authentication/register")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(asJsonString(userFullFeaturedDto));

        // then
//        mockMvc.perform(getRequest)
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$", hasSize(1)))
//            .andExpect(jsonPath("$[0].details.first_name", is(userFullFeaturedDto.getDetails().getFirstName())));
    }

    @Test
    void login() {
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format("The request body could not be created.%s", e));
        }
    }
}
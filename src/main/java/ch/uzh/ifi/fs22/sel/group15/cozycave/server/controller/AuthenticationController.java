package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.UserDetails;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserPostDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.UserMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtTokenProvider;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/auth")
//TODO: add login, logout
public class AuthenticationController {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(UserService userService, PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDto register(@RequestBody UserPostDto userPostDto) {
        log.debug("new user registration try: {}", userPostDto.toString());

        Location address = null;
        if (userPostDto.getDetails().getAddress() != null) {
            address = new Location(
                "home",
                userPostDto.getDetails().getAddress().getStreet(),
                userPostDto.getDetails().getAddress().getStreetNumber(),
                userPostDto.getDetails().getAddress().getZipCode(),
                userPostDto.getDetails().getAddress().getVillage(),
                userPostDto.getDetails().getAddress().getCountry()
            );
        }

        //User test = UserMapper.INSTANCE.userPostDtoToUser(userPostDto);
        //log.debug("Test: {}", test.toString());

        User user = userService.createUser(new User(
            null,
            null,
            new AuthenticationData(
                null,
                userPostDto.getAuthenticationData().getEmail(),
                userPostDto.getAuthenticationData().getPassword(),
                null
            ),
            userPostDto.getRole(),
            new UserDetails(
                null,
                userPostDto.getDetails().getFirstname(),
                userPostDto.getDetails().getLastname(),
                userPostDto.getDetails().getGender(),
                userPostDto.getDetails().getBirthday(),
                address,
                userPostDto.getDetails().getBiography()
            )
        ), null);

        UserGetDto result = UserMapper.INSTANCE.userToUserGetDto(user);
        System.out.println("test3");

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                user.getId().toString(),
                userPostDto.getAuthenticationData().getPassword() + user.getAuthenticationData().getSalt()
            )
        );
        System.out.println("test4");

        result.getAuthenticationData().setToken(
            jwtTokenProvider.generateToken(authentication)
        );
        System.out.println("test5");

        return result;
    }
}

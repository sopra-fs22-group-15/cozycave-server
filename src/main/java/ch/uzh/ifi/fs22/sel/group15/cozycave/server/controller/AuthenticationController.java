package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.UserMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtTokenProvider;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.PictureService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/v1/auth")
// TODO: add logout possibility if required (individual token for each user)
public class AuthenticationController {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserService userService;
    private final PictureService pictureService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(UserService userService, PictureService pictureService,PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.pictureService = pictureService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDto register(@RequestBody UserPostPutDto userPostPutDto) {
        log.debug("new user registration try: {}", userPostPutDto.toString());

        User toBeCreatedUser = UserMapper.INSTANCE.userPostPutDtoToUser(userPostPutDto);

        User user = userService.createUser(toBeCreatedUser, null);
        // set default picture to gravatar profile picture
        if (user.getDetails().getPicture() == null) {
            //createdUser.getDetails().setPicture(setGravatarPicture(newUser));
            user = pictureService.setGravatarPicture(user);
        }

        UserGetDto result = UserMapper.INSTANCE.userToUserGetDto(user);

        Authentication authentication = createAuthentication(
            user,
            userPostPutDto.getAuthenticationData().getPassword()
        );

        result.getAuthenticationData().setToken(
            jwtTokenProvider.generateToken(authentication)
        );

        log.info("new user registered with id: {}", user.getId());

        return result;
    }

    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDto login(@RequestBody UserPostPutDto userPostPutDto) {
        log.debug("new user login try: {}", userPostPutDto.getAuthenticationData().getEmail());

        User user = userService.findUserByEmail(userPostPutDto.getAuthenticationData().getEmail())
            .orElseThrow(() -> {
                log.error("user with email {} not found while logging in",
                    userPostPutDto.getAuthenticationData().getEmail());
                return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid login data");
            });

        UserGetDto result = UserMapper.INSTANCE.userToUserGetDto(user);

        Authentication authentication = createAuthentication(
            user,
            userPostPutDto.getAuthenticationData().getPassword()
        );

        result.getAuthenticationData().setToken(
            jwtTokenProvider.generateToken(authentication)
        );

        log.info("new user logged in with id: {}", user.getId());

        return result;
    }

    private Authentication createAuthentication(User user, String passwordRaw) {
        return authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                user.getId().toString(),
                passwordRaw + user.getAuthenticationData().getSalt(),
                user.getRole().generatePermittedAuthoritiesList()
            )
        );
    }
}

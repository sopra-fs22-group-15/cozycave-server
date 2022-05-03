package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.UserDetails;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.UserMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.security.JwtTokenProvider;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/auth")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
// TODO: add logout possibility if required (individual token for each user)
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
    public UserGetDto register(@RequestBody UserPostPutDto userPostPutDto) {
        log.debug("new user registration try: {}", userPostPutDto.toString());

        Location address = null;
        if (userPostPutDto.getDetails().getAddress() != null) {
            address = new Location(
                userPostPutDto.getDetails().getAddress().getStreet(),
                userPostPutDto.getDetails().getAddress().getHouseNumber(),
                userPostPutDto.getDetails().getAddress().getZipCode(),
                userPostPutDto.getDetails().getAddress().getCity(),
                userPostPutDto.getDetails().getAddress().getCountry()
            );

            if (StringUtils.hasText(userPostPutDto.getDetails().getAddress().getName())) {
                address.setName(userPostPutDto.getDetails().getAddress().getDescription());
            }

            if (StringUtils.hasText(userPostPutDto.getDetails().getAddress().getDescription())) {
                address.setDescription(userPostPutDto.getDetails().getAddress().getDescription());
            }
        }

        User user = userService.createUser(new User(
            null,
            null,
            new AuthenticationData(
                null,
                userPostPutDto.getAuthenticationData().getEmail(),
                userPostPutDto.getAuthenticationData().getPassword(),
                null
            ),
            userPostPutDto.getRole(),
            new UserDetails(
                null,
                userPostPutDto.getDetails().getFirstname(),
                userPostPutDto.getDetails().getLastname(),
                userPostPutDto.getDetails().getGender(),
                userPostPutDto.getDetails().getBirthday(),
                null,
                userPostPutDto.getDetails().getBiography()
            )
        ), address, null);

        UserGetDto result = UserMapper.INSTANCE.userToUserGetDto(user);

        Authentication authentication = createAuthentication(
            user.getId(),
            userPostPutDto.getAuthenticationData().getPassword()
        );

        result.getAuthenticationData().setToken(
            jwtTokenProvider.generateToken(authentication)
        );

        log.debug("new user registered: {}", result);

        return result;
    }

    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDto login(@RequestBody UserPostPutDto userPostPutDto) {
        log.debug("new user login try: {}", userPostPutDto.getAuthenticationData().getEmail());

        User user = userService.findUserByEmail(userPostPutDto.getAuthenticationData().getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Authentication authentication = createAuthentication(user.getId(),
            userPostPutDto.getAuthenticationData().getPassword());

        UserGetDto result = UserMapper.INSTANCE.userToUserGetDto(user);

        result.getAuthenticationData().setToken(
            jwtTokenProvider.generateToken(authentication)
        );

        log.debug("new user logged in: {}", result);

        return result;
    }

    private Authentication createAuthentication(UUID uuid, String passwordRaw) {
        User user = userService.findUserID(uuid)
            .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        return authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                uuid.toString(),
                passwordRaw + user.getAuthenticationData().getSalt(),
                user.getRole().generatePermittedAuthoritiesList()
            )
        );
    }
}

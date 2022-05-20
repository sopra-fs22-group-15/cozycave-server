package ch.uzh.ifi.fs22.sel.group15.cozycave.server;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.UniversityDomains;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.UserDetails;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ListingService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestController
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String helloWorld() {
        return "The API is running.";
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
            }
        };
    }

    @Bean
    public CommandLineRunner createTestData(UserService userService,
        UniversityDomains universityDomains,
        @Value("${cozycave.debug.create_test_user}") boolean createTestUser, ListingService listingService) {
        return args -> {
            if (!createTestUser) {
                return;
            }

            if (userService.existsUser("test@uzh.ch")) {
                return;
            }

            userService.createUser(new User(
                null,
                null,
                new AuthenticationData(
                    null,
                    "test@uzh.ch",
                    "password",
                    null
                ),
                null,
                new UserDetails(
                    UUID.randomUUID(),
                    "Erika",
                    "Mustermann",
                    Gender.FEMALE,
                    Date.from(Instant.now().minus(9000, ChronoUnit.DAYS)),
                    new Location(
                        null,
                        null,
                        null,
                        "Hauptstrasse",
                        "24",
                        null,
                        "5312",
                        "Döttingen",
                        "Aargau",
                        "Switzerland"
                    ),
                    "+41791234567",
                    "bio",
                    null
                )
            ), null);
        };
    }
}

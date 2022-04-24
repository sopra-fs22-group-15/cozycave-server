package ch.uzh.ifi.fs22.sel.group15.cozycave.server;

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
                registry.addMapping("/").allowedOrigins("*").allowedMethods("*");
                registry.addMapping("/v1/**").allowedOrigins("*").allowedMethods(MediaType.APPLICATION_JSON_VALUE);
            }
        };
    }

    /*
    @Bean
    public CommandLineRunner createTestData(UserService userService) {
        return args -> {
            userService.createUser(new User(
                UUID.randomUUID(),
                new Date(),
                new AuthenticationData(
                    UUID.randomUUID(),
                    "test2@uzh.ch",
                    "hashedPassword",
                    "SaLt2",
                    "no token"
                ),
                Role.STUDENT,
                new UserDetails(
                    UUID.randomUUID(),
                    "Erika",
                    "Mustermann",
                    Gender.FEMALE,
                    new Date(),
                    new Location(
                        "Hauptstrasse",
                        "24",
                        5312,
                        "Döttingen",
                        "Switzerland"
                    ),
                    "bio",
                    null
                )
            ));
        };
    }
    */
}

package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.LocationDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class UserPostPutDto implements Serializable {

    @JsonProperty("authentication")
    private AuthenticationDataDto authenticationData;

    private Role role;

    private UserDetailsDto details;

    @AllArgsConstructor @NoArgsConstructor @Data
    public static class AuthenticationDataDto implements Serializable {

        private String email;

        private String password;
    }

    @AllArgsConstructor @NoArgsConstructor @Data
    public static class UserDetailsDto implements Serializable {

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        private Gender gender;

        private Date birthday;

        private LocationDto address;

        @JsonProperty("phone_number")
        private String phoneNumber;

        private String biography;
    }
}

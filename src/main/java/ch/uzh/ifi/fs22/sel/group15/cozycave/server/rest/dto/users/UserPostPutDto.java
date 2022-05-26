package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.LocationDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PicturePostDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class UserPostPutDto implements Serializable {

    @JsonProperty("authentication")
    private AuthenticationDataDto authenticationData;

    private Role role;

    private UserDetailsDto details;

    public UserPostPutDto(@NotNull AuthenticationDataDto authenticationData, Role role,
                          @NotNull UserDetailsDto details) {
        this.authenticationData = authenticationData == null ? new AuthenticationDataDto() : authenticationData;
        this.details = details == null ? new UserDetailsDto() : details;
    }

    public UserPostPutDto() {
        this.authenticationData = new AuthenticationDataDto();
        this.details = new UserDetailsDto();
    }

    public void setAuthenticationData(@NotNull AuthenticationDataDto authenticationData) {
        this.authenticationData = authenticationData == null ? new AuthenticationDataDto() : authenticationData;
    }

    public void setDetails(@NotNull UserDetailsDto details) {
        this.details = details == null ? new UserDetailsDto() : details;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AuthenticationDataDto implements Serializable {

        private String email;

        private String password;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class UserDetailsDto implements Serializable {

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        private Gender gender;

        private Date birthday;

        private LocationDto address;

        @JsonProperty("special_address")
        private List<LocationDto> specialAddress;

        @JsonProperty("phone_number")
        private String phoneNumber;

        private String biography;

        private PicturePostDto picture;
    }
}

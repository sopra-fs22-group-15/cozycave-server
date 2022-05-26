package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.LocationDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserGetDto implements Serializable {

    private UUID id;

    @JsonProperty("creation_date")
    private Date creationDate;

    @JsonProperty("authentication")
    private AuthenticationDataDto authenticationData;

    private Role role;

    private UserDetailsDto details;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AuthenticationDataDto implements Serializable {

        private String email;

        @JsonInclude(Include.NON_EMPTY)
        private String token;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class UserDetailsDto implements Serializable {

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        @JsonInclude(Include.NON_EMPTY)
        private Gender gender;

        @JsonInclude(Include.NON_EMPTY)
        private Date birthday;

        @JsonInclude(Include.NON_EMPTY)
        private LocationDto address;

        @JsonInclude(Include.NON_EMPTY)
        @JsonProperty("special_address")
        private List<LocationDto> specialAddress;

        @JsonInclude(Include.NON_EMPTY)
        @JsonProperty("phone_number")
        private String phoneNumber;

        @JsonInclude(Include.NON_EMPTY)
        private String biography;

        @JsonInclude(Include.NON_EMPTY)
        private PictureDto picture;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PictureDto implements Serializable {
        private UUID id;

        @JsonProperty("creation_date")
        private Date creationDate;

        @JsonProperty("picture_url")
        private String pictureUrl;
    }

}

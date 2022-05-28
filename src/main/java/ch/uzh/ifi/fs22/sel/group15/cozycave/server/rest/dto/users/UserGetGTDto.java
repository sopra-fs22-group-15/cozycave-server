package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PictureGetDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserGetGTDto implements Serializable {

    private UUID id;

    private UserDetailsDto details;

    private AuthenticationDataDto authenticationDataDto;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class UserDetailsDto implements Serializable {

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String biography;

        private PictureGetGTDto picture;

        private Long age;

        @Column(name = "phone_number")
        private String phoneNumber;

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class PictureGetGTDto implements Serializable {
            @JsonProperty("picture_url")
            private String pictureUrl;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class AuthenticationDataDto implements Serializable {

        private String email;

    }

}

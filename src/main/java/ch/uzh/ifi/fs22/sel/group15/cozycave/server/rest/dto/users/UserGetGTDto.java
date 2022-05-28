package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserGetGTDto implements Serializable {

    private UUID id;

    private UserDetailsDto details;

    @SerializedName(value = "authentication")
    private AuthenticationDataDto authenticationData;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class UserDetailsDto implements Serializable {

        @SerializedName(value = "first_name")
        private String firstName;

        @SerializedName(value = "last_name")
        private String lastName;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String biography;

        private Gender gender;

        private PictureGetGTDto picture;

        private Long age;

        @SerializedName(value = "phone_number")
        private String phoneNumber;

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        public static class PictureGetGTDto implements Serializable {
            @SerializedName(value = "picture_url")
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

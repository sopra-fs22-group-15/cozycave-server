package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PictureGetDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserGetPublicDto implements Serializable {

    private UUID id;

    private UserDetailsDto details;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class UserDetailsDto implements Serializable {

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        @JsonInclude(Include.NON_EMPTY)
        private String biography;

        private PictureGetDto picture;
    }
}

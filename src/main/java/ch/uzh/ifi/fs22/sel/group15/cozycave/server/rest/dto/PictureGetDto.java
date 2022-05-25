package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PictureGetDto implements Serializable {

    private UUID id;

    @JsonProperty("creation_date")
    private Date creationDate;

    @JsonProperty("picture_url")
    private String pictureUrl;
}

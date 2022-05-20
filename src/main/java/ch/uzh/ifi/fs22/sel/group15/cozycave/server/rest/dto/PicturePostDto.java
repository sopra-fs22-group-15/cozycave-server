package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data
public class PicturePostDto implements Serializable {

    private UUID id;

    @JsonProperty("picture_url")
    private String pictureUrl;
}

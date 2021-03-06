package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.LocationDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PicturePostDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListingPostPutDto implements Serializable {

    private UUID id;

    private String title;

    private String description;

    private LocationDto address;

    private Boolean published;

    private Double sqm;

    @JsonProperty("listing_type")
    private ListingType listingType;

    private Boolean furnished;

    @JsonProperty("available_to")
    private List<Gender> availableTo;

    private Boolean available;

    private Double rent;

    private Double deposit;

    private Double rooms;

    private List<PicturePostDto> pictures;

    private List<PicturePostDto> floorplan;
}

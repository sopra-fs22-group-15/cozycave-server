package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.LocationDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PictureGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetPublicDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class ListingGetDto implements Serializable {

    private UUID id;

    @JsonProperty("creation_date")
    private Date creationDate;

    private String title;

    private String description;

    private LocationDto address;

    private Boolean published;

    @JsonInclude(Include.NON_NULL)
    private Double sqm;

    @JsonProperty("listing_type")
    private ListingType listingType;

    @JsonInclude(Include.NON_NULL)
    private Boolean furnished;

    @JsonProperty("available_to")
    private List<Gender> availableTo;

    private Boolean available;

    @JsonInclude(Include.NON_NULL)
    private Double rent;

    @JsonInclude(Include.NON_NULL)
    private Double deposit;

    @JsonInclude(Include.NON_NULL)
    private Double rooms;

    private UserGetPublicDto publisher;

    private List<PictureGetDto> pictures;

    private List<PictureGetDto> floorplan;
}

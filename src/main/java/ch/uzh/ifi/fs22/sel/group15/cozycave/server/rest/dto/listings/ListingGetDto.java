package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.LocationDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetPublicDto;
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

    private UserGetPublicDto publisher;
}

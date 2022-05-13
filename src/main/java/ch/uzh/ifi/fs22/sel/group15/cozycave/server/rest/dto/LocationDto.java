package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class LocationDto implements Serializable {

    @JsonInclude(Include.NON_EMPTY)
    private String name;

    @JsonInclude(Include.NON_EMPTY)
    private String description;

    private String street;

    @JsonProperty("house_number")
    private String houseNumber;

    @JsonProperty("apartment_number")
    @JsonInclude(Include.NON_EMPTY)
    private String apartmentNumber;

    @JsonProperty("zip_code")
    private String zipCode;

    private String city;

    @JsonInclude(Include.NON_EMPTY)
    private String state;

    private String country;
}


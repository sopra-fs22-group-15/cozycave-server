package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.applications;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ApplicationStatus;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Data
public class ApplicationPostPutDto {

    private UUID id;

    @JsonProperty("creation_date")
    private Date creationDate;

    private UserGetDto applicant;

    private ListingGetDto listing;

    @JsonProperty("application_status")
    private ApplicationStatus applicationStatus;

}

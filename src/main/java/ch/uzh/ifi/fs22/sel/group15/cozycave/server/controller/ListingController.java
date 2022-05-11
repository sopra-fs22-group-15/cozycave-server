package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.ListingMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ListingService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/v1")
@Slf4j
public class ListingController {

    private final ListingService listingService;
    private final UserService userService;

    ListingController(ListingService listingService, UserService userService) {
        this.listingService = listingService;
        this.userService = userService;
    }

    // Get all listings in a list
    @GetMapping("/listings")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ListingGetDto> getAllListings() {
        return listingService.getListings().stream()
            .map(ListingMapper.INSTANCE::listingToListingGetDto)
            .collect(Collectors.toList());
    }

    // Creates new listing
    @PostMapping("/listings")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ListingGetDto createListing(
        @AuthenticationPrincipal String authUserId,
        @RequestBody ListingPostPutDto listingPostPutDto
    ) {
        Listing listing = ListingMapper.INSTANCE.listingPostPutDtoToListing(listingPostPutDto);

        User publisher = userService.findUserID(UUID.fromString(authUserId))
            .orElseThrow(() -> {
                log.error("user (publisher) with id {} not found while creating listing", authUserId);
                return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error finding publisher");
            });
        listing.setPublisher(publisher);

        Listing createdListing = listingService.createListing(listing);

        log.info("created listing with id {}", createdListing.getId());

        return ListingMapper.INSTANCE.listingToListingGetDto(createdListing);
    }

    // get specific listing
    @GetMapping("/listings/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ListingGetDto findListing(@PathVariable UUID id) {
        return listingService.findListingById(id)
            .map(ListingMapper.INSTANCE::listingToListingGetDto)
            .orElseThrow(() -> {
                log.debug("listing with id {} not found while finding listing", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "listing with id " + id + " not found");
            });
    }

    // update specific listing
    @PutMapping("/listings/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ListingGetDto updateListing(@PathVariable UUID id, @RequestBody ListingPostPutDto listingPostPutDto) {
        Listing listing = listingService.findListingById(id)
            .orElseThrow(() -> {
                log.debug("listing with id {} not found while updating listing", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "listing with id " + id + " not found");
            });

        Listing listingInput = ListingMapper.INSTANCE.listingPostPutDtoToListing(listingPostPutDto);
        listingInput.setId(id);

        Listing updatedListing = listingService.updateListing(listingInput);

        log.info("listing with id {} updated", id);

        return ListingMapper.INSTANCE.listingToListingGetDto(updatedListing);
    }

    // delete a specific listing
    @DeleteMapping("/listings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteListing(@PathVariable UUID id) {
        if (!listingService.existsListing(id)) {
            log.debug("listing with id {} not found while deleting listing", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "listing with id " + id + " not found");
        }

        listingService.deleteListing(id);

        log.info("listing with id {} deleted", id);
    }
}

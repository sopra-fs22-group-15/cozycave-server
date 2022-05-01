package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listing.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.*;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.ListingGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.ListingMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ListingService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
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
public class ListingController {

    private final ListingService listingService;

    ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    // Get all listings in a list
    @GetMapping("/listing")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ListingGetDto> getAllListings() {
        return listingService.getListings().stream()
                .map(ListingMapper.INSTANCE::listingToListingGetDto)
                .collect(Collectors.toList());
    }

    // Creates new listing
    @PostMapping("/listing")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ListingGetDto createListing(@RequestBody ListingPostDto listingPostDto) {
        Listing listingInput = ListingMapper.INSTANCE.listingPostDtoToListing(listingPostDto);

        // TODO: User Authentication required to create listings?
        Listing createdListing = listingService.createListing(listingInput);

        return ListingMapper.INSTANCE.listingToListingGetDto(createdListing);
    }

    // get specific listing
    @GetMapping("/listing/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ListingGetDto findListing(@PathVariable UUID id) {
        return listingService.findListingById(id)
                .map(ListingMapper.INSTANCE::listingToListingGetDto)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing couldn't be found with that listing ID."));
    }

    // update specific listing
    @PutMapping("/listing/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ListingGetDto updateListing(@PathVariable UUID id, @RequestBody ListingPutDto listingPutDto) {
        Listing listing = listingService.findListingById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing couldn't be found with that listing ID."));

        Listing listingInput = ListingMapper.INSTANCE.listingPutDtoToListing(listingPutDto);

        return ListingMapper.INSTANCE.listingToListingGetDto(listingService.updateListing(listingInput));
    }

    // delete a specific listing
    @DeleteMapping("/listing/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteListing(@PathVariable UUID id, @RequestBody ListingPutDto listingPutDto) {
        Listing listingInput = ListingMapper.INSTANCE.listingPutDtoToListing(listingPutDto);

        listingService.deleteListing(listingInput);
    }

}

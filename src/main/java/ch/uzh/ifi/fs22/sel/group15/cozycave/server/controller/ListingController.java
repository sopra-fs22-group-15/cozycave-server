package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listing.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.ListingGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.ListingPostDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.ListingPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.ListingMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ListingService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/v1")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
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
    public ListingGetDto createListing(@RequestBody ListingPostDto listingPostDto) {
        Listing listingInput = ListingMapper.INSTANCE.listingPostDtoToListing(listingPostDto);

        Location address = null;

        if (listingPostDto.getAddress() != null) {
            address = new Location(
                    listingPostDto.getName(),
                    listingPostDto.getName(),
                    listingPostDto.getAddress().getStreet(),
                    listingPostDto.getAddress().getHouseNumber(),
                    listingPostDto.getAddress().getApartmentNumber(),
                    listingPostDto.getAddress().getZipCode(),
                    listingPostDto.getAddress().getCity(),
                    listingPostDto.getAddress().getCountry()
            );
        }

        User userInput = userService.findUserID(listingPostDto.getPublisher())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher couldn't be found with that Publisher ID."));

        listingInput.setAddress(address);

        Listing createdListing = listingService.createListing(listingInput);

        return ListingMapper.INSTANCE.listingToListingGetDto(createdListing);
    }

    // get specific listing
    @GetMapping("/listings/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ListingGetDto findListing(@PathVariable UUID id) {
        return listingService.findListingById(id)
                .map(ListingMapper.INSTANCE::listingToListingGetDto)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing couldn't be found with that listing ID."));
    }

    // update specific listing
    @PutMapping("/listings/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ListingGetDto updateListing(@PathVariable UUID id, @RequestBody ListingPutDto listingPutDto) {
        Listing listing = listingService.findListingById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing couldn't be found with that listing ID."));

        Listing listingInput = ListingMapper.INSTANCE.listingPutDtoToListing(listingPutDto);
        listingInput.setId(id);

        return ListingMapper.INSTANCE.listingToListingGetDto(listingService.updateListing(listingInput));
    }

    // delete a specific listing
    @DeleteMapping("/listings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteListing(@PathVariable UUID id, @RequestBody ListingPutDto listingPutDto) {
        Listing listingInput = ListingMapper.INSTANCE.listingPutDtoToListing(listingPutDto);

        listingInput.setId(id);

        listingService.deleteListing(listingInput);
    }

}

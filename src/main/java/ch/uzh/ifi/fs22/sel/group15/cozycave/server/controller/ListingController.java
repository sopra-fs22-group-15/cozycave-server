package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications.Application;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.applications.ApplicationGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.applications.ApplicationPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.ApplicationMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.ListingMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.UserMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ApplicationService;
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
    private final ApplicationService applicationService;

    ListingController(ListingService listingService, UserService userService, ApplicationService applicationService) {
        this.listingService = listingService;
        this.userService = userService;
        this.applicationService = applicationService;
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

    // get all applications of a listing
    @GetMapping("/listings/{id}/applications")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ApplicationGetDto> findApplications(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.error("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding authenticated user");
                });

        Listing listing = listingService.findListingById(id)
                .orElseThrow(() -> {
                    log.debug("listing with id {} not found while geting listing", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "listing with id " + id + " not found");
                });
        if (authUser.getId() != listing.getPublisher().getId()) {
            log.debug("applications to listing id {} are not allowed for", authUser.getId());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "applications for id " + id + " not allowed to collect");
        }

        return applicationService.findApplicationsToListing(id).stream()
                .map(ApplicationMapper.INSTANCE::applicationToApplicationGetDto)
                .collect(Collectors.toList());
    }

    // Creates an application to a listing
    @PostMapping("/listings/{id}/applications")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ApplicationGetDto createApplication(
            @AuthenticationPrincipal String authUserId,
            @RequestBody ApplicationPostPutDto applicationPostPutDto
    ) {
        Application applicationInput = ApplicationMapper.INSTANCE.applicationPostPutDtoToApplication(applicationPostPutDto);

        User applicant = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.error("user (applicant) with id {} not found while creating application", authUserId);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN, "error finding publisher");
                });

        applicationInput.setApplicant(applicant);
        Application createdApplication = applicationService.createApplication(applicationInput);

        log.info("created application with id {}", createdApplication.getId());

        return ApplicationMapper.INSTANCE.applicationToApplicationGetDto(createdApplication);
    }

    // get specific applications of a listing
    @GetMapping("/listings/{id}/applications/{applicationID}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ApplicationGetDto findApplication(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id,
            @PathVariable UUID applicationID) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.error("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding authenticated user");
                });

        Listing listing = listingService.findListingById(id)
                .orElseThrow(() -> {
                    log.debug("listing with id {} not found while geting listing", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "listing with id " + id + " not found");
                });

        ApplicationGetDto application =  applicationService.findApplicationById(applicationID)
                .map(ApplicationMapper.INSTANCE::applicationToApplicationGetDto)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "application with id " + applicationID + " not found"));

        if (application.getListing().getId() != listing.getId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "application to listing couldn't be found");
        }
        // either publisher of listing to see application or applicant itself
        if ((authUser.getId() != application.getApplicant().getId()) &&
                (authUser.getId() != listing.getPublisher().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "not allowed to get applications to listing");
        }

        return application;
    }

    // publisher of listing changes status of an application
    @PutMapping("/listings/{id}/applications/{applicationID}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ApplicationGetDto updateApplication(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id,
            @PathVariable UUID applicationID,
            @RequestBody ApplicationPostPutDto applicationPostPutDto) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.error("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN,
                            "error finding authenticated user");
                });

        Listing listing = listingService.findListingById(id)
                .orElseThrow(() -> {
                    log.debug("listing with id {} not found while geting listing", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "listing with id " + id + " not found");
                });

        Application applicationToBeUpdated = applicationService.findApplicationById(applicationID)
                .orElseThrow(() -> {
                    log.error("application with id {} not found", applicationID);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "error finding application");
                });

        Application applicationInput = ApplicationMapper.INSTANCE.applicationPostPutDtoToApplication(applicationPostPutDto);

        return ApplicationMapper.INSTANCE.applicationToApplicationGetDto(
                applicationService.decideOnApplication(applicationInput, authUser));
    }

    // delete a specific user
    @DeleteMapping("/listings/{id}/applications/{applicationID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApplication(
            @AuthenticationPrincipal String authUserId,
            @PathVariable UUID id,
            @PathVariable UUID applicationID) {
        User authUser = userService.findUserID(UUID.fromString(authUserId))
                .orElseThrow(() -> {
                    log.error("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding authenticated user");
                });

        Application applicationToBeDeleted = applicationService.findApplicationById(applicationID)
                .orElseThrow(() -> {
                    log.error("application with id {} not found", applicationID);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding application");
                });

        if ((applicationToBeDeleted.getApplicant().getId() != authUser.getId()) &&
                (!authUser.getRole().greaterEquals(Role.ADMIN))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You're not allowed to delete the application");
        }
        applicationService.deleteApplication(id);

        log.info("listing with id {} deleted", id);
    }

}

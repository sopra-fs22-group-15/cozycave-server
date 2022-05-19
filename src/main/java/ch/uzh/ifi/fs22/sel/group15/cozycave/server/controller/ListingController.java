package ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller.ListingController.ListingFilter.FilterPair;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.controller.ListingController.ListingFilter.ListingFilters;
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
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ApplicationService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.ListingService;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;


import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
   /*@GetMapping("/listings")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ListingGetDto> getAllListings() {
        return listingService.getListings().stream()
            .map(ListingMapper.INSTANCE::listingToListingGetDto)
            .collect(Collectors.toList());
    }*/
    @GetMapping("/listings")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ListingGetDto> getAllListingsFiltered(
            @RequestParam(name = "MIN_RENT") Optional<String> minRent,
            @RequestParam(name = "MAX_RENT") Optional<String> maxRent,
            @RequestParam(name = "AVAILABLE_TO") Optional<List<String>> availableTo,
            @RequestParam(name = "LISTING_TYPE") Optional<List<String>> listingType,
            @RequestParam(name = "CITY") Optional<String> city,
            @RequestParam(name = "ZIP_CODE") Optional<String> zipCode,
            @RequestParam(name = "MIN_SQM") Optional<String> minSqm,
            @RequestParam(name = "MAX_SQM") Optional<String> maxSqm,
            @RequestParam(name = "AVAILABLE") Optional<String> available,
            @RequestParam(name = "SORT") Optional<String> sort,
            @RequestParam(name = "ORDER") Optional<String> order
    ) {
        HashMap<String, Object> filtersMap = new HashMap<String, Object>();
        List<Listing> allListings = listingService.getListings();

        // check if values are filled
        if (minRent.isPresent()) {
            try {
                filtersMap.put("MIN_RENT", Integer.parseInt(minRent.get()));
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expected a number");
            }
        }
        if (maxRent.isPresent()) {
            try {
                filtersMap.put("MAX_RENT", Integer.parseInt(maxRent.get()));
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expected a number");
            }
        }
        if (availableTo.isPresent()) {
            // for conversion from String to ENUM
            List<Gender> temp = new ArrayList<>();
            for (String genderString : availableTo.get()) {
                try {
                    temp.add(Gender.valueOf(genderString.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gender for AVAILABLE_to does not exist");
                }
            }
            filtersMap.put("AVAILABLE_TO", temp);
        }
        if (listingType.isPresent()) {
            // for conversion from String to ENUM
            List<ListingType> temp = new ArrayList<>();
            for (String listingString : listingType.get()) {
                try {
                    temp.add(ListingType.valueOf(listingString.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LISTING_TYPE does not exist");
                }
            }
            filtersMap.put("LISTING_TYPE", temp);
        }
        if (city.isPresent()) filtersMap.put("CITY", city.get());
        if (zipCode.isPresent()) filtersMap.put("ZIP_CODE", zipCode.get());
        if (minSqm.isPresent()) {
            try {
                filtersMap.put("MIN_SQM", Integer.parseInt(minSqm.get()));
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expected a number");
            }
        }
        if (maxSqm.isPresent()) {
            try {
                filtersMap.put("MAX_SQM", Integer.parseInt(maxSqm.get()));
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expected a number");
            }
        }

        if (available.isPresent()) filtersMap.put("AVAILABLE", Boolean.parseBoolean(available.get()));

        if (filtersMap.size() > 0) {
            List<FilterPair> filterPairs = filtersMap.entrySet().stream()
                    .map(e -> new FilterPair(ListingFilters.getFilter(e.getKey()), e.getValue())).toList();

            allListings = ListingFilter.createFilter(allListings).filter(filterPairs).getListings();
        }

        if (sort.isPresent()) {

            Sorting sorting;
            try {
                sorting = Sorting.valueOf(sort.get().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sorting not supported");
            }

            OrderType orderType = OrderType.ASC;

            if (order.isPresent()) {
                try {
                    orderType = OrderType.valueOf(order.get().toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OrderType not found, only ASC and DESC supported");
                }
            }

            // set default ordering to ASC or use userinput, only works for rent at the moment
            if (orderType.equals(OrderType.ASC)) {
                if (sorting.equals(Sorting.RENT)) {
                    allListings.sort(Comparator.comparing(Listing::getRent));
                } else if (sorting.equals(Sorting.SQM)) {
                    allListings.sort(Comparator.comparing(Listing::getSqm));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operation not valid");
                }
            }

            if (orderType.equals(OrderType.DESC)) {
                if (sorting.equals(Sorting.RENT)) {
                    allListings.sort(Comparator.comparing(Listing::getRent, (listing1, listing2) -> {
                        return listing2.compareTo(listing1);
                    }));
                    Collections.sort(allListings, Comparator.comparing(Listing::getRent, (listing1, listing2) -> {
                        return listing2.compareTo(listing1);
                    }));
                } else if (sorting.equals(Sorting.SQM)) {
                    allListings.sort(Comparator.comparing(Listing::getSqm, (listing1, listing2) -> {
                        return listing2.compareTo(listing1);
                    }));
                    Collections.sort(allListings, Comparator.comparing(Listing::getSqm, (listing1, listing2) -> {
                        return listing2.compareTo(listing1);
                    }));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operation not valid");
                }
            }
        }

        return allListings.stream()
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
                    log.debug("user (authenticated user) with id {} not found while getting application", authUserId);
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
                    log.debug("user (applicant) with id {} not found while creating application", authUserId);
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
                    log.debug("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding authenticated user");
                });

        Listing listing = listingService.findListingById(id)
                .orElseThrow(() -> {
                    log.debug("listing with id {} not found while geting listing", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "listing with id " + id + " not found");
                });

        ApplicationGetDto application = applicationService.findApplicationById(applicationID)
                .map(ApplicationMapper.INSTANCE::applicationToApplicationGetDto)
                .orElseThrow(() -> {
                    log.debug("application with id {} couldn't be found", applicationID);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "application with id " + applicationID + " not found");
                });

        if (application.getListing().getId() != listing.getId()) {
            log.debug("application listing id {} is not the same as the id of the listing id {}", application.getListing().getId(), listing.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "application to listing couldn't be found");
        }
        // either publisher of listing to see application or applicant itself
        if ((authUser.getId() != application.getApplicant().getId()) &&
                (authUser.getId() != listing.getPublisher().getId())) {
            log.debug("not applicant of application with id {} nor an admin", application.getApplicant().getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "not allowed to get applications to listing");
        }

        log.info("found application with id {}", application.getId());

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
                    log.debug("user (authenticated user) with id {} not found while getting application", authUserId);
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
                    log.debug("application with id {} not found", applicationID);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "error finding application");
                });

        Application applicationInput = ApplicationMapper.INSTANCE.applicationPostPutDtoToApplication(applicationPostPutDto);

        applicationInput = applicationService.decideOnApplication(applicationInput, authUser);

        log.info("application with id {} updated", applicationInput.getId());

        return ApplicationMapper.INSTANCE.applicationToApplicationGetDto(applicationInput);
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
                    log.debug("user (authenticated user) with id {} not found while getting application", authUserId);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding authenticated user");
                });

        Application applicationToBeDeleted = applicationService.findApplicationById(applicationID)
                .orElseThrow(() -> {
                    log.debug("application with id {} not found", applicationID);
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "error finding application");
                });

        if ((applicationToBeDeleted.getApplicant().getId() != authUser.getId()) &&
                (!authUser.getRole().greaterEquals(Role.ADMIN))) {
            log.debug("not applicant of application with id {} nor an admin", applicationToBeDeleted.getApplicant().getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You're not allowed to delete the application");
        }
        applicationService.deleteApplication(id);

        log.info("listing with id {} deleted", id);
    }

    public enum OrderType {
        ASC,
        DESC;
    }

    public enum Sorting {
        RENT,
        SQM;
    }

    // filter listings

    public static class ListingFilter {

        private List<Listing> listings;

        private ListingFilter(List<Listing> listings) {
            this.listings = listings;
        }

        public static ListingFilter createFilter(List<Listing> listings) {
            return new ListingFilter(listings);
        }

        public List<Listing> getListings() {
            return listings;
        }

        public ListingFilter filter(List<FilterPair> filters) {
            for (FilterPair f : filters) {
                switch (f.getFilter()) {
                    case MIN_RENT -> filterByMinRent(f.getIntegerValue());
                    case MAX_RENT -> filterByMaxRent(f.getIntegerValue());
                    case AVAILABLE_TO -> filterByGender(f.getAvailableToValue());
                    case LISTING_TYPE -> filterByListingType(f.getListingTypeValue());
                    case CITY -> filterByCity(f.getStringValue());
                    case ZIP_CODE -> filterByZipCode(f.getStringValue());
                    case MIN_SQM -> filterByMinSqm(f.getIntegerValue());
                    case MAX_SQM -> filterByMaxSqm(f.getIntegerValue());
                    case AVAILABLE -> filterByAvailable(f.getBooleanValue());
                }
            }

            return this;
        }

        public ListingFilter filterByMinRent(int minRent) {
            this.listings = listings.stream()
                    .filter(listing -> listing.getRent() >= minRent)
                    .collect(Collectors.toList());
            return this;
        }

        public ListingFilter filterByMaxRent(int maxRent) {
            this.listings = listings.stream()
                    .filter(listing -> listing.getRent() <= maxRent)
                    .collect(Collectors.toList());
            return this;
        }

        public ListingFilter filterByGender(List<Gender> availableTo) {
            this.listings = listings.stream()
                    .filter(listing -> listing.getAvailableTo().containsAll(availableTo))
                    .collect(Collectors.toList());
            return this;
        }

        public ListingFilter filterByListingType(List<ListingType> listingType) {
            this.listings = listings.stream()
                    .filter(listing -> listingType.contains(listing.getListingType()))
                    .collect(Collectors.toList());
            return this;
        }

        public ListingFilter filterByCity(String city) {
            this.listings = listings.stream()
                    .filter(listing -> Objects.equals(listing.getAddress().getCity(), city))
                    .collect(Collectors.toList());
            return this;
        }

        public ListingFilter filterByZipCode(String zipCode) {
            this.listings = listings.stream()
                    .filter(listing -> Objects.equals(listing.getAddress().getZipCode(), zipCode))
                    .collect(Collectors.toList());
            return this;
        }

        public ListingFilter filterByMinSqm(int minSqm) {
            this.listings = listings.stream()
                    .filter(listing -> listing.getSqm() >= minSqm)
                    .collect(Collectors.toList());
            return this;
        }

        public ListingFilter filterByMaxSqm(int maxSqm) {
            this.listings = listings.stream()
                    .filter(listing -> listing.getSqm() <= maxSqm)
                    .collect(Collectors.toList());
            return this;
        }

        public ListingFilter filterByAvailable(boolean available) {
            this.listings = listings.stream()
                    .filter(listing -> Objects.equals(listing.getPublished(), available))
                    .collect(Collectors.toList());
            return this;
        }


        public enum ListingFilters {

            MIN_RENT(Integer.class),
            MAX_RENT(Integer.class),
            AVAILABLE_TO(ArrayList.class),
            LISTING_TYPE(ArrayList.class),
            CITY(String.class),
            ZIP_CODE(String.class),
            MIN_SQM(Integer.class),
            MAX_SQM(Integer.class),
            AVAILABLE(Boolean.class);

            private Class<?> type;

            ListingFilters(Class<?> type) {
                this.type = type;
            }

            public static ListingFilters getFilter(String filter) {
                return ListingFilters.valueOf(filter.toUpperCase());
            }

            public Class<?> getType() {
                return type;
            }
        }

        public static class FilterPair {

            private ListingFilters filter;
            private Object value;

            public FilterPair(ListingFilters filter, Object value) {
                this.filter = filter;

                if (filter.getType() != value.getClass()) {
                    throw new IllegalArgumentException("value is not of type " + filter.getType());
                }

                this.value = value;
            }

            public ListingFilters getFilter() {
                return filter;
            }

            public Object getValue() {
                return value;
            }

            public Integer getIntegerValue() {
                return (Integer) value;
            }

            public String getStringValue() {
                return (String) value;
            }

            public Boolean getBooleanValue() {
                return (Boolean) value;
            }

            public Double getDoubleValue() {
                return (Double) value;
            }

            public Long getLongValue() {
                return (Long) value;
            }

            public Float getFloatValue() {
                return (Float) value;
            }

            public Character getCharacterValue() {
                return (Character) value;
            }

            public Byte getByteValue() {
                return (Byte) value;
            }

            public Short getShortValue() {
                return (Short) value;
            }

            @SuppressWarnings("unchecked")
            public List<Gender> getAvailableToValue() {
                return (List<Gender>) value;
            }

            @SuppressWarnings("unchecked")
            public List<ListingType> getListingTypeValue() {
                return (List<ListingType>) value;
            }
        }
    }

}

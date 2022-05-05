package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listing.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.repository.UserRepository;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.ListingGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.ListingPostDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.ListingPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.service.UserService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

// TODO: add @Mapping annotation
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ListingMapper {

    // GET DTO MAPPER
    ListingMapper INSTANCE = Mappers.getMapper(ListingMapper.class);

    Listing listingGetDtoToListing(ListingGetDto listingGetDto);

    ListingGetDto listingToListingGetDto(Listing listing);


    // POST DTO MAPPER
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateListingFromListingGetDto(ListingGetDto listingGetDto, @MappingTarget Listing listing);

    @Mapping(source = "publisher", target = "publisher", qualifiedByName = "uuidToUser")
    Listing listingPostDtoToListing(ListingPostDto listingPostDto);

    @Mapping(source = "publisher", target = "publisher", qualifiedByName = "userToUUID")
    ListingPostDto listingPostDto(Listing listing);

    // PUT DTO MAPPER
    @Mapping(source = "publisher", target = "publisher", qualifiedByName = "uuidToUser")
    Listing listingPutDtoToListing(ListingPutDto listingPutDto);

    @Mapping(source = "publisher", target = "publisher", qualifiedByName = "userToUUID")
    ListingPutDto listingToListingPutDto(Listing listing);

    /*@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateListingFromListingPutDto(
            ListingPutDto listingPutDto, @MappingTarget Listing listing);*/

    @Named("uuidToUser")
    public static User uuidToUser(UUID uuid) {
        return new User(uuid, null, null, null, null);
    }

    @Named("userToUUID")
    public static UUID userToUUID(User user) {
        return user.getId();
    }


}

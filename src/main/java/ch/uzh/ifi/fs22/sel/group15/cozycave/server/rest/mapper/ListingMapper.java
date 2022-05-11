package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingPostPutDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface ListingMapper {

    ListingMapper INSTANCE = Mappers.getMapper(ListingMapper.class);

    Listing listingGetDtoToListing(ListingGetDto listingGetDto);

    ListingGetDto listingToListingGetDto(Listing listing);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateListingFromListingGetDto(ListingGetDto listingGetDto, @MappingTarget Listing listing);

    Listing listingPostPutDtoToListing(ListingPostPutDto listingPostPutDto);

    ListingPostPutDto listingToListingPostPutDto(Listing listing);
}

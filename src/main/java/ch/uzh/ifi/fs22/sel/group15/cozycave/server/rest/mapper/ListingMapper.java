package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listing.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.ListingGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.ListingPostDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.ListingPutDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

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

    //Listing listingPostDtoToListing(ListingPostDto listingPostDto);

    //ListingPostDto listingPostDto(Listing listing);

    // PUT DTO MAPPER
    Listing listingPutDtoToListing(ListingPutDto listingPutDto);

    ListingPutDto listingToListingPutDto(Listing listing);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateListingFromListingPutDto(
            ListingPutDto listingPutDto, @MappingTarget Listing listing);


}

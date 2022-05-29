package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest;


import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.UserDetails;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PictureGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetPublicDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.ListingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOListingMapper {

    private UserGetDto applicantGetDto;
    private UserGetPublicDto publisherGetPublicDto;
    private ListingGetDto listingGetDto;
    private Listing listing;
    private ListingPostPutDto listingPostPutDto;
    private List<PictureGetDto> picturesGetDto;
    private List<PictureGetDto> floorplanGetDto;
    private List<Picture> pictures;
    private List<Picture> floorplan;
    private PictureGetDto pictureGetDto;


    @BeforeEach
    public void setup() {

        picturesGetDto = new ArrayList<>();
        floorplanGetDto = new ArrayList<>();

        listingGetDto = new ListingGetDto(
                UUID.randomUUID(),
                new Date(),
                "Test WG",
                "Test WG Description",
                null,
                true,
                150.0,
                ListingType.HOUSE,
                true,
                null,
                true,
                1600.0,
                3000.0,
                4.5,
                null,
                picturesGetDto,
                floorplanGetDto
        );

        listingPostPutDto = new ListingPostPutDto(
                UUID.randomUUID(),
                "Test WG",
                "Test WG Description",
                null,
                true,
                150.0,
                ListingType.HOUSE,
                true,
                null,
                true,
                1600.0,
                3000.0,
                4.5,
                null,
                null
        );

        pictureGetDto = new PictureGetDto(
                UUID.randomUUID(),
                new Date(),
                "http://database.cozycave.ch/CozyCave/595e1d18-dd6e-4a8a-ac08-7ddff4d9c06d.jpg"
        );

        publisherGetPublicDto = new UserGetPublicDto(
                UUID.randomUUID(),
                new UserGetPublicDto.UserDetailsDto(
                        "Test",
                        "uzh",
                        "Biography",
                        pictureGetDto
                )
        );

        pictures = new ArrayList<>();
        floorplan = new ArrayList<>();

        listing = new Listing(
                UUID.randomUUID(),
                new Date(),
                "Test WG",
                "Test WG Description",
                null,
                true,
                150.0,
                ListingType.HOUSE,
                true,
                null,
                true,
                1600.0,
                3000.0,
                4.5,
                null,
                pictures,
                floorplan
        );
    }

    @Test
    public void listingGetDto_To_Listing() {

        Listing listing = ListingMapper.INSTANCE.listingGetDtoToListing(listingGetDto);

        assertEquals(listing.getId(),listingGetDto.getId());
        assertEquals(listing.getCreationDate(),listingGetDto.getCreationDate());
        assertEquals(listing.getTitle(),listingGetDto.getTitle());
        assertEquals(listing.getDescription(),listingGetDto.getDescription());
        assertEquals(listing.getPublished(),listingGetDto.getPublished());
        assertEquals(listing.getSqm(),listingGetDto.getSqm());
        assertEquals(listing.getListingType(),listingGetDto.getListingType());
        assertEquals(listing.getFurnished(),listingGetDto.getFurnished());
        assertEquals(listing.getAvailable(),listingGetDto.getAvailable());
        assertEquals(listing.getRent(),listingGetDto.getRent());
        assertEquals(listing.getDeposit(),listingGetDto.getDeposit());
        assertEquals(listing.getRooms(),listingGetDto.getRooms());

    }

    @Test
    public void listing_To_ListingGetDto() {

        ListingGetDto listingGetDto = ListingMapper.INSTANCE.listingToListingGetDto(listing);

        assertEquals(listing.getId(),listingGetDto.getId());
        assertEquals(listing.getCreationDate(),listingGetDto.getCreationDate());
        assertEquals(listing.getTitle(),listingGetDto.getTitle());
        assertEquals(listing.getDescription(),listingGetDto.getDescription());
        assertEquals(listing.getPublished(),listingGetDto.getPublished());
        assertEquals(listing.getSqm(),listingGetDto.getSqm());
        assertEquals(listing.getListingType(),listingGetDto.getListingType());
        assertEquals(listing.getFurnished(),listingGetDto.getFurnished());
        assertEquals(listing.getAvailable(),listingGetDto.getAvailable());
        assertEquals(listing.getRent(),listingGetDto.getRent());
        assertEquals(listing.getDeposit(),listingGetDto.getDeposit());
        assertEquals(listing.getRooms(),listingGetDto.getRooms());

    }

    @Test
    public void listingPostPutDto_To_Listing() {

        Listing listing = ListingMapper.INSTANCE.listingPostPutDtoToListing(listingPostPutDto);

        assertEquals(listing.getId(),listingPostPutDto.getId());
        assertEquals(listing.getTitle(),listingPostPutDto.getTitle());
        assertEquals(listing.getDescription(),listingPostPutDto.getDescription());
        assertEquals(listing.getPublished(),listingPostPutDto.getPublished());
        assertEquals(listing.getSqm(),listingPostPutDto.getSqm());
        assertEquals(listing.getListingType(),listingPostPutDto.getListingType());
        assertEquals(listing.getFurnished(),listingPostPutDto.getFurnished());
        assertEquals(listing.getAvailable(),listingPostPutDto.getAvailable());
        assertEquals(listing.getRent(),listingPostPutDto.getRent());
        assertEquals(listing.getDeposit(),listingPostPutDto.getDeposit());
        assertEquals(listing.getRooms(),listingPostPutDto.getRooms());

    }

    @Test
    public void listing_To_ListingPostPutDto() {

        ListingPostPutDto listingPostPutDto = ListingMapper.INSTANCE.listingToListingPostPutDto(listing);

        assertEquals(listing.getId(),listingPostPutDto.getId());
        assertEquals(listing.getTitle(),listingPostPutDto.getTitle());
        assertEquals(listing.getDescription(),listingPostPutDto.getDescription());
        assertEquals(listing.getPublished(),listingPostPutDto.getPublished());
        assertEquals(listing.getSqm(),listingPostPutDto.getSqm());
        assertEquals(listing.getListingType(),listingPostPutDto.getListingType());
        assertEquals(listing.getFurnished(),listingPostPutDto.getFurnished());
        assertEquals(listing.getAvailable(),listingPostPutDto.getAvailable());
        assertEquals(listing.getRent(),listingPostPutDto.getRent());
        assertEquals(listing.getDeposit(),listingPostPutDto.getDeposit());
        assertEquals(listing.getRooms(),listingPostPutDto.getRooms());

    }


}



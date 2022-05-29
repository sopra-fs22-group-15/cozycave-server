package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest;


import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Gender;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.ListingType;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.constant.Role;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Location;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.AuthenticationData;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.listings.Listing;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.UserDetails;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.LocationDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PictureGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetPublicDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.ListingMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.LocationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOLocationMapper {

    private LocationDto locationDto;
    private Location location;


    @BeforeEach
    public void setup() {
        locationDto = new LocationDto(
                UUID.randomUUID(),
                "Privat Address",
                "Privat Address Description",
                "Hauptstrasse",
                "10",
                "306",
                "8005",
                "Zürich",
                "Zürich",
                "Switzerland"
        );

        location = new Location(
                UUID.randomUUID(),
                "Privat Address",
                "Privat Address Description",
                "Hauptstrasse",
                "10",
                "306",
                "8005",
                "Zürich",
                "Zürich",
                "Switzerland"
        );

    }

    @Test
    public void locationDto_To_Location() {

        Location location = LocationMapper.INSTANCE.locationDtoToLocation(locationDto);

        assertEquals(location.getId(), locationDto.getId());
        assertEquals(location.getName(), locationDto.getName());
        assertEquals(location.getDescription(), locationDto.getDescription());
        assertEquals(location.getStreet(), locationDto.getStreet());
        assertEquals(location.getHouseNumber(), locationDto.getHouseNumber());
        assertEquals(location.getApartmentNumber(), locationDto.getApartmentNumber());
        assertEquals(location.getZipCode(), locationDto.getZipCode());
        assertEquals(location.getCity(), locationDto.getCity());
        assertEquals(location.getState(), locationDto.getState());
        assertEquals(location.getCountry(), locationDto.getCountry());

    }
    @Test
    public void location_To_LocationDto() {

        LocationDto locationDto = LocationMapper.INSTANCE.locationToLocationDto(location);

        assertEquals(location.getId(), locationDto.getId());
        assertEquals(location.getName(), locationDto.getName());
        assertEquals(location.getDescription(), locationDto.getDescription());
        assertEquals(location.getStreet(), locationDto.getStreet());
        assertEquals(location.getHouseNumber(), locationDto.getHouseNumber());
        assertEquals(location.getApartmentNumber(), locationDto.getApartmentNumber());
        assertEquals(location.getZipCode(), locationDto.getZipCode());
        assertEquals(location.getCity(), locationDto.getCity());
        assertEquals(location.getState(), locationDto.getState());
        assertEquals(location.getCountry(), locationDto.getCountry());

    }

}



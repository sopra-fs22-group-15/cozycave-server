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
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PicturePostDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.listings.ListingPostPutDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.UserGetPublicDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.ListingMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.LocationMapper;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper.PictureMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOPictureMapper {

    private PictureGetDto pictureGetDto;
    private Picture picture;
    private PicturePostDto picturePostDto;

    @BeforeEach
    public void setup() {
        pictureGetDto = new PictureGetDto(
                UUID.randomUUID(),
                new Date(),
                "http://database.cozycave.ch/CozyCave/595e1d18-dd6e-4a8a-ac08-7ddff4d9c06d.jpg"
        );

        picture = new Picture(
                UUID.randomUUID(),
                new Date(),
                null,
                "http://database.cozycave.ch/CozyCave/595e1d18-dd6e-4a8a-ac08-7ddff4d9c06d.jpg"
        );

        picturePostDto = new PicturePostDto(
                UUID.randomUUID(),
                new Date(),
                "http://database.cozycave.ch/CozyCave/595e1d18-dd6e-4a8a-ac08-7ddff4d9c06d.jpg"
        );

    }

    @Test
    public void pictureGetDto_To_Picture() {

        Picture picture = PictureMapper.INSTANCE.pictureGetDtoToPicture(pictureGetDto);

        assertEquals(picture.getId(), pictureGetDto.getId());
        assertEquals(picture.getCreationDate(), pictureGetDto.getCreationDate());
        assertEquals(picture.getPictureUrl(), pictureGetDto.getPictureUrl());

    }

    @Test
    public void picture_To_PictureGetDto() {

        PictureGetDto pictureGetDto = PictureMapper.INSTANCE.pictureToPictureGetDto(picture);

        assertEquals(picture.getId(), pictureGetDto.getId());
        assertEquals(picture.getCreationDate(), pictureGetDto.getCreationDate());
        assertEquals(picture.getPictureUrl(), pictureGetDto.getPictureUrl());

    }

    @Test
    public void picturePostDto_To_Picture() {

        Picture picture = PictureMapper.INSTANCE.picturePostDtoToPicture(picturePostDto);

        assertEquals(picture.getId(), picturePostDto.getId());
        assertEquals(picture.getCreationDate(), picturePostDto.getCreationDate());
        assertEquals(picture.getPictureUrl(), picturePostDto.getPictureUrl());

    }

    @Test
    public void picture_To_PicturePostDto() {

        PicturePostDto picturePostDto = PictureMapper.INSTANCE.pictureToPicturePostDto(picture);

        assertEquals(picture.getId(), picturePostDto.getId());
        assertEquals(picture.getCreationDate(), picturePostDto.getCreationDate());
        assertEquals(picture.getPictureUrl(), picturePostDto.getPictureUrl());

    }

}



package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.Picture;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PictureGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.PicturePostDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface PictureMapper {

    PictureMapper INSTANCE = Mappers.getMapper(PictureMapper.class);

    Picture pictureGetDtoToPicture(PictureGetDto pictureGetDto);

    PictureGetDto pictureToPictureGetDto(Picture picture);

    Picture picturePostDtoToPicture(PicturePostDto picturePostDto);

    PicturePostDto pictureToPicturePostDto(Picture picture);
}

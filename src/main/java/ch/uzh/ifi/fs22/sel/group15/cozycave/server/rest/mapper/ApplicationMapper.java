package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.applications.Application;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.applications.ApplicationGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.applications.ApplicationPostPutDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface ApplicationMapper {

    ApplicationMapper INSTANCE = Mappers.getMapper(ApplicationMapper.class);

    Application applicationGetDtoToApplication(ApplicationGetDto applicationGetDto);

    ApplicationGetDto applicationToApplicationGetDto(Application application);

    Application applicationPostPutDtoToApplication(ApplicationPostPutDto applicationPostPutDto);

    ApplicationPostPutDto applicationToApplicationPostPutDto(Application application);
}

package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserGetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {
    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    UserGetDTO convertEntityToUserGetDTO(User user);
}

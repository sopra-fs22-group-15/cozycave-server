package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.*;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {
    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "creation", target = "creation")
    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "details", target = "details")
    UserGetDTO convertEntityToUserGetDTO(User user);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "creation", target = "creation")
    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "details", target = "details")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);
    UserPutDTO convertEntityToUserPutDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "creation", target = "creation")
    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "details", target = "details")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);
    UserPostDTO convertEntityToUserPostDTO(User user);
}

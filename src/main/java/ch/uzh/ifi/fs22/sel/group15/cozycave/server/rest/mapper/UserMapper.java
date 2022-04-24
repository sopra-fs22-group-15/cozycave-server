package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.user.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserGetDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserPostDto;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.UserPutDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

// TODO: add @Mapping annotation
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userGetDtoToUser(UserGetDto userGetDto);

    /*@Mapping(source = "id", target = "id")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "authenticationData", target = "authenticationData")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "details", target = "details")*/
    UserGetDto userToUserGetDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserGetDto(UserGetDto userGetDto, @MappingTarget User user);

    User userPostDtoToUser(UserPostDto userPostDto);

    UserPostDto userToUserPostDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserPostDto(
        UserPostDto userPostDto, @MappingTarget User user);

    User userPutDtoToUser(UserPutDto userPutDto);

    UserPutDto userToUserPutDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserPutDto(
        UserPutDto userPutDto, @MappingTarget User user);
}

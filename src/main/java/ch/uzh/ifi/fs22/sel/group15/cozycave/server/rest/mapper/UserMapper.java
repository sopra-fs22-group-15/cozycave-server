package ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.mapper;

import ch.uzh.ifi.fs22.sel.group15.cozycave.server.entity.users.User;
import ch.uzh.ifi.fs22.sel.group15.cozycave.server.rest.dto.users.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userGetDtoToUser(UserGetDto userGetDto);

    UserGetDto userToUserGetDto(User user);

    User userGetPublicDtoToUser(UserGetPublicDto userGetPublicDto);

    UserGetPublicDto userToUserGetPublicDto(User user);

    User userPostPutDtoToUser(UserPostPutDto userPostPutDto);

    UserPostPutDto userToUserPostPutDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserPostPutDto(UserPostPutDto userPostPutDto, @MappingTarget User user);

    @Mapping(source = "details.birthday", target = "details.age", qualifiedByName = "ageConversion")
    UserGetGTDto userToUserGetGTDto(User user);

    @Mapping(source = "details.birthday", target = "details.age", qualifiedByName = "ageConversion")
    UserGetGTPublicDto userToUserGetGTPublicDto(User user);

    @Named("ageConversion")
    public static Long ageConversion(Date birthday) {

        if (birthday != null) {
            LocalDate birthdate = Instant.ofEpochMilli(birthday.getTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
            LocalDate now = LocalDate.now();
            Long age =  ChronoUnit.YEARS.between(birthdate, now);
            return age;
        }
        return null;
    }

}

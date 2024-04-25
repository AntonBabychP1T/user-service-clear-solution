package solutionclear.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import solutionclear.userservice.config.MapperConfig;
import solutionclear.userservice.dto.UpdateUserDto;
import solutionclear.userservice.dto.UserCreateRequestDto;
import solutionclear.userservice.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toUserModel(UserCreateRequestDto requestDto);

    void updateUserFields(UpdateUserDto updateUserDto, @MappingTarget User user);

    void updateAllUserFields(UserCreateRequestDto requestDto, @MappingTarget User user);
}

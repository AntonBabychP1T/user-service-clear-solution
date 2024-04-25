package solutionclear.userservice.service;

import solutionclear.userservice.dto.UpdateUserDto;
import solutionclear.userservice.dto.UserCreateRequestDto;
import solutionclear.userservice.model.User;
import java.util.List;

public interface UserService {
    User createUser(UserCreateRequestDto requestDto);

    User updateUserFields(Long id, UpdateUserDto updateUserDto);

    User updateAllUserFields(Long id, UserCreateRequestDto requestDtoUserDto);

    void deleteUser(Long id);

    User findUserById(Long id);

    List<User> findUsersByBirthDateRange(String from, String to);
}

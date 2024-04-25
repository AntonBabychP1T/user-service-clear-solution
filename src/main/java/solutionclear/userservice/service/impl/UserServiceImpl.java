package solutionclear.userservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import solutionclear.userservice.dto.UpdateUserDto;
import solutionclear.userservice.dto.UserCreateRequestDto;
import solutionclear.userservice.exception.NotValidAgeException;
import solutionclear.userservice.exception.RegistrationException;
import solutionclear.userservice.mapper.UserMapper;
import solutionclear.userservice.model.User;
import solutionclear.userservice.repository.UserRepository;
import solutionclear.userservice.service.UserService;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${min.age}")
    private int minAge;

    @Override
    public User createUser(UserCreateRequestDto requestDto) {
        checkAge(requestDto.birthDate());
        if (userRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new RegistrationException("This email already registered");
        }
        return userRepository.save(userMapper.toUserModel(requestDto));
    }

    @Override
    public User updateUserFields(Long id, UpdateUserDto updateUserDto) {
        User user = getUserById(id);
        userMapper.updateUserFields(updateUserDto, user);
        return userRepository.save(user);
    }

    @Override
    public User updateAllUserFields(Long id, UserCreateRequestDto requestDtoUserDto) {
        User user = getUserById(id);
        userMapper.updateAllUserFields(requestDtoUserDto, user);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findUserById(Long id) {
        return getUserById(id);
    }

    @Override
    public List<User> findUsersByBirthDateRange(String from, String to) {
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("The 'from' date must be less than or equal to the 'to' date.");
        }
        return userRepository.findAllByBirthDateBetween(startDate, endDate);
    }

    private void checkAge(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        LocalDate validDate = today.minusYears(minAge);

        if (birthDate.isAfter(validDate)) {
            throw new NotValidAgeException("User is younger than the required minimum age of " + minAge);
        }
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found")
        );
    }
}

package solutionclear.userservice.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import solutionclear.userservice.dto.UpdateUserDto;
import solutionclear.userservice.dto.UserCreateRequestDto;
import solutionclear.userservice.exception.RegistrationException;
import solutionclear.userservice.mapper.UserMapper;
import solutionclear.userservice.model.User;
import solutionclear.userservice.repository.UserRepository;
import solutionclear.userservice.service.impl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    private static final int minAge = 18;
    private static final String VALID_FIRST_NAME = "John";
    private static final String VALID_LAST_NAME = "Doe";
    private static final LocalDate VALID_BIRTH_DATE = LocalDate.of(2003, 9, 13);
    private static final LocalDate INVALID_BIRTH_DATE = LocalDate.now().minusYears(minAge - 1);
    public static final String VALID_EMAIL = "john@doe.com";
    public static final String VALID_ADDRESS = "Kyiv 123";
    public static final String VALID_PHONE = "123456789";
    public static final Long VALID_ID = 1L;
    public static final Long INVALID_ID = -1L;

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @Test
    @DisplayName("Verify createUser() method works")
    public void createUser_ValidRequest_Success() {
        // Arrange
        UserCreateRequestDto requestDto = createValidRequestDto();
        User expected = createUser();
        Mockito.when(userRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());
        Mockito.when(userMapper.toUserModel(requestDto)).thenReturn(expected);
        Mockito.when(userRepository.save(expected)).thenReturn(expected);

        // Act
        User actual = userService.createUser(requestDto);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Verify createUser() throw RegistrationException")
    public void createUser_EmailAlreadyExists_ThrowsException() {
        // Arrange
        UserCreateRequestDto requestDto = createValidRequestDto();
        Mockito.when(userRepository.findByEmail(requestDto.email())).thenReturn(Optional.of(createUser()));

        // Act & Assert
        assertThrows(RegistrationException.class, () -> userService.createUser(requestDto));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Verify updateUserFields() updates fields correctly")
    public void updateUserFields_ValidRequest_Success() {
        // Arrange
        Long userId = VALID_ID;
        UpdateUserDto updateUserDto = updateUserDto();
        User existingUser = createUser();
        User updatedUser = createUser();
        updatedUser.setFirstName(updateUserDto.getFirstName());
        updatedUser.setLastName(updateUserDto.getLastName());

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(updatedUser);

        // Act
        User result = userService.updateUserFields(userId, updateUserDto);

        // Assert
        assertNotNull(result);
        assertEquals(updatedUser.getFirstName(), result.getFirstName());
        assertEquals(updatedUser.getLastName(), result.getLastName());
        Mockito.verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Verify updateAllUserFields() updates all fields correctly")
    public void updateAllUserFields_ValidRequest_Success() {
        // Arrange
        Long userId = VALID_ID;
        UserCreateRequestDto requestDto = createValidRequestDto();
        User existingUser = createUser();
        User updatedUser = createUser();
        updatedUser.setFirstName(requestDto.firstName());
        updatedUser.setLastName(requestDto.lastName());

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(updatedUser);

        // Act
        User result = userService.updateAllUserFields(userId, requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(updatedUser.getFirstName(), result.getFirstName());
        assertEquals(updatedUser.getLastName(), result.getLastName());
        Mockito.verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Verify deleteUser() calls repository delete method")
    public void deleteUser_ValidId_NoContent() {
        // Arrange
        Long userId = VALID_ID;

        Mockito.doNothing().when(userRepository).deleteById(userId);

        // Act
        userService.deleteUser(userId);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Verify findUserById() retrieves a user successfully")
    public void findUserById_ValidId_Success() {
        // Arrange
        Long userId = VALID_ID;
        User expectedUser = createUser();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        User actual = userService.findUserById(userId);

        // Assert
        assertNotNull(actual);
        assertEquals(expectedUser, actual);
    }

    @Test
    @DisplayName("Verify findUserById() retrieves a user successfully")
    public void findUserById_NotValidId_EntityNotFoundException() {
        // Arrange
        Long userId = INVALID_ID;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findUserById(userId));
    }

    @Test
    @DisplayName("Verify findUsersByBirthDateRange() with valid range returns users")
    public void testFindUsersByBirthDateRange_ValidRange_Success() {
        // Arrange
        String from = "2000-01-01";
        String to = "2001-01-01";
        List<User> expected = List.of(createUser());

        Mockito.when(userRepository.findAllByBirthDateBetween(LocalDate.parse(from), LocalDate.parse(to)))
                .thenReturn(expected);

        // Act
        List<User> result = userService.findUsersByBirthDateRange(from, to);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Verify findUsersByBirthDateRange() with invalid range throws exception")
    public void testFindUsersByBirthDateRange_InvalidRange_ThrowsException() {
        // Arrange
        String from = "2001-01-01";
        String to = "2000-01-01";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.findUsersByBirthDateRange(from, to));
    }

    private UserCreateRequestDto createValidRequestDto() {
        return new UserCreateRequestDto(
                VALID_FIRST_NAME,
                VALID_LAST_NAME,
                VALID_BIRTH_DATE,
                VALID_EMAIL,
                VALID_ADDRESS,
                VALID_PHONE
        );
    }

    private User createUser() {
        User user = new User();
        user.setFirstName(VALID_FIRST_NAME);
        user.setLastName(VALID_LAST_NAME);
        user.setBirthDate(VALID_BIRTH_DATE);
        user.setEmail(VALID_EMAIL);
        user.setAddress(VALID_ADDRESS);
        user.setPhoneNumber(VALID_PHONE);
        user.setId(VALID_ID);
        return user;
    }

    private UpdateUserDto updateUserDto() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName(VALID_FIRST_NAME);
        updateUserDto.setLastName(VALID_LAST_NAME);
        updateUserDto.setBirthDate(VALID_BIRTH_DATE);
        updateUserDto.setEmail(VALID_EMAIL);
        updateUserDto.setAddress(VALID_ADDRESS);
        updateUserDto.setPhoneNumber(VALID_PHONE);
        return updateUserDto;
    }
}

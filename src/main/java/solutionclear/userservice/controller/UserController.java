package solutionclear.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import solutionclear.userservice.dto.UpdateUserDto;
import solutionclear.userservice.dto.UserCreateRequestDto;
import solutionclear.userservice.model.User;
import solutionclear.userservice.service.UserService;

@Tag(name = "User manager", description = "Endpoints for cars managing")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Create a new user",
            description = "Create a new user using UserCreateRequestDto")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid UserCreateRequestDto requestDto) {
        return userService.createUser(requestDto);
    }

    @Operation(summary = "Update user fields",
            description = "Update specific fields for a user")
    @PatchMapping("/{id}")
    public User updateUserFields(@PathVariable Long id,
                                 @RequestBody @Valid UpdateUserDto updateUserDto) {
        return userService.updateUserFields(id, updateUserDto);
    }

    @Operation(summary = "Update all user fields",
            description = "Completely update all fields for a user")
    @PutMapping("/{id}")
    public User updateAllUserFields(@PathVariable Long id,
                                    @RequestBody @Valid UserCreateRequestDto requestDto) {
        return userService.updateAllUserFields(id, requestDto);
    }

    @Operation(summary = "Delete user by id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @Operation(summary = "Find user by ID",
            description = "Find a user by their unique identifier")
    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @Operation(summary = "Find users by birth date range",
            description = "Find users whose birth date falls within a specified range")
    @GetMapping("/search")
    public List<User> findUsersByBirthDateRange(
            @RequestParam String from,
            @RequestParam String to) {
        return userService.findUsersByBirthDateRange(from, to);
    }
}

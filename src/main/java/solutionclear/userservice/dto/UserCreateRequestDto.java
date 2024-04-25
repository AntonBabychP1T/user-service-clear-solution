package solutionclear.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import solutionclear.userservice.validation.PastOrPresentDate;

public record UserCreateRequestDto(
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull @PastOrPresentDate LocalDate birthDate,
        @Email String email,
        String address,
        String phoneNumber
) {
}

package solutionclear.userservice.dto;

import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import solutionclear.userservice.validation.PastOrPresentDate;

@Setter
@Getter
public class UpdateUserDto {
    private String firstName;
    private String lastName;
    @PastOrPresentDate
    private LocalDate birthDate;
    @Email
    private String email;
    private String address;
    private String phoneNumber;

}

package by.javaguru.dto;

import by.javaguru.entity.Gender;
import by.javaguru.entity.Role;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserDto {
    Integer id;
    String name;
    String email;
    String phone;
    Role role;
    Gender gender;
    LocalDate birthday;
}

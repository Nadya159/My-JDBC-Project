package by.javaguru.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateUserDto {
    String name, phone, email, role, gender, birthday, password;
}

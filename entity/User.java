package by.javaguru.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class User {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private Role role;
    private Gender gender;
    private LocalDate birthday;
    private String password;
}

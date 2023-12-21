package by.javaguru.mapper;

import by.javaguru.dto.CreateUserDto;
import by.javaguru.entity.User;
import by.javaguru.entity.Gender;
import by.javaguru.entity.Role;
import by.javaguru.utils.LocalDateFormatter;

public class CreateUserMapper implements Mapper<User, CreateUserDto> {
    private static final CreateUserMapper INSTANCE = new CreateUserMapper();

    @Override
    public User mapFrom(CreateUserDto object) {
        return User.builder()
                .name(object.getName())
                .phone(object.getPhone())
                .email(object.getEmail())
                .password(object.getPassword())
                .role(Role.valueOf(object.getRole()))
                .gender(Gender.valueOf(object.getGender()))
                .birthday(LocalDateFormatter.format(object.getBirthday()))
                .build();
    }

    public static CreateUserMapper getInstance() {
        return INSTANCE;
    }

    private CreateUserMapper() {
    }
}

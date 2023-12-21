package by.javaguru.mapper;

import by.javaguru.dto.UserDto;
import by.javaguru.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper implements Mapper<UserDto, User> {
    private static final UserMapper INSTANCE = new UserMapper();

    @Override
    public UserDto mapFrom(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .role(user.getRole())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .build();
    }

    public static UserMapper getInstance() {
        return INSTANCE;
    }


}

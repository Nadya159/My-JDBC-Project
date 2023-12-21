package by.javaguru.service;

import by.javaguru.dao.UserDao;
import by.javaguru.dto.CreateUserDto;
import by.javaguru.dto.UserDto;
import by.javaguru.exception.ValidationException;
import by.javaguru.mapper.CreateUserMapper;
import by.javaguru.mapper.UserMapper;
import by.javaguru.validator.CreateUserValidator;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserService {
    private static final UserService INSTANCE = new UserService();
    private final CreateUserMapper createUserMapper = CreateUserMapper.getInstance();
    private final UserDao userDao = UserDao.getInstance();
    private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();
    private final UserMapper userMapper = UserMapper.getInstance();

    public Integer create(CreateUserDto createUserDto) {
        var validationResult = createUserValidator.isValid(createUserDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var user = createUserMapper.mapFrom(createUserDto);
        userDao.save(user);
        return user.getId();
    }

    public Optional<UserDto> login(String email, String password) {
        return userDao.findByEmailAndPassword(email, password).map(userMapper::mapFrom);
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}

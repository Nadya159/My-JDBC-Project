package by.javaguru.dao;


import by.javaguru.entity.Gender;
import by.javaguru.entity.Role;
import by.javaguru.entity.User;
import by.javaguru.utils.ConnectionManager;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserDao implements Dao<Integer, User> {
    private final static UserDao INSTANCE = new UserDao();

    private final static String SAVE_SQL = """
            INSERT INTO users (name, phone, email, role, gender, birthday, password)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
    private final static String DELETE_SQL = """
            DELETE FROM users
            WHERE id = ?
            """;
    private final static String FIND_ALL_SQL = """
            SELECT id, name, phone, email, role, gender, birthday
            FROM users
            """;
    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;
    private final static String UPDATE_SQL = """
            UPDATE users
            SET name = ?,
                phone = ?,
                email = ?,
                role = ?
                gender = ?
                birthday = ?
                password = ?
            WHERE id = ?
            """;

    private static final String GET_BY_EMAIL_AND_PASSWORD_SQL =
            "SELECT * FROM users WHERE email = ? AND password = ?";

    @Override
    public boolean update(User user) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPhone());
            statement.setString(3, user.getEmail());
            statement.setString(4, String.valueOf(user.getRole()));
            statement.setString(5, String.valueOf(user.getGender()));
            statement.setObject(6, user.getBirthday());
            statement.setObject(7, user.getPassword());
            statement.setInt(8, user.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<User> users = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next())
                users.add(buildUser(result));
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static User buildUser(ResultSet result) throws SQLException {
        return User.builder()
                .id(result.getInt("id"))
                .name(result.getString("name"))
                .phone(result.getString("phone"))
                .email(result.getString("email"))
                .role(Role.valueOf(result.getString("role")))
                .gender(Gender.valueOf(result.getString("gender")))
                .birthday(LocalDate.parse(result.getString("birthday")))
                .password(result.getString("password"))
                .build();
    }

    @Override
    public Optional<User> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, id);
            var result = statement.executeQuery();
            User user = null;
            if (result.next())
                user = buildUser(result);
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @SneakyThrows
    public User save(User user) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPhone());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, String.valueOf(user.getRole()));
            preparedStatement.setString(5, String.valueOf(user.getGender()));
            preparedStatement.setDate(6, Date.valueOf(user.getBirthday()));
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            user.setId(generatedKeys.getObject("id", Integer.class));
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_BY_EMAIL_AND_PASSWORD_SQL)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            var resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = buildEntity(resultSet);
            }
            return Optional.ofNullable(user);
        }
    }

    private User buildEntity(ResultSet resultSet) throws java.sql.SQLException {
        return User.builder()
                .id(resultSet.getObject("id", Integer.class))
                .name(resultSet.getObject("name", String.class))
                .birthday(resultSet.getObject("birthday", Date.class).toLocalDate())
                .email(resultSet.getObject("email", String.class))
                .password(resultSet.getObject("password", String.class))
                .role(Role.find(resultSet.getObject("role", String.class)).orElse(null))
                .gender(Gender.valueOf(resultSet.getObject("gender", String.class)))
                .build();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }

    private UserDao() {
    }
}



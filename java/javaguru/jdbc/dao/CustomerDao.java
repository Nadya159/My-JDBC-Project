package javaguru.jdbc.dao;

import javaguru.jdbc.entity.Customer;
import javaguru.jdbc.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDao implements Dao<Integer, Customer>{
    private final static CustomerDao INSTANCE = new CustomerDao();

    private final static String SAVE_SQL = """
            INSERT INTO customer (id, first_name, last_name, phone, email, street, house, flat)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private final static String DELETE_SQL = """
            DELETE FROM customer
            WHERE id = ?
            """;
    private final static String FIND_ALL_SQL = """
            SELECT id, first_name, last_name, phone, email, street, house, flat
            FROM customer
            """;
    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;
    private final static String UPDATE_SQL = """
            UPDATE customer
            SET first_name = ?, 
                last_name = ?, 
                phone = ?, 
                email = ?, 
                street = ?, 
                house = ?, 
                flat = ?
            WHERE id = ?
            """;
    @Override
    public boolean update(Customer customer) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getEmail());
            statement.setString(5, customer.getStreet());
            statement.setString(6, customer.getHouse());
            statement.setString(7, customer.getFlat());
            statement.setInt(8, customer.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Customer> customers = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next())
                customers.add(buildCustomer(result));
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static Customer buildCustomer(ResultSet result) throws SQLException {
        return Customer.builder()
                .id(result.getInt("id"))
                .firstName(result.getString("first_name"))
                .lastName(result.getString("last_name"))
                .phone(result.getString("phone"))
                .email(result.getString("email"))
                .street(result.getString("street"))
                .house(result.getString("house"))
                .flat(result.getString("flat"))
                .build();
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, id);
            var result = statement.executeQuery();
            Customer customer = null;
            if (result.next())
                customer = buildCustomer(result);
            return Optional.ofNullable(customer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer save(Customer customer) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL)) {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getEmail());
            statement.setString(5, customer.getStreet());
            statement.setString(6, customer.getHouse());
            statement.setString(7, customer.getFlat());
            statement.setInt(8, customer.getId());
            statement.executeUpdate();
            return customer;
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

    public static CustomerDao getInstance() {
        return INSTANCE;
    }

    private CustomerDao() {
    }
}

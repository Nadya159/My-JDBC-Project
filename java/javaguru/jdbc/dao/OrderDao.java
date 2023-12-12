package javaguru.jdbc.dao;

import javaguru.jdbc.entity.Order;
import javaguru.jdbc.entity.OrderStatus;
import javaguru.jdbc.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDao implements Dao<Integer, Order>{
    private static final OrderDao INSTANCE = new OrderDao();
    private final static String SAVE_SQL = """
            INSERT INTO orders (id, customer_id, create_date, delivery_date, address, status)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
    private final static String DELETE_SQL = """
            DELETE FROM orders
            WHERE id = ?
            """;
    private final static String FIND_ALL_SQL = """
            SELECT id, customer_id, create_date, delivery_date, address, status
            FROM orders
            """;
    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;
    private final static String UPDATE_SQL = """
            UPDATE orders
            SET customer_id = ?,
            create_date = ?,
            delivery_date = ?,
            address = ?,
            status = ?
            WHERE id = ?
            """;
    @Override
    public boolean update(Order order) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, order.getCustomerId());
            statement.setTimestamp(2, Timestamp.valueOf(order.getCreateDate()));
            statement.setTimestamp(3, Timestamp.valueOf(order.getDeliveryDate()));
            statement.setString(4, order.getAddress());
            statement.setString(5, String.valueOf(order.getStatus()));
            statement.setInt(6, order.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Order> orders = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next())
                orders.add(buildOrder(result));
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Order buildOrder(ResultSet result) throws SQLException {
        return Order.builder()
                .id(result.getInt("id"))
                .customerId(result.getInt("customer_id"))
                .createDate(result.getTimestamp("create_date").toLocalDateTime())
                .deliveryDate(result.getTimestamp("delivery_date").toLocalDateTime())
                .address(result.getString("address"))
                .status(OrderStatus.valueOf(result.getString("status")))
                .build();
    }

    public Optional<Order> findById(Integer orderId, Connection connection) {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, orderId);
            var result = statement.executeQuery();
            Order order = null;
            if (result.next())
                order = buildOrder(result);
            return Optional.ofNullable(order);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Order> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, id);
            var result = statement.executeQuery();
            Order order = null;
            if (result.next())
                order = buildOrder(result);
            return Optional.ofNullable(order);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order save(Order order) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, order.getId());
            statement.setInt(2, order.getCustomerId());
            statement.setTimestamp(3, Timestamp.valueOf(order.getCreateDate()));
            statement.setTimestamp(4, Timestamp.valueOf(order.getCreateDate()));
            statement.setString(5, order.getAddress());
            statement.setString(6, String.valueOf(order.getStatus()));
            statement.executeUpdate();
            var keys = statement.getGeneratedKeys();
            if (keys.next())
                order.setId(keys.getInt("id"));
            return order;
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

    public static OrderDao getInstance() {
        return INSTANCE;
    }

    private OrderDao() {
    }


}

package javaguru.jdbc.dao;

import javaguru.jdbc.entity.Item;
import javaguru.jdbc.entity.Order;
import javaguru.jdbc.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDao implements Dao<Order, Item> {
    private final static ItemDao INSTANCE = new ItemDao();
    private final static OrderDao orderDao = OrderDao.getInstance();
    private final static ProductDao productDao = ProductDao.getInstance();
    private final static String SAVE_SQL = """
            INSERT INTO items (order_id, product_id, quantity, price_order)
            VALUES (?, ?, ?, ?)
            """;
    private final static String DELETE_SQL = """
            DELETE FROM items
            WHERE order_id = ?
            """;
    private final static String FIND_ALL_SQL = """
            SELECT order_id, product_id, quantity, price_order
            FROM items
            """;
    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE order_id = ?
            """;
    private final static String UPDATE_SQL = """
            UPDATE items
            SET quantity = ?, price_order = ?
            WHERE order_id = ?,
            product_id = ?
            """;

    @Override
    public boolean update(Item item) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setInt(1, item.getQuantity());
            statement.setBigDecimal(2, item.getPriceOrder());
            statement.setInt(3, item.getOrder().getId());
            statement.setInt(4, item.getProduct().getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Item> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Item> items = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next())
                items.add(buildItem(result));
            return items;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Item buildItem(ResultSet result) throws SQLException {
        return Item.builder()
                .order(orderDao.findById(result.getInt("order_id"),
                        result.getStatement().getConnection()).orElse(null))
                .product(productDao.findById(result.getInt("product_id"),
                        result.getStatement().getConnection()).orElse(null))
                .quantity(result.getInt("quantity"))
                .priceOrder(result.getBigDecimal("price_order"))
                .build();
    }

    @Override
    public Optional<Item> findById(Order order) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, order.getId());
            var result = statement.executeQuery();
            Item item = null;
            if (result.next())
                item = buildItem(result);
            return Optional.ofNullable(item);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Item save(Item item) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL)) {
            statement.setInt(1, item.getOrder().getId());
            statement.setInt(2, item.getProduct().getId());
            statement.setInt(3, item.getQuantity());
            statement.setBigDecimal(4, item.getPriceOrder());
            statement.executeUpdate();
            return item;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Order order) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, order.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ItemDao getInstance() {
        return INSTANCE;
    }

    private ItemDao() {
    }
}

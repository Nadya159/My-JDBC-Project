package javaguru.jdbc.dao;

import javaguru.jdbc.entity.Product;
import javaguru.jdbc.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDao implements Dao<Integer, Product> {
    private final static ProductDao INSTANCE = new ProductDao();
    private final static String SAVE_SQL = """
            INSERT INTO product (id, name, price, balance)
            VALUES (?, ?, ?, ?)
            """;
    private final static String DELETE_SQL = """
            DELETE FROM product
            WHERE id = ?
            """;
    private final static String FIND_ALL_SQL = """
            SELECT id, name, price, balance
            FROM product
            """;
    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;
    private final static String UPDATE_SQL = """
            UPDATE product
            SET name = ?,
            price = ?, 
            balance = ?
            WHERE id = ?
            """;

    @Override
    public boolean update(Product product) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getBalance());
            statement.setInt(4, product.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Product> products = new ArrayList<>();
            var result = statement.executeQuery();
            while (result.next())
                products.add(buildProduct(result));
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Product buildProduct(ResultSet result) throws SQLException {
        return Product.builder()
                .id(result.getInt("id"))
                .name(result.getString("name"))
                .price(result.getBigDecimal("price"))
                .balance(result.getInt("balance"))
                .build();
    }
    public Optional<Product> findById(Integer productId, Connection connection) {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, productId);
            var result = statement.executeQuery();
            Product product = null;
            if (result.next())
                product = buildProduct(result);
            return Optional.ofNullable(product);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Product> findById(Integer id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, id);
            var result = statement.executeQuery();
            Product product = null;
            if (result.next())
                product = buildProduct(result);
            return Optional.ofNullable(product);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product save(Product product) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL)) {
            statement.setInt(1, product.getId());
            statement.setString(2, product.getName());
            statement.setBigDecimal(3, product.getPrice());
            statement.setInt(4, product.getBalance());
            statement.executeUpdate();
            return product;
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

    public static ProductDao getInstance() {
        return INSTANCE;
    }

    private ProductDao() {
    }
}

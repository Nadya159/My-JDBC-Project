package by.javaguru.service;

import by.javaguru.dao.ProductDao;
import by.javaguru.dto.ProductDto;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {
    private static final ProductService INSTANCE = new ProductService();
    private final ProductDao productDao = ProductDao.getInstance();

    public List<ProductDto> findAll() {
        return productDao.findAll().stream().map(product ->
                        new ProductDto(product.getId(), "%s - %s - %s".formatted(
                                product.getName(), product.getPrice(), product.getBalance())))
                .collect(Collectors.toList());
    }

    public static ProductService getInstance() {
        return INSTANCE;
    }

    private ProductService() {
    }
}

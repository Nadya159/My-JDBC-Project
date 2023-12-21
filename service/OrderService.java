package by.javaguru.service;

import by.javaguru.dao.OrderDao;
import by.javaguru.dto.OrderDto;

import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private static final OrderService INSTANCE = new OrderService();
    private final OrderDao orderDao = OrderDao.getInstance();

    public List<OrderDto> findAll() {
        return orderDao.findAll().stream().map(order ->
                        new OrderDto(order.getId(), "%s - %s - %s".formatted(
                                order.getAddress(), order.getStatus(), order.getDeliveryDate().toLocalDate())))
                .collect(Collectors.toList());
    }

    public static OrderService getInstance() {
        return INSTANCE;
    }

    private OrderService() {
    }
}

package javaguru.jdbc.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data @Builder
public class Item {
    private Order order;
    private Product product;
    private Integer quantity;
    private BigDecimal priceOrder;
}

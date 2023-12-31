package javaguru.jdbc.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data @Builder
public class Product {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer balance;
}

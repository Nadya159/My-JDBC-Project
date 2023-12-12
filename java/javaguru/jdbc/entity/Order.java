package javaguru.jdbc.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data @Builder
public class Order {
    private Integer id;
    private Integer customerId;
    private LocalDateTime createDate;
    private LocalDateTime deliveryDate;
    private String address;
    private OrderStatus status;
}

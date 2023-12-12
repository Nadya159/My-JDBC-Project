package javaguru.jdbc.entity;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class Customer {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String street;
    private String house;
    private String flat;
}

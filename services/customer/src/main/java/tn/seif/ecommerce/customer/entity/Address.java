package tn.seif.ecommerce.customer.entity;

import lombok.*;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Validated
@EqualsAndHashCode
public class Address {

    private String street;
    private String houseNumber;
    private String zipCode;
}

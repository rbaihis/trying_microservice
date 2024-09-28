package tn.seif.ecommerce.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.seif.ecommerce.customer.dto.CustomerRequest;
import tn.seif.ecommerce.customer.entity.Address;
import tn.seif.ecommerce.customer.entity.Customer;
import tn.seif.ecommerce.customer.util.CustomerMapper;


@ExtendWith(MockitoExtension.class)
public class CustomerMapperTest{



    @InjectMocks
    private CustomerMapper mapper;

    @Test
    @DisplayName("CustomerRequest to Customer Testing")
    public void requestToCustomerMappingTest(){

        CustomerRequest request = new CustomerRequest(null, "seif", "rabbeh" , "rabbehs@gmail.com", new Address("khaznadar","92", "2000"));
        Customer expectedOutput = Customer.builder().id(request.getId()).firstName(request.getFirstName()).lastName(request.getLastName()).email(request.getEmail()).address(request.getAddress()).build();
        Assertions.assertEquals( expectedOutput, mapper.requestToCustomerMapping(request));
    }





}

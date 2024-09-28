package tn.seif.ecommerce.customer.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.seif.ecommerce.customer.dto.CustomerRequest;
import tn.seif.ecommerce.customer.dto.CustomerResponse;
import tn.seif.ecommerce.customer.entity.Address;
import tn.seif.ecommerce.customer.entity.Customer;
import tn.seif.ecommerce.exception.EmptyRequestException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerMapperTest {

    @InjectMocks
    private CustomerMapper mapper;


    @Test
    @DisplayName(" CustomerRequest to customer mapping method")
    void requestToCustomerMapping() {
        CustomerRequest goodRequest = new CustomerRequest(null, "seif", "rabbeh" , "rabbehs@gmail.com", new Address("khaznadar","92", "2000"));
        Customer expectedOutput = Customer.builder().id(goodRequest.getId()).firstName(goodRequest.getFirstName()).lastName(goodRequest.getLastName()).email(goodRequest.getEmail()).address(goodRequest.getAddress()).build();

        assertAll(
                () -> assertEquals( expectedOutput, mapper.requestToCustomerMapping(goodRequest) ),
                () -> assertThrows(EmptyRequestException.class , ()-> mapper.requestToCustomerMapping(null)  ),
                () -> assertThrows( EmptyRequestException.class , ()-> mapper.requestToCustomerMapping( new CustomerRequest(null, null, "rabbeh" , "rabbehs@gmail.com", new Address("khaznadar","92", "2000") ) ) ),
                () -> assertThrows(EmptyRequestException.class , ()-> mapper.requestToCustomerMapping( new CustomerRequest(null, "seif", null , "rabbehs@gmail.com", new Address("khaznadar","92", "2000")))),
                () -> assertThrows(EmptyRequestException.class , ()-> mapper.requestToCustomerMapping( new CustomerRequest(null, "seif", "rabbeh" , null, new Address("khaznadar","92", "2000")))),
                () -> assertThrows(EmptyRequestException.class , ()-> mapper.requestToCustomerMapping( new CustomerRequest(null, "seif", "rabbeh" , "rabbehs@gmail.com", null )))
        );




    }

    @Test
    @DisplayName(" customer to CustomerResponse mapper test")
    void customerToResponseMapping() {
        Customer customer = new Customer("11111", "seif", "rabbeh" , "rabbehs@gmail.com", new Address("khaznadar","92", "2000"));
        CustomerResponse expectedOutput = CustomerResponse.builder().id(customer.getId()).firstName(customer.getFirstName()).lastName(customer.getLastName()).email(customer.getEmail()).address(customer.getAddress()).build();
        assertEquals( expectedOutput, mapper.customerToResponseMapping(customer));
        assertEquals( "khaznadar", mapper.customerToResponseMapping(customer).getAddress().getStreet());
    }
}
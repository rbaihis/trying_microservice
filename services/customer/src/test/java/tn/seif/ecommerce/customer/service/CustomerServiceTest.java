package tn.seif.ecommerce.customer.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.seif.ecommerce.customer.dto.CustomerRequest;
import tn.seif.ecommerce.customer.dto.CustomerResponse;
import tn.seif.ecommerce.customer.entity.Address;
import tn.seif.ecommerce.customer.entity.Customer;
import tn.seif.ecommerce.customer.repository.CustomerRepository;
import tn.seif.ecommerce.customer.util.CustomerMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks private CustomerService service;
    @Mock private CustomerRepository mockRepository;
    @Mock private CustomerMapper mockMapper;


    @Test
    @DisplayName("Adding customer Service Test")
    void createCustomer() {
        CustomerRequest request = new CustomerRequest(null, "seif", "rabbeh" , "rabbehs@gmail.com", new Address("khaznadar","92", "2000"));
        Customer customerMapped =  new Customer(null, "seif", "rabbeh" , "rabbehs@gmail.com", new Address("khaznadar","92", "2000"));
        Customer customerRepo =  new Customer("1111", "seif", "rabbeh" , "rabbehs@gmail.com", new Address("khaznadar","92", "2000"));
        CustomerResponse response =  new CustomerResponse("1111", "seif", "rabbeh" , "rabbehs@gmail.com", new Address("khaznadar","92", "2000"));
        when(mockMapper.requestToCustomerMapping(request)).thenReturn(customerMapped);
        when(mockRepository.save(customerMapped)).thenReturn(customerRepo);

        Assertions.assertEquals(response.getId(),service.createCustomer(request));
    }


    @Test
    void updateCustomer() {
    }

    @Test
    void getAllCustomerResponse() {
    }

    @Test
    void getCustomerResponse() {
    }

    @Test
    void customerExist() {
    }

    @Test
    void deleteCustomer() {
    }
}
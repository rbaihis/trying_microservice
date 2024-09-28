package tn.seif.ecommerce.customer.util;

import org.springframework.stereotype.Service;
import tn.seif.ecommerce.customer.entity.Customer;
import tn.seif.ecommerce.customer.dto.CustomerRequest;
import tn.seif.ecommerce.customer.dto.CustomerResponse;
import tn.seif.ecommerce.exception.EmptyRequestException;

@Service
public class CustomerMapper implements ICustomerMapper {


    @Override
    public Customer requestToCustomerMapping(CustomerRequest request){
        boolean isNotValid = (request == null || request.getFirstName()== null || request.getLastName()== null || request.getEmail() == null ||  request.getAddress() == null );
        if (isNotValid)
            throw new EmptyRequestException("customer request passed is null or required are null ");
        return Customer.builder()
                .id(request.getId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail()).address(request.getAddress()).build();
    }


    @Override
    public CustomerResponse customerToResponseMapping(Customer customer){

        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .build();
    }






}

package tn.seif.ecommerce.customer.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tn.seif.ecommerce.customer.entity.Customer;
import tn.seif.ecommerce.customer.dto.CustomerRequest;
import tn.seif.ecommerce.customer.dto.CustomerResponse;
import tn.seif.ecommerce.customer.util.ICustomerMapper;
import tn.seif.ecommerce.customer.repository.CustomerRepository;
import tn.seif.ecommerce.exception.CustomerNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository repository;
    private final ICustomerMapper mapper;

    @Override
    public String createCustomer(CustomerRequest request) {
        log.info("creating new customer with email: {}",request.getEmail());
        return repository.save(mapper.requestToCustomerMapping(request)).getId();
    }
    //---------------------------
    @Override
    public void updateCustomer(CustomerRequest request) {
        log.info("updating customer with id: {}",request.getId());
        repository.save( mergeCustomer(getCustomerById(request.getId()) , request));
    }

    private Customer mergeCustomer(Customer customer, CustomerRequest request) {

        if(StringUtils.isNotBlank(request.getFirstName()))
            customer.setFirstName(request.getFirstName());
        if(StringUtils.isNotBlank(request.getLastName()))
            customer.setLastName(request.getLastName());
        if(StringUtils.isNotBlank(request.getEmail()))
            customer.setEmail(request.getEmail());
        if(request.getAddress() != null)
            customer.setAddress(request.getAddress());

        return customer;
    }

    //--------------------------

    private List<Customer> getAllCustomer(){
        return repository.findAll();
    }

    @Override
    public List<CustomerResponse> getAllCustomerResponse() {
        log.info("fetching all customers.");
        return getAllCustomer().stream().map(mapper::customerToResponseMapping).collect(Collectors.toList());
    }
    //-------------------------
    private Customer getCustomerById(String id){
        return repository.findById(id)
                .orElseThrow(()->new CustomerNotFoundException(
                        String.format("can not find Customer:: No customer found with id %s ", id)
                ));
    }
    @Override
    public CustomerResponse getCustomerResponse(String id) {
        log.info("getting customer by ID, with  id:{}.",id);
        return mapper.customerToResponseMapping(getCustomerById(id));
    }

    @Override
    public boolean customerExist(String id) {
        log.info("Checking  customer existence by ID, with  id:{}.",id);
        return repository.existsById(id);
    }

    //--------
    @Override
    public void deleteCustomer(String id) {
        log.info("deleting  customer by ID, with  id:{}.",id);
        if (!customerExist(id))
            return;
        repository.deleteById(id);
    }
}


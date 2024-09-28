package tn.seif.ecommerce.service;


import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.seif.ecommerce.customer.dto.CustomerRequest;
import tn.seif.ecommerce.customer.dto.CustomerResponse;
import tn.seif.ecommerce.customer.entity.Address;
import tn.seif.ecommerce.customer.entity.Customer;
import tn.seif.ecommerce.customer.repository.CustomerRepository;
import tn.seif.ecommerce.customer.service.CustomerService;
import tn.seif.ecommerce.customer.util.CustomerMapper;
import tn.seif.ecommerce.exception.CustomerNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks private CustomerService service;
    @Mock private CustomerRepository mockRepository;
    @Mock private CustomerMapper mockMapper;

    //--------attributes------
    private  List<Customer> customers = new ArrayList<>();

    // ---util methods
    private CustomerResponse utilCustomerToCustomerResponse(Customer c){
        return CustomerResponse.builder().id(c.getId()).firstName(c.getFirstName())
                .email(c.getEmail()).lastName(c.getLastName()).address(c.getAddress())
                .build();
    }
    private Customer utilCRequestToCustomer(CustomerRequest req){
        return Customer.builder().id(req.getId()).firstName(req.getFirstName())
                .email(req.getEmail()).lastName(req.getLastName()).address(req.getAddress())
                .build();
    }


    // ---hooks
    @BeforeEach
    void init(){
         customers.add(new Customer("1111", "seif", "rabbeh", "rabbehs@gmail.com", new Address("khaznadar", "92", "2000")));
         customers.add(new Customer("2222", "ali", "ben", "ali@gmail.com", new Address("bardo", "75", "2000")) );
         customers.add(new Customer("3333", "sabrine", "lom", "sabrine@gmail.com", new Address("zouhour", "33", "2052")));
    }

    // ---testing
    @Test
    @DisplayName("Adding customer Service Test")
    void createCustomer(){
        //verify adding customer behavior

        when(mockMapper.requestToCustomerMapping(Mockito.any(CustomerRequest.class)))
                .thenAnswer(invocation ->  utilCRequestToCustomer(invocation.getArgument(0)));
        when(mockRepository.save(Mockito.any(Customer.class))).
                thenAnswer(invocation ->{
                    ((Customer) invocation.getArgument(0)).setId("0000");
                    return ((Customer) invocation.getArgument(0));
                });

        assertEquals("0000", service.createCustomer(new CustomerRequest(null, "slim", "mochSlim" , "slim@gmail.com", new Address("beja","56", "3010"))));
    }

    @Test
    @DisplayName("checking customer exist with ")
    void customerExist() {
        //verify checking if customer exist or not behavior
        when(mockRepository.existsById(Mockito.any(String.class)))
                .thenAnswer(invocation-> customers.stream().anyMatch(c->c.getId().equals(invocation.getArgument(0))));
        assertAll(
                ()-> assertTrue(service.customerExist("1111"),"customer should exist in fake db"),
                ()-> assertFalse(service.customerExist("0000"),"customer should not exist in this fake db")
        );
    }



    @Test
    @DisplayName("testing get all customers, with empty and full db")
    public  void getAllCustomerResponse() {
        assertAll(
                // db contains data
                ()->{
                    when(mockRepository.findAll()).thenReturn(customers);
                    when(mockMapper.customerToResponseMapping(Mockito.any(Customer.class)))
                            .thenAnswer( invocation -> {
                                if(invocation.getArgument(0)==null)
                                    return null;
                                Customer dynamicCustomer = invocation.getArgument(0);
                                return utilCustomerToCustomerResponse(dynamicCustomer);
                            });
                    for (int i = 0; i < customers.size(); i++) {
                        assertEquals(customers.get(i).getId(), service.getAllCustomerResponse().get(i).getId());
                        assertEquals(customers.get(i).getFirstName(), service.getAllCustomerResponse().get(i).getFirstName());
                        assertEquals(customers.get(i).getLastName(), service.getAllCustomerResponse().get(i).getLastName());
                        assertEquals(customers.get(i).getEmail(), service.getAllCustomerResponse().get(i).getEmail());
                        assertEquals(customers.get(i).getAddress(), service.getAllCustomerResponse().get(i).getAddress());
                    }
                },
                // db contains no data
                ()->{
                    when(mockRepository.findAll()).thenReturn(new ArrayList<Customer>());
                    assertEquals(new ArrayList<CustomerResponse>(), service.getAllCustomerResponse());
                }
                );
    }



    @Test
    @DisplayName("get customerResponse by id ")
    public  void getCustomerResponse() {
        when(mockRepository.findById(Mockito.any(String.class)))
                .thenAnswer( invocation ->{
                    String id = invocation.getArgument(0);
                    return customers.stream().filter(customer -> customer.getId().equals(id)).findAny();
                });
        when(mockMapper.customerToResponseMapping(Mockito.any(Customer.class)))
                .thenAnswer(invocation -> utilCustomerToCustomerResponse(invocation.getArgument(0)));

        assertAll(
                ()->assertEquals("1111", service.getCustomerResponse("1111").getId()),
                ()-> assertThrowsExactly(CustomerNotFoundException.class ,()->service.getCustomerResponse("0000"))
        );
    }


    @Test
    @DisplayName("updating customer with mockedDB")
    void updateCustomer() {
        when(mockRepository.findById(Mockito.any(String.class)))
                .thenAnswer( invocation ->{
                    String id = invocation.getArgument(0);
                    return customers.stream().filter(customer -> customer.getId().equals(id)).findFirst();
                });
        when(mockRepository.save(Mockito.any(Customer.class)))
                .thenAnswer(invocation ->{
                    Customer invocatedCustomer = invocation.getArgument(0);
                    return customers.stream()
                            .filter(c->c.getId().equals(invocatedCustomer.getId()))
                            .findFirst()
                            .map(c-> {
                                c.setId(invocatedCustomer.getId());
                                c.setFirstName(invocatedCustomer.getFirstName());
                                c.setLastName(invocatedCustomer.getLastName());
                                c.setEmail(invocatedCustomer.getEmail());
                                c.setAddress(invocatedCustomer.getAddress());
                                return c;
                            }).orElseThrow();
                } );

        assertAll(
                ()->{
                    //data exist
                    CustomerRequest request = new CustomerRequest("1111", "zzzzz", "rabbeh", "rabbehs@gmail.com", new Address("khaznadar", "92", "2000"));
                    service.updateCustomer(request);
                    Customer cusFromDb= customers.stream().filter(c->c.getId().equals("1111")).findFirst().orElse(new Customer(null,"WrongName",null,null,null));
                    assertEquals("zzzzz", cusFromDb.getFirstName());
                },
                ()->{
                    //data Not Exist
                    CustomerRequest request = new CustomerRequest("0000", "zzzzz", "rabbeh", "rabbehs@gmail.com", new Address("khaznadar", "92", "2000"));
                    assertThrows(CustomerNotFoundException.class, ()-> service.updateCustomer(request));
                }
        );
    }


    @Test
    @DisplayName("deleting customer from mock db by id")
    public void deleteCustomer() {

        int initialDbRowSize= customers.size();
        doAnswer(invocation ->{
            customers.removeIf(c->c.getId().equals(invocation.getArgument(0)));
            return null; // since its void
        }).when(mockRepository).deleteById(Mockito.any(String.class));

        when(mockRepository.existsById(Mockito.any(String.class)))
                .thenAnswer( invocation ->{
                    System.out.println("repo.existById called...");
                    String id = invocation.getArgument(0);
                    return customers.stream().anyMatch(customer -> customer.getId().equals(id));
                });

        // present in db
        service.deleteCustomer("1111");
        int expectedRowSize_OperationOne = initialDbRowSize - 1;
        assertEquals(expectedRowSize_OperationOne,customers.size(), "should be deleted successfully");

        // not present in db (size should not change)
        service.deleteCustomer("1111");
        assertEquals(expectedRowSize_OperationOne,customers.size(), "already deleted nothing should happen to db");
    }

}

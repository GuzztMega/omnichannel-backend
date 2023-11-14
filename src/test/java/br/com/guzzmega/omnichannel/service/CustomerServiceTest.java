package br.com.guzzmega.omnichannel.service;

import br.com.guzzmega.omnichannel.domain.ChannelType;
import br.com.guzzmega.omnichannel.domain.Customer;
import br.com.guzzmega.omnichannel.repository.CustomerRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Must save a Valid Customer")
    public void whenSavingValidCustomerShouldReturnSaved() {
        var customer = new Customer("John", "john@gmail.com", "+5541987654321");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        var savedCustomer = customerService.save(customer);

        assertSame(customer, savedCustomer, "The saved customer should be returned.");
        verify(customerRepository).save(customer);
    }

    @Test
    @DisplayName("Must find by Valid Customer ID")
    public void whenFindByValidIdShouldReturnExpectedCustomer() {
        Long validId = 1L;
        var customer = new Customer("John", "john@gmail.com", "");
        when(customerRepository.findById(validId)).thenReturn(Optional.of(customer));

        var foundCustomer = customerService.findById(validId);

        assertSame(customer, foundCustomer, "The expected customer should be returned.");
    }

    @Test
    @DisplayName("Must throw Exception when try find by an Invalid ID")
    public void whenFindByInvalidIdShouldThrowException() {
        Long invalidId = 0L;
        when(customerRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> customerService.findById(invalidId),
                "Finding by an invalid ID should throw an ObjectNotFoundException.");
    }

    @Test
    @DisplayName("Must find by Email or Phone based on ChannelType")
    public void whenFindByChannelTypeShouldReturnCustomer() {
        String email = "john@gmail.com";
        String phoneNumber = "+5544988775544";

        var customer = new Customer("John", "john@gmail.com", "+5544988775544");
        when(customerRepository.findCustomerByEmail(email)).thenReturn(Optional.of(customer));
        when(customerRepository.findCustomerByPhoneNumber(phoneNumber)).thenReturn(Optional.of(customer));

        var foundByEmailCustomer = customerService.findByChannelType(email, ChannelType.EMAIL);
        var foundByPhoneCustomer = customerService.findByChannelType(phoneNumber, ChannelType.SMS);

        assertSame(customer, foundByEmailCustomer, "The customer with given email and channel type should be returned.");
        assertSame(customer, foundByPhoneCustomer, "The customer with given phone number and channel type should be returned.");
    }

    @Test
    @DisplayName("Must find a List of All Customers Successful")
    public void whenFindAllShouldReturnListOfAllCustomers() {
        List<Customer> customers = List.of(
                new Customer("John", "john@gmail.com", "+5544988775544"),
                new Customer("Mary", "mary@gmail.com", "+5544911223388"));
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> foundCustomers = customerService.findAll();

        assertSame(customers, foundCustomers, "All customers should be returned.");
    }
}

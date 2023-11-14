package br.com.guzzmega.omnichannel.controller;

import br.com.guzzmega.omnichannel.domain.*;
import br.com.guzzmega.omnichannel.domain.record.AssignChannelRecord;
import br.com.guzzmega.omnichannel.domain.record.CustomerRecord;
import br.com.guzzmega.omnichannel.service.ChannelService;
import br.com.guzzmega.omnichannel.service.CustomerService;
import br.com.guzzmega.omnichannel.service.InteractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;
    @Mock
    private ChannelService channelService;
    @Mock
    private InteractionService interactionService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(
                        new MockHttpServletRequest()));
    }

    @Test
    @DisplayName("Must create a Customer successfully")
    public void whenCreateCustomerShouldReturnCreatedCustomerWithUri() {
        var customerRecord = new CustomerRecord("John", "john@gmail.com", "+5541987654321");
        var customer       = new Customer("John", "john@gmail.com", "+5541987654321");
        when(customerService.save(any(Customer.class))).thenReturn(customer);

        ResponseEntity<Customer> responseEntity = customerController.createCustomer(customerRecord);

        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getHeaders().getLocation());
        assertEquals(201, responseEntity.getStatusCodeValue());
    }

    @Test
    @DisplayName("Must retrieve Customer by ID successfully")
    public void whenGetCustomerByIdShouldReturnCustomer() {
        Long validId = 1L;
        var customer = new Customer("John", "john@gmail.com", "+5541987654321");
        when(customerService.findById(validId)).thenReturn(customer);

        ResponseEntity<Customer> responseEntity = customerController.getCustomer(validId);

        assertSame(customer, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    @DisplayName("Must assign Customer to a Channel successfully")
    public void whenInsertCustomerIntoChannelShouldReturnUpdatedChannelWithUri() {
        Long customerId = 1L;
        var record   = new AssignChannelRecord(1L);
        var customer = new Customer("John", "john@gmail.com", "+5541987654321");
        var channel  = new Channel("Omni Center", "omnichannel@gmail.com", "+5541987654321", ChannelType.WHATSAPP);

        when(customerService.findById(customerId)).thenReturn(customer);
        when(channelService.findById(record.channelId())).thenReturn(channel);
        when(channelService.save(channel)).thenReturn(channel);

        ResponseEntity<Channel> responseEntity = customerController.insertIntoChannel(customerId, record);

        assertTrue(channel.getCustomerList().contains(customer));
        assertEquals(201, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getHeaders().getLocation());
    }

    @Test
    @DisplayName("Must retrieve Customer interaction historic successfully")
    public void whenGetHistoricShouldReturnInteractions() {
        Long customerId = 1L;
        var interaction1 = new Interaction( "mary@gmail.com","omnichannel@gmail.com","Hey!");
        interaction1.setSendDate(LocalDateTime.now());
        interaction1.setInteractionStatus(InteractionStatus.QUEUED);
        var interaction2 = new Interaction( "mary@gmail.com","omnichannel@gmail.com","Hey?");
        interaction2.setSendDate(LocalDateTime.now());
        interaction2.setInteractionStatus(InteractionStatus.QUEUED);

        List<Interaction> interactions = List.of(interaction1, interaction2);
        when(interactionService.findByCustomer(customerId)).thenReturn(interactions);

        ResponseEntity<List<Interaction>> responseEntity = customerController.getHistoric(customerId);

        assertSame(interactions, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
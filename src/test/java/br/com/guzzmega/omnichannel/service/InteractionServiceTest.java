package br.com.guzzmega.omnichannel.service;

import br.com.guzzmega.omnichannel.domain.*;
import br.com.guzzmega.omnichannel.domain.record.InteractionRecord;
import br.com.guzzmega.omnichannel.repository.InteractionRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InteractionServiceTest {

    @Mock
    private InteractionRepository interactionRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private ChannelService channelService;

    @InjectMocks
    private InteractionService interactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should enqueue and save Interaction successfully")
    void whenEnqueueInteractionShouldProcessSuccessfully() throws Exception {
        var interactionRecord = new InteractionRecord( "john@gmail.com", "omnichannel@gmail.com","Hello, how are you?");
        var interaction = new Interaction( "john@gmail.com", "omnichannel@gmail.com","Hello, how are you?");
        interaction.setInteractionStatus(InteractionStatus.QUEUED);

        var sentInteraction = interaction;
        var channel = new Channel("Omni Center", "omnichannel@gmail.com", "+5541987654321", ChannelType.CHAT);
        var customer = new Customer("John", "john@gmail.com", "+5541987654321");
        channel.getCustomerList().add(customer);

        when(channelService.findByEmailOrPhoneNumber(anyString(), anyString())).thenReturn(channel);
        when(customerService.findByChannelType(anyString(), any(ChannelType.class))).thenReturn(customer);
        when(interactionRepository.save(any(Interaction.class))).thenReturn(sentInteraction);

        interactionService.processInteraction(sentInteraction, interactionRecord);
        var savedInteraction = interactionService.enqueueInteraction(interactionRecord);

        assertEquals(InteractionStatus.SENT, savedInteraction.getInteractionStatus(), "The saved interaction should be returned with Sent status.");
        verify(interactionRepository).save(any(Interaction.class));
    }

    @Test
    @DisplayName("Should handle error during Interaction enqueue")
    void whenEnqueueInteractionWithErrorShouldHandleError() throws Exception {
        var interactionRecord = new InteractionRecord("omnichannel@gmail.com", "john@gmail.com", "Hello, how are you?");
        var interaction = new Interaction( "john@gmail.com", "omnichannel@gmail.com","Hello, how are you?");
        interaction.setInteractionStatus(InteractionStatus.QUEUED);

        var failedInteraction = interaction;
        interactionService.handleInteractionError(failedInteraction, interactionRecord, new ValidationException("Test Exception"));
        when(channelService.findByEmailOrPhoneNumber(anyString(), anyString())).thenThrow(new ValidationException("Test Exception"));
        when(interactionRepository.save(any(Interaction.class))).thenReturn(failedInteraction);


        var savedInteraction = interactionService.enqueueInteraction(interactionRecord);
        assertEquals(InteractionStatus.FAILED, savedInteraction.getInteractionStatus());
        assertNotNull(savedInteraction.getErrorMessage());
        verify(interactionRepository).save(any(Interaction.class));
    }

    @Test
    @DisplayName("Should correctly start an Interaction")
    void whenStartInteractionShouldInitializeCorrectly() {
        var interactionRecord = new InteractionRecord("omnichannel@gmail.com", "john@gmail.com", "Hello, how are you?");
        var interaction = interactionService.startInteraction(interactionRecord);

        assertEquals(interactionRecord.channelParam(), interaction.getChannelParam());
        assertEquals(interactionRecord.customerParam(), interaction.getCustomerParam());
        assertEquals(interactionRecord.body(), interaction.getBody());
        assertNotNull(interaction.getSendDate());
        assertEquals(InteractionStatus.QUEUED, interaction.getInteractionStatus());
    }

    @Test
    @DisplayName("Should process an Interaction correctly")
    void whenProcessInteractionShouldSetCorrectDetails() throws Exception {
        var interaction = new Interaction( "john@gmail.com", "omnichannel@gmail.com", "Hello, how are you?");
        var interactionRecord = new InteractionRecord("omnichannel@gmail.com", "john@gmail.com", "Hello, how are you?");
        var channel  = new Channel("Omni Center", "omnichannel@gmail.com", "+5541987654321", ChannelType.EMAIL);
        var customer = new Customer("John", "john@gmail.com", "+5541987654321");
        channel.getCustomerList().add(customer);

        when(channelService.findByEmailOrPhoneNumber(anyString(), anyString())).thenReturn(channel);
        when(customerService.findByChannelType(anyString(), any(ChannelType.class))).thenReturn(customer);

        interactionService.processInteraction(interaction, interactionRecord);

        assertEquals(channel, interaction.getChannel());
        assertEquals(customer, interaction.getCustomer());
        assertEquals(InteractionStatus.SENT, interaction.getInteractionStatus());
    }

    @Test
    @DisplayName("Should handle Interaction error correctly")
    void whenHandleInteractionErrorShouldSetErrorDetails() {
        var interaction = new Interaction( "john@gmail.com", "omnichannel@gmail.com","Hello, how are you?");
        var interactionRecord = new InteractionRecord("omnichannel@gmail.com", "john@gmail.com", "Hello, how are you?");
        var exception = new Exception("Test exception");

        interactionService.handleInteractionError(interaction, interactionRecord, exception);

        assertNotNull(interaction.getErrorDate());
        assertTrue(interaction.getErrorMessage().contains("Test exception"));
        assertEquals(InteractionStatus.FAILED, interaction.getInteractionStatus());
    }

    @Test
    @DisplayName("Should save Interaction correctly")
    void whenSaveInteractionShouldReturnSavedInteraction() {
        var interaction = new Interaction("john@gmail.com", "omnichannel@gmail.com","Hello, how are you?");
        when(interactionRepository.save(interaction)).thenReturn(interaction);

        var savedInteraction = interactionService.saveInteraction(interaction);

        assertSame(interaction, savedInteraction);
        verify(interactionRepository).save(interaction);
    }

    @Test
    @DisplayName("Should calculate call duration correctly")
    void whenGetCallDurationShouldReturnCorrectDuration() {
        var interaction = new Interaction( "john@gmail.com","omnichannel@gmail.com", "Hello, how are you?");
        interaction.setVoiceStartDate(LocalDateTime.now().minusMinutes(1));
        interaction.setVoiceEndDate(LocalDateTime.now());

        long duration = interactionService.getCallDurationMiliSeconds(interaction);

        assertTrue(duration > 0);
        assertTrue(duration <= 60000);
    }
}
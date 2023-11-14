package br.com.guzzmega.omnichannel.controller;

import br.com.guzzmega.omnichannel.domain.Interaction;
import br.com.guzzmega.omnichannel.domain.InteractionStatus;
import br.com.guzzmega.omnichannel.domain.record.InteractionRecord;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class InteractionControllerTest {

    @Mock
    private InteractionService interactionService;

    @InjectMocks
    private InteractionController interactionController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(
                        new MockHttpServletRequest()));
    }

    @Test
    @DisplayName("Must retrieve Interaction by ID successfully")
    public void whenGetInteractionByIdShouldReturnInteraction() {
        Long validId = 1L;
        var interaction = new Interaction( "john@gmail.com","omnichannel@gmail.com","Hello, how are you?");
        when(interactionService.findById(validId)).thenReturn(interaction);

        ResponseEntity<Interaction> responseEntity = interactionController.getInteraction(validId);

        assertSame(interaction, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    @DisplayName("Must create Interaction successfully")
    void whenCreateInteractionShouldReturnCreatedInteraction() {
        var interactionRecord = new InteractionRecord( "john@gmail.com", "omnichannel@gmail.com","Hello, how are you?");
        var interaction = new Interaction( "john@gmail.com","omnichannel@gmail.com","Hello, how are you?");
        interaction.setInteractionStatus(InteractionStatus.SENT);

        when(interactionService.enqueueInteraction(interactionRecord)).thenReturn(interaction);
        ResponseEntity<Interaction> responseEntity = interactionController.createInteraction(interactionRecord);

        assertEquals(201, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getHeaders().getLocation());
        assertSame(interaction, responseEntity.getBody());
    }

    @Test
    @DisplayName("Must handle failed Interaction creation")
    void whenCreateInteractionFailsShouldReturnNotFound() {
        var interactionRecord = new InteractionRecord( "john@gmail.com", "omnichannel@gmail.com","Hello, how are you?");
        var interaction = new Interaction( "john@gmail.com","omnichannel@gmail.com","Hello, how are you?");
        interaction.setInteractionStatus(InteractionStatus.FAILED);

        when(interactionService.enqueueInteraction(interactionRecord)).thenReturn(interaction);
        ResponseEntity<Interaction> responseEntity = interactionController.createInteraction(interactionRecord);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertNull(responseEntity.getBody());
    }
}

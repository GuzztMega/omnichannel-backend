package br.com.guzzmega.omnichannel.controller;

import br.com.guzzmega.omnichannel.domain.Channel;
import br.com.guzzmega.omnichannel.domain.ChannelType;
import br.com.guzzmega.omnichannel.domain.record.ChannelRecord;
import br.com.guzzmega.omnichannel.service.ChannelService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class ChannelControllerTest {

    @Mock
    private ChannelService channelService;

    @InjectMocks
    private ChannelController channelController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(
                        new MockHttpServletRequest()));
    }

    @Test
    @DisplayName("Must create a Channel and return URI Successfully")
    public void whenCreateChannelShouldReturnCreatedChannelWithUri() {
        var channelRecord = new ChannelRecord("Omni Center", "omnichannel@gmail.com", "+5541987654321", ChannelType.EMAIL);
        var channel       = new Channel("Omni Center", "omnichannel@gmail.com", "+5541987654321", ChannelType.EMAIL);
        when(channelService.save(any(Channel.class))).thenReturn(channel);

        ResponseEntity<Channel> responseEntity = channelController.createChannel(channelRecord);

        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getHeaders().getLocation());
        assertEquals(201, responseEntity.getStatusCodeValue());
    }

    @Test
    @DisplayName("Must get Channel by ID successfully")
    public void whenGetChannelByIdShouldReturnChannel() {
        Long validId = 1L;
        var channel = new Channel("Omni Center", "omnichannel@gmail.com", "+5541987654321", ChannelType.SMS);
        when(channelService.findById(validId)).thenReturn(channel);

        ResponseEntity<Channel> responseEntity = channelController.getChannel(validId);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertSame(channel, responseEntity.getBody(), "The returned channel should match the expected one.");
    }

    @Test
    @DisplayName("Must get All Channels successfully")
    public void whenGetAllChannelsShouldReturnChannelList() {
        List<Channel> channels = List.of(
                new Channel("Omni Center", "omnichannel@gmail.com", "+5541987654321", ChannelType.VOICE),
                new Channel("AI HelpCenter", "aihelp@gmail.com", "+5541987654321", ChannelType.EMAIL));
        when(channelService.findAll()).thenReturn(channels);

        ResponseEntity<List<Channel>> responseEntity = channelController.getAllChannels();

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertSame(channels, responseEntity.getBody(), "The returned channels list should match the expected list.");
    }
}
package br.com.guzzmega.omnichannel.service;

import br.com.guzzmega.omnichannel.domain.Channel;
import br.com.guzzmega.omnichannel.domain.ChannelType;
import br.com.guzzmega.omnichannel.repository.ChannelRepository;
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

public class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @InjectMocks
    private ChannelService channelService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Must save a Valid Channel Successfully")
    public void whenSavingValidChannelShouldReturnSaved() {
        var channel = new Channel("Omni Center", "omnichannel@gmail.com", "+5541987654321", ChannelType.EMAIL);
        when(channelRepository.save(any(Channel.class))).thenReturn(channel);

        var savedChannel = channelService.save(channel);

        assertSame(channel, savedChannel, "The saved channel should be returned.");
        verify(channelRepository).save(channel);
    }

    @Test
    @DisplayName("Must find by Valid Channel ID")
    public void whenFindByValidIdShouldReturnExpectedChannel() {
        Long validId = 1L;
        var channel = new Channel("Omni Center", "omnichannel@gmail.com", "+5541987654321", ChannelType.SMS);
        when(channelRepository.findById(validId)).thenReturn(Optional.of(channel));

        var foundChannel = channelService.findById(validId);

        assertSame(channel, foundChannel, "The expected channel should be returned.");
    }

    @Test
    @DisplayName("Must throw Exception when try find by an Invalid ID")
    public void whenFindByInvalidIdShouldThrowException() {
        Long invalidId = 0L;
        when(channelRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> channelService.findById(invalidId),
                "Finding by an invalid ID should throw an ObjectNotFoundException.");
    }

    @Test
    @DisplayName("Must find by Email or PhoneNumber, then return Channel Successfully")
    public void whenFindByValidEmailOrPhoneNumberShouldReturnChannel() {
        String email = "omnichannel@gmail.com";
        String phoneNumber = "+5541987654321";
        var channel = new Channel("Omni Center", "omnichannel@gmail.com", "+5541987654321", ChannelType.CHAT);
        when(channelRepository.findChannelByEmailOrPhoneNumber(email, "")).thenReturn(Optional.of(channel));
        when(channelRepository.findChannelByEmailOrPhoneNumber("", phoneNumber)).thenReturn(Optional.of(channel));

        var emailChannel = channelService.findByEmailOrPhoneNumber(email, "");
        var phoneChannel = channelService.findByEmailOrPhoneNumber("", phoneNumber);

        assertSame(channel, emailChannel, "The channel with given email should be returned.");
        assertSame(channel, phoneChannel, "The channel with given phone number should be returned.");
        verify(channelRepository, times(1)).findChannelByEmailOrPhoneNumber(email, "");
        verify(channelRepository, times(1)).findChannelByEmailOrPhoneNumber("", phoneNumber);
    }

    @Test
    @DisplayName("Must find a List of All Channels Successfully")
    public void whenFindAllShouldReturnListOfAllChannels() {
        List<Channel> channels = List.of(
                new Channel("Voice Center", null, "+5541987654321", ChannelType.VOICE),
                new Channel("AI HelpCenter", "aihelp@gmail.com", null, ChannelType.EMAIL));
        when(channelRepository.findAll()).thenReturn(channels);

        List<Channel> foundChannels = channelService.findAll();

        assertSame(channels, foundChannels, "All channels should be returned.");
    }
}
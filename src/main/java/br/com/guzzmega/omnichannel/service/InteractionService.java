package br.com.guzzmega.omnichannel.service;

import br.com.guzzmega.omnichannel.domain.*;
import br.com.guzzmega.omnichannel.domain.record.InteractionRecord;
import br.com.guzzmega.omnichannel.repository.InteractionRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class InteractionService {

    @Autowired
    private InteractionRepository interactionRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ChannelService channelService;

    public Interaction save(Interaction interaction){
        return interactionRepository.save(interaction);
    }

    public Interaction findById(Long id){
        Optional<Interaction> optional = interactionRepository.findById(id);
        return optional.orElseThrow(() -> new ObjectNotFoundException("Interaction", id));
    }

    public List<Interaction> findByCustomer(Long customerId){
        return interactionRepository.getInteractionByCustomerId(customerId);
    }

    public Interaction enqueueInteraction(InteractionRecord interactionRecord){

        Interaction interaction = new Interaction(interactionRecord.channelParam(), interactionRecord.customerParam(), interactionRecord.body());
        interaction.setSendDate(LocalDateTime.now());
        interaction.setInteractionStatus(InteractionStatus.QUEUED);
        interaction = save(interaction);

        try {
            Channel channel = channelService.findByEmailOrPhoneNumber(interactionRecord.channelParam(), interactionRecord.channelParam());
            interaction.setChannel(channel);

            Customer customer = customerService.findByChannelType(interactionRecord.customerParam(), channel.getChannelType());
            interaction.setCustomer(customer);

            if(channel.getChannelType() == ChannelType.VOICE)
                interaction.setVoiceStartDate(LocalDateTime.now());

            interaction.setInteractionStatus(InteractionStatus.SENT);
        } catch (Exception exception) {
            interaction.setErrorDate(LocalDateTime.now());
            interaction.setErrorMessage("Couldn't deliver the message from " +
                    "Customer Param: " + interactionRecord.customerParam() + " to " +
                    "Channel Param: " + interactionRecord.channelParam() + ". " +
                    "An error has ocurred: " + exception.getMessage());
            interaction.setInteractionStatus(InteractionStatus.FAILED);
        } finally {
            interaction = interactionRepository.save(interaction);
        }

        return interaction;
    }

    public long getCallDurationSeconds(Interaction interaction){
        if(interaction.getVoiceEndDate() == null)
            return 0;

        return ChronoUnit.MILLIS.between(
                interaction.getVoiceStartDate().atZone(ZoneId.systemDefault()).toInstant(),
                interaction.getVoiceEndDate().atZone(ZoneId.systemDefault()).toInstant());
    }
}
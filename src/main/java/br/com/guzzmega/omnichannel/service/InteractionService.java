package br.com.guzzmega.omnichannel.service;

import br.com.guzzmega.omnichannel.domain.*;
import br.com.guzzmega.omnichannel.domain.record.InteractionRecord;
import br.com.guzzmega.omnichannel.repository.InteractionRepository;
import jakarta.validation.ValidationException;
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

    public Interaction enqueueInteraction(InteractionRecord interactionRecord) {
        Interaction interaction = startInteraction(interactionRecord);
        try {
            processInteraction(interaction, interactionRecord);
        } catch (Exception exception) {
            handleInteractionError(interaction, interactionRecord, exception);
        } finally {
            return saveInteraction(interaction);
        }
    }

    public Interaction startInteraction(InteractionRecord interactionRecord) {
        Interaction interaction = new Interaction(interactionRecord.customerParam(), interactionRecord.channelParam(), interactionRecord.body());
        interaction.setSendDate(LocalDateTime.now());
        interaction.setInteractionStatus(InteractionStatus.QUEUED);
        return interaction;
    }

    public void processInteraction(Interaction interaction, InteractionRecord interactionRecord) throws Exception {
        Channel channel = channelService.findByEmailOrPhoneNumber(interactionRecord.channelParam(), interactionRecord.channelParam());
        interaction.setChannel(channel);

        Customer customer = customerService.findByChannelType(interactionRecord.customerParam(), channel.getChannelType());
        interaction.setCustomer(customer);

        if(!channel.getCustomerList().contains(customer))
            throw new ValidationException("Customer is not a participant of the Channel");

        if (channel.getChannelType() == ChannelType.VOICE)
            interaction.setVoiceStartDate(LocalDateTime.now());

        interaction.setInteractionStatus(InteractionStatus.SENT);
    }

    public void handleInteractionError(Interaction interaction, InteractionRecord interactionRecord, Exception exception) {
        interaction.setErrorDate(LocalDateTime.now());
        interaction.setErrorMessage("Couldn't deliver the message from " +
                "Customer Param: " + interactionRecord.customerParam() + " to " +
                "Channel Param: " + interactionRecord.channelParam() + ". " +
                "An error has occurred: " + exception.getMessage());
        interaction.setInteractionStatus(InteractionStatus.FAILED);
    }

    public Interaction saveInteraction(Interaction interaction) {
        return interactionRepository.save(interaction);
    }

    public long getCallDurationMiliSeconds(Interaction interaction){
        if(interaction.getVoiceEndDate() == null)
            return 0;

        return ChronoUnit.MILLIS.between(
                interaction.getVoiceStartDate().atZone(ZoneId.systemDefault()).toInstant(),
                interaction.getVoiceEndDate().atZone(ZoneId.systemDefault()).toInstant());
    }
}
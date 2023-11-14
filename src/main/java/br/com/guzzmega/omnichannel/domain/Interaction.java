package br.com.guzzmega.omnichannel.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "INTERACTION")
public class Interaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Channel channel;
    @ManyToOne
    private Customer customer;
    private String channelParam;
    private String customerParam;
    private InteractionStatus interactionStatus;

    @NotNull
    private LocalDateTime sendDate;
    private LocalDateTime deliveryDate;
    private LocalDateTime readDate;
    private LocalDateTime errorDate;
    private LocalDateTime voiceStartDate;
    private LocalDateTime voiceEndDate;
    private String errorMessage;
    @NotEmpty
    private String body;

    public Interaction(){
    }

    public Interaction(String customerParam, String channelParam, String body) {
        this.body = body;
        this.channelParam = channelParam;
        this.customerParam = customerParam;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getVoiceStartDate() {
        return voiceStartDate;
    }

    public void setVoiceStartDate(LocalDateTime voiceStartDate) {
        this.voiceStartDate = voiceStartDate;
    }

    public LocalDateTime getVoiceEndDate() {
        return voiceEndDate;
    }

    public void setVoiceEndDate(LocalDateTime voiceEndDate) {
        this.voiceEndDate = voiceEndDate;
    }

    public LocalDateTime getSendDate() {
        return this.sendDate;
    }

    public void setSendDate(final LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    public LocalDateTime getDeliveryDate() {
        return this.deliveryDate;
    }

    public void setDeliveryDate(final LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalDateTime getReadDate() {
        return this.readDate;
    }

    public void setReadDate(final LocalDateTime readDate) {
        this.readDate = readDate;
    }

    public LocalDateTime getErrorDate() {
        return this.errorDate;
    }

    public void setErrorDate(final LocalDateTime errorDate) {
        this.errorDate = errorDate;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public String getChannelParam() {
        return this.channelParam;
    }

    public void setChannelParam(final String channelParam) {
        this.channelParam = channelParam;
    }

    public String getCustomerParam() {
        return this.customerParam;
    }

    public void setCustomerParam(final String customerParam) {
        this.customerParam = customerParam;
    }

    public InteractionStatus getInteractionStatus() {
        return this.interactionStatus;
    }

    public void setInteractionStatus(final InteractionStatus interactionStatus) {
        this.interactionStatus = interactionStatus;
    }
}

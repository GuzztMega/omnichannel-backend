package br.com.guzzmega.omnichannel.domain;

import jakarta.persistence.Table;

@Table(name = "CHANNELTYPE")
public enum ChannelType {
    CHAT, EMAIL, SMS, VOICE, WHATSAPP;
}

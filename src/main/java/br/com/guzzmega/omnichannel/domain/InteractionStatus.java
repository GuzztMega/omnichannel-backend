package br.com.guzzmega.omnichannel.domain;

import jakarta.persistence.Table;

@Table(name = "INTERACTIONSTATUS")
public enum InteractionStatus {
    FAILED, QUEUED, SENT, DELIVERED, READ
}
package br.com.guzzmega.omnichannel.domain.record;

import jakarta.validation.constraints.NotBlank;

public record InteractionRecord(
        @NotBlank(message = "CustomerParam is mandatory") String customerParam,
        @NotBlank(message = "ChannelParam is mandatory") String channelParam,
        @NotBlank(message = "Body is mandatory") String body
) {
}

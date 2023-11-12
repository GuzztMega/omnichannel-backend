package br.com.guzzmega.omnichannel.domain.record;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record InteractionRecord(
        @NotNull String customerParam,
        @NotNull String channelParam,
        @NotEmpty String body
) {
}

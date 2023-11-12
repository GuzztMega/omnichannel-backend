package br.com.guzzmega.omnichannel.domain.record;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AssignChannelRecord(
        @NotNull @Min(1) Long channelId) {
}

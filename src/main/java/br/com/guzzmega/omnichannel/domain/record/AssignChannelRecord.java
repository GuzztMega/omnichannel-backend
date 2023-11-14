package br.com.guzzmega.omnichannel.domain.record;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AssignChannelRecord(
        @NotNull @Min(value=1, message="ChannelId can't be 0") Long channelId) {
}

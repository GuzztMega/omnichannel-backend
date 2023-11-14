package br.com.guzzmega.omnichannel.domain.record;

import br.com.guzzmega.omnichannel.domain.ChannelType;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record ChannelRecord(
        @NotBlank @Length(max = 200, message = "200 chars max for name") String name,
        @NotNull @Email String email,
        @NotNull String phoneNumber,
        @NotNull ChannelType channelType
        ) {
}

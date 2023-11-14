package br.com.guzzmega.omnichannel.domain.record;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CustomerRecord(
        @NotBlank @Length(max=200, message="200 chars max for name") String name,
        @Email String email,
        String phoneNumber
        ) {
}

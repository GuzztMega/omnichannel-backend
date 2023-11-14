package br.com.guzzmega.omnichannel.domain;

import br.com.guzzmega.omnichannel.domain.record.ChannelRecord;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChannelRecordTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Must Accept a Valid Channel")
    public void whenChannelValidThenNoConstraintViolation() {
        String name = "Channel Name";
        String email = "channel@example.com";
        String phoneNumber = "";
        ChannelType channelType = ChannelType.CHAT;

        var channelRecord = new ChannelRecord(name, email, phoneNumber, channelType);
        var violations = validator.validate(channelRecord);

        assertTrue(violations.isEmpty(), "Valid ChannelRecord should not have constraint violations");
    }

    @Test
    @DisplayName("Must Reject Invalid Channel Name")
    public void whenBlankNameThenShouldHaveConstraintViolation() {
        String name = " ";
        String email = "channel@example.com";
        String phoneNumber = "1234567890";
        ChannelType channelType = ChannelType.SMS;

        var channelRecord = new ChannelRecord(name, email, phoneNumber, channelType);
        var violations = validator.validate(channelRecord);

        assertFalse(violations.isEmpty(), "ChannelRecord with blank name should have constraint violations");
    }

    @Test
    @DisplayName("Must Reject Invalid Channel Email")
    public void whenInvalidEmailThenShouldHaveConstraintViolation() {
        String name = "Channel Name";
        String email = "notAnEmail";
        String phoneNumber = null;
        ChannelType channelType = ChannelType.SMS;

        ChannelRecord channelRecord = new ChannelRecord(name, email, phoneNumber, channelType);
        var violations = validator.validate(channelRecord);

        assertFalse(violations.isEmpty(), "ChannelRecord with invalid email should have constraint violations");
    }
}
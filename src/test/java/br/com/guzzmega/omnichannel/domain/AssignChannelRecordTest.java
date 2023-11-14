package br.com.guzzmega.omnichannel.domain;

import br.com.guzzmega.omnichannel.domain.record.AssignChannelRecord;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssignChannelRecordTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Must Reject a Invalid Channel Assign")
    public void whenAssignChannelInvalidThenShouldHaveConstraintViolation() {
        Long channelId = 0L;

        var assignChannelRecord = new AssignChannelRecord(channelId);
        var violations = validator.validate(assignChannelRecord);

        assertEquals(1, violations.size(), "Invalid Assign Channel should have one constraint violations");
    }

    @Test
    @DisplayName("Must Accept a valid Channel Assign successful")
    public void wheAssignChannelValidThenShouldBeSuccessfull() {
        Long channelId = 1L;

        var assignChannelRecord = new AssignChannelRecord(channelId);
        var violations = validator.validate(assignChannelRecord);

        assertTrue(violations.isEmpty(), "Valid Assign Channel should not have constraint violations");
    }
}
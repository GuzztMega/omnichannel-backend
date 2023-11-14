package br.com.guzzmega.omnichannel.domain;

import br.com.guzzmega.omnichannel.domain.record.InteractionRecord;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterectionRecordTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Must Reject a Invalid Interaction")
    public void whenInteractionInvalidThenShouldHaveConstraintViolation() {
        String customerParam = " ";
        String channelParam = " ";
        String body = "";

        var customerRecord = new InteractionRecord(customerParam, channelParam, body);
        var violations = validator.validate(customerRecord);

        assertEquals(3, violations.size(), "Invalid Interaction should have three constraint violations");
    }

    @Test
    @DisplayName("Must Accept a valid Interaction successful")
    public void whenInteractionValidThenShouldBeValid() {
        String customerParam = "email@gmail.com";
        String channelParam = "another@email.com.br";
        String body = "Hello World!";

        var customerRecord = new InteractionRecord(customerParam, channelParam, body);
        var violations = validator.validate(customerRecord);

        assertTrue(violations.isEmpty(), "Valid Interaction should not have constraint violations");
    }
}
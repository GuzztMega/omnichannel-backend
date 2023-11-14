package br.com.guzzmega.omnichannel.domain;

import br.com.guzzmega.omnichannel.domain.record.CustomerRecord;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerRecordTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Must Accept a Valid Customer")
    public void whenCustomerValidThenNoConstraintViolation() {
        String name = "Customer Name";
        String email = "customer@example.com";
        String phoneNumber = "";

        var customerRecord = new CustomerRecord(name, email, phoneNumber);
        var violations = validator.validate(customerRecord);

        assertTrue(violations.isEmpty(), "Valid CustomerRecord should not have constraint violations");
    }

    @Test
    @DisplayName("Must Reject Invalid Customer Name")
    public void whenBlankNameThenShouldHaveConstraintViolation() {
        String name = " ";
        String email = "customer@example.com";
        String phoneNumber = "1234567890";

        var customerRecord = new CustomerRecord(name, email, phoneNumber);
        var violations = validator.validate(customerRecord);

        assertFalse(violations.isEmpty(), "CustomerRecord with blank name should have constraint violations");
    }

    @Test
    @DisplayName("Must Reject Invalid Customer Email")
    public void whenInvalidEmailThenShouldHaveConstraintViolation() {
        String name = "Customer Name";
        String email = "notAnEmail";
        String phoneNumber = null;

        CustomerRecord customerRecord = new CustomerRecord(name, email, phoneNumber);
        var violations = validator.validate(customerRecord);

        assertFalse(violations.isEmpty(), "CustomerRecord with invalid email should have constraint violations");
    }
}
package com.andreea.ticket_tracker.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TicketRequestDTOTest {

    @Test
    void testAllFieldsValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setTitle("");
        dto.setDescription("a".repeat(300));
        dto.setPosition(-1);
        dto.setBoardId(null);

        Set<ConstraintViolation<TicketRequestDTO>> violations = validator.validate(dto);

        assertEquals(5, violations.size());

        Set<String> messages = violations.stream()
                .map(v -> v.getMessage())
                .collect(Collectors.toSet());

        assertTrue(messages.contains("title_is_required"));
        assertTrue(messages.contains("title_length_invalid"));
        assertTrue(messages.contains("Description too long"));
        assertTrue(messages.contains("Position must be >= 0"));
        assertTrue(messages.contains("BoardId cannot be null"));
    }
}

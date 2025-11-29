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

public class ProjectRequestDTOTest {

    @Test
    void testAllFieldsValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        ProjectRequestDTO dto = new ProjectRequestDTO();
        dto.setName("");
        dto.setDescription("a".repeat(300));

        Set<ConstraintViolation<ProjectRequestDTO>> violations = validator.validate(dto);

        assertEquals(3, violations.size());

        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertTrue(messages.contains("name_is_required"));
        assertTrue(messages.contains("name_length_invalid"));
        assertTrue(messages.contains("Description too long"));
    }
}

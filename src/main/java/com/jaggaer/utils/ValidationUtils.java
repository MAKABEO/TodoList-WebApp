package com.jaggaer.utils;

import com.jaggaer.dto.TaskDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ValidationUtils {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static Map<String, String> validateTask(TaskDTO taskDTO) {
        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);
        Map<String, String> errors = new HashMap<>();

        for (ConstraintViolation<TaskDTO> violation : violations) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return errors;
    }
}

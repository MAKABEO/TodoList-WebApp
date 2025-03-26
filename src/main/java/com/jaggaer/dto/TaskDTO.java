package com.jaggaer.dto;

import com.jaggaer.model.enums.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDTO {
    private String id;

    @NotBlank(message = "Title is required.")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters.")
    private String title;

    @NotBlank(message = "Description is required.")
    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters.")
    private String description;

    @NotNull(message = "Status is required.")
    private Status status;

    @NotNull(message = "Start date is required.")
    @FutureOrPresent(message = "Start date cannot be in the past.")
    private LocalDate startDate;

    @NotNull(message = "Target date is required.")
    @FutureOrPresent(message = "Target date must be today or in the future.")
    private LocalDate targetDate;
}

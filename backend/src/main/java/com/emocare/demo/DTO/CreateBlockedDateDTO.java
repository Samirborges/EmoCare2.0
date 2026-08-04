package com.emocare.demo.DTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateBlockedDateDTO(
        @NotNull LocalDate blockedDate,
        boolean fullDay,
        LocalTime startTime,
        LocalTime endTime,
        String reason
        ) {
}

package com.emocare.demo.DTO;

import com.emocare.demo.entity.enums.Weekday;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record CreateAvailabilitySlotDTO(
        @NotNull Weekday weekday,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime
        ) {
}

package com.emocare.demo.DTO;

import com.emocare.demo.entity.ProfessionalAvailability;
import com.emocare.demo.entity.enums.Weekday;

import java.time.LocalTime;
import java.util.UUID;

public record AvailabilitySlotDTO(UUID id, Weekday weekday, LocalTime startTime, LocalTime endTime) {
    public static AvailabilitySlotDTO from(ProfessionalAvailability a) {
        return new AvailabilitySlotDTO(a.getId(), a.getWeekday(), a.getStartTime(), a.getEndTime());
    }
}

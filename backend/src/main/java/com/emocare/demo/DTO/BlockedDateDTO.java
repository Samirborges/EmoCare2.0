package com.emocare.demo.DTO;

import com.emocare.demo.entity.BlockedDate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record BlockedDateDTO(UUID id, LocalDate blockedDate, boolean fullDay, LocalTime startTime, LocalTime endTime, String reason) {
    public static BlockedDateDTO from(BlockedDate b) {
        return new BlockedDateDTO(b.getId(), b.getBlockedDate(), b.getIsFullDay(), b.getStartTime(), b.getEndTime(), b.getReason());
    }
}

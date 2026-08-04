package com.emocare.demo.DTO;

import com.emocare.demo.entity.Absence;
import com.emocare.demo.entity.enums.AbsenceStatus;
import com.emocare.demo.entity.enums.AbsenceType;

import java.time.LocalDate;
import java.util.UUID;

public record AbsenceDTO(UUID id, AbsenceType type, LocalDate startDate, LocalDate endDate, String reason, AbsenceStatus status) {
    public static AbsenceDTO from(Absence a) {
        return new AbsenceDTO(a.getId(), a.getType(), a.getStartDate(), a.getEndDate(), a.getReason(), a.getStatus());
    }
}

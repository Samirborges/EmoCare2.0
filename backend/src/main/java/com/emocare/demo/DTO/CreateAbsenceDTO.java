package com.emocare.demo.DTO;


import com.emocare.demo.entity.enums.AbsenceType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateAbsenceDTO(
        @NotNull AbsenceType type,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        String reason
) {}

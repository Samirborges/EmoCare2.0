package com.emocare.demo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record UpdateUserDTO(
        String fullName,
        String photoUrl,
        @JsonFormat(pattern = "yyyy-MM-dd")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate birthDate,
        String phone,

        // Professional-specific
        String crp,
        String biography,
        String therapeuticApproach,
        Short experienceYears
) {

}

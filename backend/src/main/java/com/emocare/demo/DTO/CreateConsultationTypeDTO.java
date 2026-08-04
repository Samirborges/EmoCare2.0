package com.emocare.demo.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateConsultationTypeDTO(
        @NotBlank String name,
        @NotNull @Min(1) Short durationMinutes,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal price
        ) {
}

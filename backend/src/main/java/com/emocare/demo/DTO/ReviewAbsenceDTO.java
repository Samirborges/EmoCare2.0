package com.emocare.demo.DTO;

import jakarta.validation.constraints.NotNull;

public record ReviewAbsenceDTO(@NotNull Boolean approve, String notes) {
}

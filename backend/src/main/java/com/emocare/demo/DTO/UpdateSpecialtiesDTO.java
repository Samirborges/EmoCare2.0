package com.emocare.demo.DTO;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record UpdateSpecialtiesDTO(@NotEmpty List<UUID> specialtyIds) {
}

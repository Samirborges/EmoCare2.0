package com.emocare.demo.DTO;

import com.emocare.demo.entity.Specialty;

import java.util.UUID;

public record SpecialtyDTO(UUID id, String name, String description) {
    public static SpecialtyDTO from(Specialty specialty) {
        return new SpecialtyDTO(specialty.getId(), specialty.getName(), specialty.getDescription());
    }
}

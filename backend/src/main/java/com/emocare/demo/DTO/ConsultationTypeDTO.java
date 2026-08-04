package com.emocare.demo.DTO;


import com.emocare.demo.entity.ConsultationType;

import java.math.BigDecimal;
import java.util.UUID;

public record ConsultationTypeDTO(UUID id, String name, Short durationMinutes, BigDecimal price) {
    public static ConsultationTypeDTO from(ConsultationType ct) {
        return new ConsultationTypeDTO(ct.getId(), ct.getName(), ct.getDurationMinutes(), ct.getPrice());
    }
}

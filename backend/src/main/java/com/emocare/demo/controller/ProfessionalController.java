package com.emocare.demo.controller;

import com.emocare.demo.DTO.SpecialtyDTO;
import com.emocare.demo.DTO.UpdateSpecialtiesDTO;
import com.emocare.demo.service.SpecialtyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/professionals/me")
public class ProfessionalController {

    private final SpecialtyService specialtyService;

    public ProfessionalController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @PutMapping("/specialties")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public ResponseEntity<List<SpecialtyDTO>> updateSpecialties(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateSpecialtiesDTO dto) {

        UUID professionalId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(specialtyService.updateProfessionalSpecialties(professionalId, dto.specialtyIds()));
    }
}

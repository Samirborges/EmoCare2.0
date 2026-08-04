package com.emocare.demo.controller;

import com.emocare.demo.DTO.AbsenceDTO;
import com.emocare.demo.DTO.ReviewAbsenceDTO;
import com.emocare.demo.service.AbsenceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/absences")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAbsenceController {

    private final AbsenceService service;

    public AdminAbsenceController(AbsenceService service) {
        this.service = service;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<AbsenceDTO>> listPending() {
        return ResponseEntity.ok(service.listPendingVacations());
    }

    @PatchMapping("/{id}/review")
    public ResponseEntity<AbsenceDTO> review(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id, @Valid @RequestBody ReviewAbsenceDTO dto) {
        UUID adminId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(service.reviewVacation(id, adminId, dto.approve(), dto.notes()));
    }
}

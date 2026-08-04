package com.emocare.demo.controller;

import com.emocare.demo.DTO.AbsenceDTO;
import com.emocare.demo.DTO.CreateAbsenceDTO;
import com.emocare.demo.service.AbsenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/professionals/me/absences")
@PreAuthorize("hasRole('PROFESSIONAL')")
public class AbsenceController {

    private final AbsenceService service;

    public AbsenceController(AbsenceService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AbsenceDTO>> list(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.listMine(UUID.fromString(jwt.getSubject())));
    }

    @PostMapping
    public ResponseEntity<AbsenceDTO> create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateAbsenceDTO dto) {
        var created = service.create(UUID.fromString(jwt.getSubject()), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
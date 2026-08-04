package com.emocare.demo.controller;

import com.emocare.demo.DTO.ConsultationTypeDTO;
import com.emocare.demo.DTO.CreateConsultationTypeDTO;
import com.emocare.demo.service.ConsultationTypeService;
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
@RequestMapping("/professionals/me/consultation-types")
@PreAuthorize("hasRole('PROFESSIONAL')")
public class ConsultationTypeController {

    private final ConsultationTypeService service;

    public ConsultationTypeController(ConsultationTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ConsultationTypeDTO>> list(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.listMine(UUID.fromString(jwt.getSubject())));
    }

    @PostMapping
    public ResponseEntity<ConsultationTypeDTO> create(@AuthenticationPrincipal Jwt jwt,
                                                      @Valid @RequestBody CreateConsultationTypeDTO dto) {
        var created = service.create(UUID.fromString(jwt.getSubject()), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultationTypeDTO> update(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id,
                                                      @Valid @RequestBody CreateConsultationTypeDTO dto) {
        return ResponseEntity.ok(service.update(UUID.fromString(jwt.getSubject()), id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        service.deactivate(UUID.fromString(jwt.getSubject()), id);
        return ResponseEntity.noContent().build();
    }

}

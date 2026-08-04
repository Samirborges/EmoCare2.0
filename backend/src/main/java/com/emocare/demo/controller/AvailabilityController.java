package com.emocare.demo.controller;

import com.emocare.demo.DTO.AvailabilitySlotDTO;
import com.emocare.demo.DTO.CreateAvailabilitySlotDTO;
import com.emocare.demo.DTO.UpdateBufferDTO;
import com.emocare.demo.service.AvailabilityService;
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
@RequestMapping("/professionals/me/availability")
@PreAuthorize("hasRole('PROFESSIONAL')")
public class AvailabilityController {

    private final AvailabilityService service;

    public AvailabilityController(AvailabilityService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AvailabilitySlotDTO>> list(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.listMine(UUID.fromString(jwt.getSubject())));
    }

    @PostMapping
    public ResponseEntity<AvailabilitySlotDTO> create(@AuthenticationPrincipal Jwt jwt,
                                                      @Valid @RequestBody CreateAvailabilitySlotDTO dto) {
        var created = service.create(UUID.fromString(jwt.getSubject()), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        service.deactivate(UUID.fromString(jwt.getSubject()), id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/buffer")
    public ResponseEntity<Void> updateBuffer(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdateBufferDTO dto) {
        service.updateBuffer(UUID.fromString(jwt.getSubject()), dto.bufferMinutes());
        return ResponseEntity.noContent().build();
    }
}

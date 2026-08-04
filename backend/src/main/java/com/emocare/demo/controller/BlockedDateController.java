package com.emocare.demo.controller;

import com.emocare.demo.DTO.BlockedDateDTO;
import com.emocare.demo.DTO.CreateBlockedDateDTO;
import com.emocare.demo.service.BlockedDateService;
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
@RequestMapping("/professionals/me/blocked-dates")
@PreAuthorize("hasRole('PROFESSIONAL')")
public class BlockedDateController {

    private final BlockedDateService service;

    public BlockedDateController(BlockedDateService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BlockedDateDTO>> list(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.listMine(UUID.fromString(jwt.getSubject())));
    }

    @PostMapping
    public ResponseEntity<BlockedDateDTO> create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody CreateBlockedDateDTO dto) {
        var created = service.create(UUID.fromString(jwt.getSubject()), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID id) {
        service.delete(UUID.fromString(jwt.getSubject()), id);
        return ResponseEntity.noContent().build();
    }
}
package com.emocare.demo.controller;

import com.emocare.demo.DTO.AdminCreateUserAdminRequestDTO;
import com.emocare.demo.DTO.AdminCreateUserProfessionalRequestDTO;
import com.emocare.demo.DTO.UserResponseDTO;
import com.emocare.demo.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getUsers(){
        return ResponseEntity.ok(service.bringUsers());
    }

    @PostMapping("/create-user-professional")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createUser(@Valid @RequestBody AdminCreateUserProfessionalRequestDTO userDTO) {

        service.createUser(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/create-user-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createUser(@Valid @RequestBody AdminCreateUserAdminRequestDTO userDTO) {
        service.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
